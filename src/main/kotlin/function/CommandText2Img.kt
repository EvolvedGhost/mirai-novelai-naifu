package com.evolvedghost.function

import com.evolvedghost.naifu.Naifu
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

suspend fun text2img(cc: CommandContext, tags: Array<out String>) {
    if (checkCommandInvalid(cc)) return
    val keywords = tags.joinToString(" ")
    val banWord = checkBannedWords(keywords)
    if (banWord != null) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("不允许的tag：$banWord")
        })
        return
    }
    val ai = Naifu(keywords)
    val value = ai.text2image()
    sendImg(cc, value)
}