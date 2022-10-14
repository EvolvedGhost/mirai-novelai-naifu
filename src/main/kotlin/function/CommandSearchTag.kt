package com.evolvedghost.function

import com.evolvedghost.naifu.Naifu
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

suspend fun searchTag(cc: CommandContext, tags: Array<out String>) {
    if (cc.sender.isConsole()) {
        cc.sender.sendMessage("不允许终端执行该命令")
        return
    }
    val keywords = tags.joinToString(" ")
    val tagData = Naifu(keywords).searchTag()
    if (!tagData.success || tagData.tags == null) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("请求错误：${tagData.error}")
        })
        return
    }
    val text = StringBuilder()
    text.append("可能的Tag有：")
    for (tag in tagData.tags.tags) {
        text.append(tag.tag)
        text.append("、")
    }
    text.deleteCharAt(text.length - 1)
    cc.sender.sendMessage(buildMessageChain {
        +QuoteReply(cc.originalMessage)
        +PlainText(text.toString())
    })
}