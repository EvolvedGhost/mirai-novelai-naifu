package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.concurrent
import com.evolvedghost.utils.DrawCoolDown
import net.mamoe.mirai.console.command.CommandSender

val drawCoolMap = mutableMapOf<Long, DrawCoolDown>()

suspend fun startDraw(cs: CommandSender): Boolean {
    if (concurrent) {
        return true
    }
    val subject = cs.subject ?: return false
    if (drawCoolMap[subject.id] == null) {
        drawCoolMap[subject.id] = DrawCoolDown()
    }
    return drawCoolMap[subject.id]!!.start()
}

suspend fun endDraw(cs: CommandSender, instantly: Boolean) {
    if (concurrent) {
        return
    }
    val subject = cs.subject ?: return
    if (drawCoolMap[subject.id] == null) {
        drawCoolMap[subject.id] = DrawCoolDown()
        return
    }
    if (instantly) {
        drawCoolMap[subject.id]!!.endInstantly()
    } else {
        drawCoolMap[subject.id]!!.end()
    }
}

fun waitTimeDraw(cs: CommandSender): String {
    if (concurrent) {
        return "出现错误"
    }
    val subject = cs.subject ?: return "出现错误"
    if (drawCoolMap[subject.id] == null) {
        return "出现错误"
    }
    val left = drawCoolMap[subject.id]!!.getLeftTime()
    return if (left == -1L) {
        "请等待上一张图绘制完毕"
    } else {
        "现在是贤者时间，请等待 $left 秒"
    }
}