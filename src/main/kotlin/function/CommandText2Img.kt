package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.additionalPrompt
import com.evolvedghost.naifu.Naifu
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

suspend fun text2img(cc: CommandContext, tags: Array<out String>): Boolean {
    val tag = checkTags(cc, tags)
    if (tag.`return`) return true
    val keywords = tag.newTag
    cc.sender.sendMessage(buildMessageChain {
        +QuoteReply(cc.originalMessage)
        +PlainText("请稍后正在处理中")
    })
    val ai = Naifu(additionalPrompt + keywords, getConf(cc.sender.user?.id))
    val value = ai.text2image()
    sendImg(cc, value)
    return !value.success
}