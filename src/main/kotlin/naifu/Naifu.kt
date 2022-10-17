package com.evolvedghost.naifu

import com.evolvedghost.MiraiNovelaiNaifuConfig.additionalPrompt
import com.evolvedghost.MiraiNovelaiNaifuConfig.apiSrc
import com.evolvedghost.MiraiNovelaiNaifuConfig.connectTimeout
import com.evolvedghost.MiraiNovelaiNaifuConfig.height
import com.evolvedghost.MiraiNovelaiNaifuConfig.ignoreCertError
import com.evolvedghost.MiraiNovelaiNaifuConfig.noise
import com.evolvedghost.MiraiNovelaiNaifuConfig.readTimeout
import com.evolvedghost.MiraiNovelaiNaifuConfig.sampler
import com.evolvedghost.MiraiNovelaiNaifuConfig.samples
import com.evolvedghost.MiraiNovelaiNaifuConfig.scale
import com.evolvedghost.MiraiNovelaiNaifuConfig.seed
import com.evolvedghost.MiraiNovelaiNaifuConfig.steps
import com.evolvedghost.MiraiNovelaiNaifuConfig.strength
import com.evolvedghost.MiraiNovelaiNaifuConfig.undesiredContent
import com.evolvedghost.MiraiNovelaiNaifuConfig.width
import com.evolvedghost.naifu.data.*
import com.evolvedghost.utils.DebugMode
import com.evolvedghost.utils.HTTPClient
import com.google.gson.GsonBuilder
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import java.net.URLEncoder
import java.security.SecureRandom
import java.util.*


class Naifu() {
    private var tags = String()

    constructor(tags: String) : this() {
        this.tags = tags
    }

    fun searchTag(): TagVal {
        //构建url地址
        val url = apiSrc + "predict-tags?prompt=" + URLEncoder.encode(tags, "utf-8")
        return try {
            val response = HTTPClient(url, connectTimeout, readTimeout, ignoreCertError).get()
            val responseData = response.body?.string().toString()
            val t = GsonBuilder().create().fromJson(responseData, PredictTagData::class.java)
            TagVal(true, t, String())
        } catch (e: Exception) {
            DebugMode().logException(e)
            TagVal(false, null, e.toString())
        }
    }

    fun text2image(): ReturnVal {
        //构建url地址
        val url = apiSrc + "generate-stream"
        return try {
            //构建Json字符串
            val gson = GsonBuilder().create()
            val newSeed = if (seed == -1L) {
                val random = SecureRandom()
                random.nextLong() % 4294967296
            } else {
                seed
            }
            val jsonString = gson.toJson(
                PostData(
                    prompt = additionalPrompt + tags,
                    uc = undesiredContent,
                    width = width,
                    height = height,
                    scale = scale,
                    sampler = sampler,
                    steps = steps,
                    seed = newSeed,
                    n_samples = samples,
                    image = null,
                    strength = null,
                    noise = null
                )
            )
            DebugMode().logText("请求的Json字段为：$jsonString")
            getReturnVal(url, jsonString)
        } catch (e: Exception) {
            DebugMode().logException(e)
            ReturnVal(false, mutableListOf(), e.toString())
        }
    }

    suspend fun image2image(image: Image): ReturnVal {
        return try {
            val img = HTTPClient(image.queryUrl(), connectTimeout, readTimeout, ignoreCertError).get().body?.bytes()
                ?: return ReturnVal(false, mutableListOf(), "原图片错误或下载失败")
            val url = apiSrc + "generate-stream"
            //构建Json字符串
            val gson = GsonBuilder().create()
            val newSeed = if (seed == -1L) {
                val random = SecureRandom()
                random.nextLong() % 4294967296
            } else {
                seed
            }
            val data = PostData(
                prompt = additionalPrompt + tags,
                uc = undesiredContent,
                width = width,
                height = height,
                scale = scale,
                sampler = sampler,
                steps = steps,
                seed = newSeed,
                n_samples = samples,
                image = null,
                strength = strength,
                noise = noise
            )
            DebugMode().logPostData(data)
            data.image = Base64.getEncoder().encodeToString(img)
            val jsonString = gson.toJson(data)
            getReturnVal(url, jsonString)
        } catch (e: Exception) {
            DebugMode().logException(e)
            ReturnVal(false, mutableListOf(), e.toString())
        }
    }

    private fun getReturnVal(url: String, jsonString: String): ReturnVal {
        val response = HTTPClient(url, connectTimeout, readTimeout, ignoreCertError).post(jsonString)
        val responseData = response.body?.string().toString()
        return if (response.body?.contentType().toString().contains("text/event-stream")) {
            val lines = responseData.lines()
            val list = mutableListOf<ByteArray>()
            for (index in 2..lines.size step 4) {
                list.add(Base64.getDecoder().decode(lines[index].substring(5)))
            }
            ReturnVal(true, list, String())
        } else if (response.body?.contentType().toString().contains("application/json")) {
            val error = GsonBuilder().create().fromJson(responseData, ErrorData::class.java)
            ReturnVal(false, mutableListOf(), error.error)
        } else {
            ReturnVal(false, mutableListOf(), responseData)
        }
    }
}