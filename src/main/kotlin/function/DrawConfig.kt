package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.custom
import com.evolvedghost.MiraiNovelaiNaifuData.personalSetting
import com.evolvedghost.naifu.data.SettingVal
import java.util.*

fun getConf(user: Long?): SettingVal {
    if (!custom || user == null || !personalSetting.contains(user)) {
        return SettingVal()
    }
    return personalSetting[user] ?: return SettingVal()
}

fun setConf(user: Long, key: String, value: String?): String {
    if (!custom) {
        return "不允许自定义配置项"
    }
    val keySet =
        mutableSetOf("negativeprompt", "width", "height", "scale", "sampler", "steps", "seed", "strength", "noise")
    val lowerKey = key.lowercase(Locale.ENGLISH)
    if (lowerKey == "default" || lowerKey == "默认") {
        personalSetting[user] = SettingVal()
        return "已重置为默认"
    }
    if (lowerKey == "help" || lowerKey == "?" || lowerKey == "帮助" || lowerKey == "？" || !keySet.contains(lowerKey)) {
        val help = StringBuilder("可用以下标签：")
        help.append(keySet.joinToString("、"))
        help.deleteCharAt(help.length - 1)
        return help.toString()
    }
    if (value == null) {
        return "value不可为空"
    }
    if (!personalSetting.contains(user)) {
        personalSetting[user] = SettingVal()
    }
    return personalSetting[user]?.set(lowerKey, value) ?: return "发生错误"
}