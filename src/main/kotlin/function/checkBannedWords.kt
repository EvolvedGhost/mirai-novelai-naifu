package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.bannedContent

fun checkBannedWords(input: String): String? {
    for (banWord in bannedContent) {
        if (input.contains(banWord)) {
            return banWord
        }
    }
    return null
}