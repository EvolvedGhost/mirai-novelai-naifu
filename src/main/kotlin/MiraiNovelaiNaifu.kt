package com.evolvedghost

import com.evolvedghost.MiraiNovelaiNaifuConfig.imageWaitTime
import com.evolvedghost.function.img2imgAfterWait
import com.evolvedghost.function.img2imgWaitMap
import com.evolvedghost.function.initConfig
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.info

object MiraiNovelaiNaifu : KotlinPlugin(
    JvmPluginDescription(
        id = "com.evolvedghost.mirai-novelai-naifu",
        name = "Mirai Novelai Naifu",
        version = "0.0.3",
    ) {
        author("EvolvedGhost")
        info("""NAIFU的Mirai插件""")
    }
) {
    override fun onEnable() {
        initConfig()
        MiraiNovelaiNaifuData.reload()
        MiraiNovelaiNaifuCommand.register()
        globalEventChannel().subscribeMessages {
            content {
                val sender = this.toCommandSender()
                if (sender.isConsole()) false
                else if (sender is GroupAwareCommandSender) {
                    val data = img2imgWaitMap[sender.group.id.toString() + "-" + sender.user.id.toString()]
                    !(data == null || data.timestamp + (imageWaitTime * 1000) < System.currentTimeMillis())
                } else {
                    val data = img2imgWaitMap[sender.user!!.id.toString()]
                    !(data == null || data.timestamp + (imageWaitTime * 1000) < System.currentTimeMillis())
                }
            } quoteReply {
                img2imgAfterWait(this)
            }
        }
        logger.info { "Mirai Novelai Naifu已经成功启动" }
    }

    override fun onDisable() {
        MiraiNovelaiNaifuConfig.save()
        MiraiNovelaiNaifuData.save()
        MiraiNovelaiNaifuCommand.unregister()
        logger.info { "Mirai Novelai Naifu已经成功卸载" }
    }
}