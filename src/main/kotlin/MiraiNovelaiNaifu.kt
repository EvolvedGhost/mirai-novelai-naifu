package com.evolvedghost

import com.evolvedghost.function.initConfig
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object MiraiNovelaiNaifu : KotlinPlugin(
    JvmPluginDescription(
        id = "com.evolvedghost.mirai-novelai-naifu",
        name = "Mirai Novelai Naifu",
        version = "0.0.1",
    ) {
        author("EvolvedGhost")
        info("""NAIFU的Mirai插件""")
    }
) {
    override fun onEnable() {
        initConfig()
        MiraiNovelaiNaifuData.reload()
        MiraiNovelaiNaifuCommand.register()
        logger.info { "Mirai Novelai Naifu已经成功启动" }
    }

    override fun onDisable() {
        MiraiNovelaiNaifuConfig.save()
        MiraiNovelaiNaifuData.save()
        MiraiNovelaiNaifuCommand.unregister()
        logger.info { "Mirai Novelai Naifu已经成功卸载" }
    }
}