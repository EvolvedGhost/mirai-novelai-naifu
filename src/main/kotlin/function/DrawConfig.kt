package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifuConfig.custom
import com.evolvedghost.MiraiNovelaiNaifuData.personalSetting
import com.evolvedghost.naifu.data.SettingVal
import com.google.gson.Gson
import java.util.*

fun getConf(user: Long?): SettingVal {
    if (!custom || user == null || !personalSetting.contains(user)) {
        return SettingVal()
    }
    return personalSetting[user] ?: return SettingVal()
}

fun setConf(user: Long, parm: Array<out String>): String {
    if (!custom) {
        return "不允许自定义配置项"
    }
    if (parm.isEmpty()) {
        return """配置项输入格式为/ai custom <key> <value>
如果你想要得到帮助请输入/ai custom help
如果你想要重置请输入/ai custom default
如果你查看你的配置请输入/ai custom check
"""
    }
    val keySet =
        mutableSetOf("negativeprompt", "width", "height", "scale", "sampler", "steps", "seed", "strength", "noise")
    val lowerKey = parm[0].lowercase(Locale.ENGLISH)
    if (lowerKey == "default" || lowerKey == "默认") {
        personalSetting[user] = SettingVal()
        return "已重置为默认"
    }
    if (lowerKey == "check" || lowerKey == "查看") {
        return "您当前配置为：\n${Gson().toJson(getConf(user))}"
    }
    if (lowerKey == "help" || lowerKey == "?" || lowerKey == "帮助" || lowerKey == "？" || !keySet.contains(lowerKey)) {
        val help = StringBuilder("可用以下标签：")
        help.append(keySet.joinToString("、"))
        return help.toString()
    }
    if (parm.size == 1) {
        return "value不可为空"
    }
    if (!personalSetting.contains(user)) {
        personalSetting[user] = SettingVal()
    }
    return personalSetting[user]?.set(lowerKey, parm[1]) ?: return "发生错误"
}