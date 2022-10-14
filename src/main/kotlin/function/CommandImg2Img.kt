package com.evolvedghost.function

import com.evolvedghost.naifu.Naifu
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

suspend fun img2img(cc: CommandContext, tags: Array<out String>) {
    if (checkCommandInvalid(cc)) return
    var keywords = tags.joinToString(" ")
    keywords = keywords.replace("[图片]", "")
    keywords = keywords.replace("[动画表情]", "")
    val banWord = checkBannedWords(keywords)
    if (banWord != null) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("不允许的tag：$banWord")
        })
        return
    }
    val originImage = cc.originalMessage.find { it is Image } as Image?
    if (originImage == null) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("找不到图片")
        })
        return
    }
    val ai = Naifu(keywords)
    val value = ai.image2image(originImage)
    sendImg(cc, value)
}