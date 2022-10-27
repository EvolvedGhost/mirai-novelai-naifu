package com.evolvedghost.function

import com.evolvedghost.MainConfig.adminMode
import com.evolvedghost.MainConfig.adminSuper
import com.evolvedghost.MainConfig.groupAllow
import com.evolvedghost.MainConfig.personalAllow
import com.evolvedghost.MainConfig.whiteList
import com.evolvedghost.MiraiNovelaiNaifuData.groupPerm
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.buildMessageChain

data class Permit(
    val allow: Boolean,
    val admin: Boolean,
    val group: Boolean,
    val banned: Boolean,
)

suspend fun checkCommandInvalid(cc: CommandContext): Boolean {
    if (cc.sender.isConsole()) {
        cc.sender.sendMessage("不允许终端执行该命令")
        return true
    }
    if (!checkPermission(cc.sender).allow) {
        cc.sender.sendMessage(buildMessageChain {
            +QuoteReply(cc.originalMessage)
            +PlainText("本群当前不允许AI绘图")
        })
        return true
    }
    return false
}

fun checkGroupPerm(id: Long): Boolean {
    return !groupPerm.contains(id) || groupPerm[id] == true
}

fun checkPermission(cs: CommandSender): Permit {
    if (cs is GroupAwareCommandSender) {
        val group = cs.group
        val target = group.members[cs.user.id]
        return if ((groupAllow || whiteList.contains(group.id)) && checkGroupPerm(group.id)) {
            Permit(
                allow = true,
                admin = adminSuper.contains(cs.user.id) || (adminMode && target?.isOperator() == true),
                group = true,
                banned = false,
            )
        } else {
            Permit(
                allow = false,
                admin = adminSuper.contains(cs.user.id) || (adminMode && target?.isOperator() == true),
                group = true,
                banned = !(groupAllow || whiteList.contains(group.id)),
            )
        }
    } else {
        return if (!personalAllow && !whiteList.contains(cs.user?.id)) {
            Permit(allow = false, admin = false, group = false, banned = true)
        } else {
            Permit(allow = true, admin = false, group = false, banned = false)
        }
    }
}