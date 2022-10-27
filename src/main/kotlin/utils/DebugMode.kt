package com.evolvedghost.utils

import com.evolvedghost.MiraiNovelaiNaifu.logger
import com.evolvedghost.MainConfig.debug
import com.evolvedghost.naifu.data.PostData
import com.google.gson.GsonBuilder
import net.mamoe.mirai.utils.info

object DebugMode {
    fun logText(text: String) {
        if (debug) {
            logger.info { text }
        }
    }

    fun logException(e: Exception) {
        if (debug) {
            e.printStackTrace()
        }
    }

    fun logPostData(d: PostData) {
        if (debug) {
            logger.info { "Post请求Json字段不含Image：" + GsonBuilder().create().toJson(d) }
        }
    }
}