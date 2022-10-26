package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.bannedContent
import com.evolvedghost.data.tagTranslate
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

data class ReturnVal(
    val `return`: Boolean,
    val newTag: String,
)

suspend fun checkTags(cc: CommandContext, tags: Array<out String>): ReturnVal {
    if (checkCommandInvalid(cc)) return ReturnVal(true, String())
    var keywords = tags.joinToString(" ")
    keywords = keywords.replace("[图片]", "")
    keywords = keywords.replace("[动画表情]", "")
    val translate = tagTranslate.get(keywords)
    keywords = translate.newTag
    for (banWord in bannedContent) {
        if (keywords.contains(banWord)) {
            cc.sender.sendMessage(buildMessageChain {
                +QuoteReply(cc.originalMessage)
                +PlainText("不允许的tag：$banWord")
            })
            return ReturnVal(true, String())
        }
    }
    if (translate.translate) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("检测到tag非纯英文，已翻译为：$keywords")
        })
    }
    return ReturnVal(false, keywords)
}