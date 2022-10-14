package com.evolvedghost.function

import com.evolvedghost.naifu.data.ReturnVal
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.MessageEvent
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

suspend fun sendImg(event: MessageEvent, value: ReturnVal) {
    if (!value.success) {
        event.toCommandSender().sendMessage(buildMessageChain {
            +QuoteReply(event.message)
            +PlainText("请求错误：${value.error}")
        })
        return
    }
    val message = mutableListOf<Message>()
    message.add(QuoteReply(event.message))
    for (image in value.image) {
        val img = event.toCommandSender().subject?.uploadImage(
            ByteArrayInputStream(image)
        )
        if (img == null) {
            message.add(PlainText("图片上传错误"))
        } else {
            message.add(img)
        }
    }
    event.toCommandSender().sendMessage(message.toMessageChain())
}