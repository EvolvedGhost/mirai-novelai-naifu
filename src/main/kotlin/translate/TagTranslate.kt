package translate

import com.evolvedghost.MiraiNovelaiNaifu.logger
import com.evolvedghost.MiraiNovelaiNaifuConfig
import com.evolvedghost.translate.data.ReturnVal
import com.evolvedghost.translate.data.TranslateData
import com.evolvedghost.utils.DebugMode
import com.evolvedghost.utils.HTTPClient
import com.github.pekoto.fastfuzzystringmatcher.StringMatcher
import com.google.gson.Gson
import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import translate.data.EHTAG
import java.util.*
import java.util.regex.Pattern

@OptIn(DelicateCoroutinesApi::class)
class TagTranslate {
    private var matcherEhTag = StringMatcher<String>()
    private var matcherEhTagWhen = Date()
    private val matcherEhTagMutex = Mutex()
    private var matcherEhTagAlive = false

    init {
        GlobalScope.launch {
            while (true) {
                refreshEhTag()
                if (matcherEhTagAlive) {
                    delay(86400000)
                } else {
                    delay(600000)
                }
            }
        }
    }

    suspend fun get(tag: String): ReturnVal {
        var finalTag = tag
        val regs = arrayOf(
            "！", "，", "。", "；", "~", "《", "》", "（", "）", "？",
            "”", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’", "!", ",",
            ".", ";", "`", "<", ">", "(", ")", "?", "'", "{", "}", "\"",
            ":", "{", "}", "\"", "\'", "\'"
        )
        // 将中文标点翻译为英文
        for (i in 0 until regs.size / 2) {
            finalTag = finalTag.replace(regs[i], regs[i + regs.size / 2])
        }
        val pattern = Pattern.compile("^[A-Za-z0-9`~!@#$%^&*()\\-_=+\\[\\]{};:'\",<.>/?\\\\|]+$")
        if (pattern.matcher(finalTag).matches()) {
            return ReturnVal(false, finalTag)
        }
        // 此处拆词
        val zhWords = mutableListOf<String>()
        val enWords = mutableListOf<String>()
        var webWords = mutableListOf<String>()
        finalTag.split(',').map {
            if (pattern.matcher(it).matches()) {
                enWords.add(it)
            } else {
                zhWords.add(it)
            }
        }
        // 此处找EHTAG
        if (matcherEhTagAlive) {
            zhWords.map {
                val word = searchEhTag(it)
                if (word == null) {
                    webWords.add(it)
                } else {
                    enWords.add(word)
                }
            }
        } else {
            webWords = enWords
        }
        // 此处翻译
        val translateBody =
            HTTPClient(
                "https://fanyi.youdao.com/translate?&doctype=json&type=ZH_CN2EN&i=" +
                        webWords.joinToString(
                            ","
                        ),
                MiraiNovelaiNaifuConfig.connectTimeout,
                MiraiNovelaiNaifuConfig.readTimeout,
                MiraiNovelaiNaifuConfig.ignoreCertError
            ).get().body?.string()
        try {
            val translateData = Gson().fromJson(translateBody, TranslateData::class.java)
            enWords.add(translateData.translateResult[0][0].tgt)
        } catch (e: Exception) {
            DebugMode().logException(e)
        }
        return ReturnVal(true, enWords.joinToString(","))
    }

    private suspend fun refreshEhTag() {
        logger.info("翻译词库刷新中")
        try {
            val json = HTTPClient(
                "https://raw.githubusercontent.com/EhTagTranslation/DatabaseReleases/master/db.raw.json",
                10,
                20,
                true
            ).get().body?.string() ?: return
            val ehTag = Gson().fromJson(json, EHTAG::class.java)
            if (ehTag.head.committer.`when` == matcherEhTagWhen) {
                return
            }
            val matcher = StringMatcher<String>()
            for (data in ehTag.data) {
                for (tag in data.data) {
                    var input = tag.value.name.replace(" ", "")
                    input = EmojiParser.removeAllEmojis(input)
                    if (tag.value.name.contains("（")) {
                        val subName = input.substring(input.indexOf("（") + 1, input.indexOf("）"))
                        val mainName = input.replace("（$subName）", "")
                        matcher.add(mainName, tag.key)
                        matcher.add(subName, tag.key)
                    } else if (tag.value.name.contains("|")) {
                        val name = input.split("|")
                        matcher.add(name[0], tag.key)
                        matcher.add(name[1], tag.key)
                    } else {
                        matcher.add(tag.value.name, tag.key)
                    }
                }
            }
            matcherEhTagMutex.withLock {
                matcherEhTag = matcher
            }
            matcherEhTagWhen = ehTag.head.committer.`when`
            matcherEhTagAlive = true
        } catch (e: Exception) {
            DebugMode().logException(e)
        }
    }

    private suspend fun searchEhTag(keyword: String): String? {
        val result = matcherEhTagMutex.withLock {
            matcherEhTag.search(keyword, 0.75f)
        }
        return if (result.isEmpty() || result[0].matchPercentage < 75) {
            null
        } else {
            result[0].associatedData
        }
    }
}