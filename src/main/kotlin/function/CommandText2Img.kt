package com.evolvedghost.function

import com.evolvedghost.naifu.Naifu
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.*
import java.io.ByteArrayInputStream

suspend fun text2img(cc: CommandContext, tags: Array<out String>) {
    if(cc.sender.isConsole()){
        cc.sender.sendMessage("不允许终端执行该命令")
        return
    }
    if (!checkPermission(cc.sender).allow) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("本群当前不允许AI绘图")
        })
        return
    }
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
    if (!value.success) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("请求错误：${value.error}")
        })
        return
    }
    val message = mutableListOf<Message>()
    message.add(QuoteReply(cc.originalMessage))
    for (image in value.image) {
        val img = cc.sender.subject?.uploadImage(
            ByteArrayInputStream(image)
        )
        if (img == null) {
            message.add(PlainText("图片上传错误"))
        } else {
            message.add(img)
        }
    }
    cc.sender.sendMessage(message.toMessageChain())
}