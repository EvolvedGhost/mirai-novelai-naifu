package com.evolvedghost

import com.evolvedghost.function.*
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

object MiraiNovelaiNaifuCommand : CompositeCommand(
    MiraiNovelaiNaifu,
    "ai",
    "绘图",
    description = "MiraiNAIFU指令",
) {
    @SubCommand("reload", "重载")
    @Description("快速重载插件配置项")
    suspend fun reload(context: CommandContext) {
        initConfig()
        if (context.sender.isConsole()) {
            context.sender.sendMessage("插件重载完成")
        } else {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText("插件重载完成")
            })
        }
    }

    @SubCommand("switch", "开关")
    @Description("开关本群出图权限[需管理员权限]")
    suspend fun switch(context: CommandContext) {
        switchPerm(context)
    }

    @SubCommand("text", "文本")
    @Description("以文本TAG进行绘图")
    suspend fun text(context: CommandContext, vararg tags: String) {
        if (startDraw(context.sender)) {
            endDraw(context.sender, text2img(context, tags))
        } else {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText(waitTimeDraw(context.sender))
            })
        }
    }

    @SubCommand("image", "图片")
    @Description("以文本TAG辅以图片进行绘图")
    suspend fun image(context: CommandContext, vararg tags: String) {
        if (startDraw(context.sender)) {
            endDraw(context.sender, img2img(context, tags))
        } else {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText(waitTimeDraw(context.sender))
            })
        }
    }

    @SubCommand("tag", "标签")
    @Description("模糊搜索标签")
    suspend fun tag(context: CommandContext, vararg tags: String) {
        searchTag(context, tags)
    }

    @SubCommand("custom", "自设")
    @Description("设置你自己的参数")
    suspend fun custom(context: CommandContext, vararg params: String) {
        if (context.sender.subject == null) {
            context.sender.sendMessage("未知/不允许的消息来源")
        } else {
            context.sender.sendMessage(buildMessageChain {
                +QuoteReply(context.originalMessage)
                +PlainText(setConf(context.sender.subject!!.id, params))
            })
        }
    }
}