package com.evolvedghost.function

import com.evolvedghost.MainConfig.additionalPrompt
import com.evolvedghost.MainConfig.imageWaitTime
import com.evolvedghost.naifu.Naifu
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

data class Img2imgWaitData(
    val timestamp: Long,
    val keywords: String,
)

val img2imgWaitMap = mutableMapOf<String, Img2imgWaitData>()
val img2imgWaitMapLock = Mutex()

suspend fun img2img(cc: CommandContext, tags: Array<out String>): Boolean {
    val tag = checkTags(cc, tags)
    if (tag.`return`) return true
    val keywords = tag.newTag
    val originImage = cc.originalMessage.find { it is Image } as Image?
    if (originImage == null) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("请于 $imageWaitTime 秒内发送图片")
        })
        img2imgWaitMapLock.withLock {
            if (cc.sender is GroupAwareCommandSender) {
                val sender = cc.sender as GroupAwareCommandSender
                img2imgWaitMap[sender.group.id.toString() + "-" + sender.user.id.toString()] =
                    Img2imgWaitData(System.currentTimeMillis(), keywords)
            } else {
                img2imgWaitMap[cc.sender.user?.id.toString()] =
                    Img2imgWaitData(System.currentTimeMillis(), keywords)
            }
        }
        return true
    }
    cc.sender.sendMessage(buildMessageChain {
        +QuoteReply(cc.originalMessage)
        +PlainText("请稍后正在处理中")
    })
    val ai = Naifu(additionalPrompt + keywords, getConf(cc.sender.user?.id))
    val value = ai.image2image(originImage)
    sendImg(cc, value)
    return !value.success
}

suspend fun img2imgAfterWait(event: MessageEvent) {
    val sender = event.toCommandSender()
    val key = if (sender is GroupAwareCommandSender) {
        sender.group.id.toString() + "-" + sender.user.id.toString()
    } else {
        sender.user!!.id.toString()
    }
    val data = img2imgWaitMapLock.withLock {
        img2imgWaitMap.remove(key)
    } ?: return
    if (startDraw(event.toCommandSender())) {
        val originImage = event.message.find { it is Image } as Image?
        if (originImage == null) {
            sender.sendMessage(buildMessageChain {
                +QuoteReply(event.message)
                +PlainText("未识别到图片")
            })
            endDraw(event.toCommandSender(), true)
            return
        }
        sender.sendMessage(buildMessageChain {
            +QuoteReply(event.message)
            +PlainText("请稍后正在处理中")
        })
        val ai = Naifu(additionalPrompt + data.keywords, getConf(event.toCommandSender().user?.id))
        val value = ai.image2image(originImage)
        sendImg(event, value)
        endDraw(event.toCommandSender(), !value.success)
    } else {
        sender.sendMessage(buildMessageChain {
            +QuoteReply(event.message)
            +PlainText(waitTimeDraw(event.toCommandSender()))
        })
    }
}