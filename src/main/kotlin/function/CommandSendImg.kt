package com.evolvedghost.function

import com.evolvedghost.naifu.data.ReturnVal
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.*
import java.io.ByteArrayInputStream

suspend fun sendImg(cc: CommandContext, value: ReturnVal) {
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