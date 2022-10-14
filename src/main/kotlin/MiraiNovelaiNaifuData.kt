package com.evolvedghost

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object MiraiNovelaiNaifuData : AutoSavePluginData("data") { // "name" 是保存的文件名 (不带后缀)
    val groupPerm: MutableMap<Long, Boolean> by value()
}