package com.evolvedghost.function

import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

suspend fun switchPerm(context: CommandContext) {
    if (context.sender.isConsole()) {
        context.sender.sendMessage("不支持控制台执行")
        return
    }
    val perm = checkPermission(context.sender)
    if (!perm.group) {
        context.sender.sendMessage(buildMessageChain {
            +QuoteReply(context.originalMessage)
            +PlainText("仅支持群聊环境使用")
        })
        return
    }
    if (perm.banned) {
        context.sender.sendMessage(buildMessageChain {
            +QuoteReply(context.originalMessage)
            +PlainText("本群已被禁止使用AI绘图，无法切换")
        })
        return
    }
    if (perm.admin) {
        if (switchGroupPerm(context.sender.getGroupOrNull()!!.id)) {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText("已开启本群AI绘图功能")
            })
        } else {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText("已关闭本群AI绘图功能")
            })
        }
    } else {
        context.sender.sendMessage(buildMessageChain {
            +QuoteReply(context.originalMessage)
            +PlainText("你没有开关的权限")
        })
    }
}