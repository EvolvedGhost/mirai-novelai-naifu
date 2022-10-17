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

suspend fun endDraw(cs: CommandSender) {
    if (concurrent) {
        return
    }
    val subject = cs.subject ?: return
    if (drawCoolMap[subject.id] == null) {
        drawCoolMap[subject.id] = DrawCoolDown()
        return
    }
    drawCoolMap[subject.id]!!.end()
}