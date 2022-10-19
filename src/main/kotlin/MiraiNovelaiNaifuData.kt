package com.evolvedghost

import com.evolvedghost.naifu.data.SettingVal
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object MiraiNovelaiNaifuData : AutoSavePluginData("data") { // "name" 是保存的文件名 (不带后缀)
    val groupPerm: MutableMap<Long, Boolean> by value()
    val personalSetting: MutableMap<Long, SettingVal> by value()
}