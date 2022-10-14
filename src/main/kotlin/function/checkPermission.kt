package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.adminMode
import com.evolvedghost.MiraiNovelaiNaifuConfig.adminSuper
import com.evolvedghost.MiraiNovelaiNaifuConfig.groupAllow
import com.evolvedghost.MiraiNovelaiNaifuConfig.personalAllow
import com.evolvedghost.MiraiNovelaiNaifuConfig.whiteList
import com.evolvedghost.MiraiNovelaiNaifuData.groupPerm
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.contact.isOperator

data class Permit(
    val allow: Boolean,
    val admin: Boolean,
    val group: Boolean,
)

fun switchGroupPerm(id: Long): Boolean {
    val flag = !checkGroupPerm(id)
    groupPerm[id] = flag
    return flag
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
                group = true
            )
        } else {
            Permit(
                allow = false,
                admin = adminSuper.contains(cs.user.id) || (adminMode && target?.isOperator() == true),
                group = true
            )
        }
    } else {
        return if (!personalAllow && !whiteList.contains(cs.user?.id)) {
            Permit(allow = false, admin = false, group = false)
        } else {
            Permit(allow = true, admin = false, group = false)
        }
    }
}