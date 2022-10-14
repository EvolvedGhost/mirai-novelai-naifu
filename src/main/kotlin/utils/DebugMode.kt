package com.evolvedghost.utils

import com.evolvedghost.MiraiNovelaiNaifu.logger
import com.evolvedghost.MiraiNovelaiNaifuConfig.debug
import net.mamoe.mirai.utils.info

class DebugMode {
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
}