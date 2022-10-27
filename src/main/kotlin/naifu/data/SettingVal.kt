package com.evolvedghost.naifu.data

import com.evolvedghost.MainConfig
import kotlinx.serialization.Serializable

@Serializable
data class SettingVal(
    var nPrompt: String = MainConfig.undesiredContent,
    var width: Int = MainConfig.width,
    var height: Int = MainConfig.height,
    var scale: Float = MainConfig.scale,
    var sampler: String = MainConfig.sampler,
    var steps: Int = MainConfig.steps,
    var seed: Long = MainConfig.seed,
    var strength: Float = MainConfig.strength,
    var noise: Float = MainConfig.noise,
) {
    fun set(key: String, value: String?): String {
        return when (key) {
            "nprompt" -> {
                nPrompt = value ?: ""
                "您的nprompt已设置为：$nPrompt"
            }
            "width" -> {
                val tempWidth = try {
                    value?.toInt()
                } catch (e: Exception) {
                    null
                }
                if (tempWidth == null || tempWidth % 64 != 0 || tempWidth < 64 || tempWidth > 1024) {
                    "设置错误，width必须为64的倍数，范围[64,1024]"
                } else {
                    width = tempWidth
                    "您的width已设置为：$width"
                }
            }
            "height" -> {
                val tempHeight = try {
                    value?.toInt()
                } catch (e: Exception) {
                    null
                }
                if (tempHeight == null || tempHeight % 64 != 0 || tempHeight < 64 || tempHeight > 1024) {
                    "设置错误，height必须为64的倍数，范围[64,1024]"
                } else {
                    height = tempHeight
                    "您的height已设置为：$height"
                }
            }
            "scale" -> {
                val tempScale = try {
                    value?.toFloat()
                } catch (e: Exception) {
                    null
                }
                if (tempScale == null || tempScale < 1.1 || tempScale > 100) {
                    "设置错误，scale范围[1.1,100.0]"
                } else {
                    scale = tempScale
                    "您的scale已设置为：$scale"
                }
            }
            "sampler" -> {
                val samplerSet = setOf("k_euler_ancestral", "k_euler", "k_lms", "plms", "ddim")
                if (value == null || !samplerSet.contains(value)) {
                    "设置错误，sampler可选项：【推荐：k_euler_ancestral、k_euler、k_lms；其他：plms、ddim】"
                } else {
                    sampler = value
                    "您的sampler已设置为：$sampler"
                }
            }
            "steps" -> {
                val tempSteps = try {
                    value?.toInt()
                } catch (e: Exception) {
                    null
                }
                if (tempSteps == null || tempSteps < 1 || tempSteps > 50) {
                    "设置错误，steps范围[1,50]"
                } else {
                    steps = tempSteps
                    "您的steps已设置为：$steps"
                }
            }
            "seed" -> {
                val tempSeed = try {
                    value?.toLong()
                } catch (e: Exception) {
                    null
                }
                if (tempSeed == null || ((tempSeed != -1L) && ((tempSeed < 0) || (tempSeed > 4294967295)))) {
                    "设置错误，seed可设置为-1（随机）或[0,4294967295]（固定）"
                } else {
                    seed = tempSeed
                    "您的seed已设置为：$seed"
                }
            }
            "strength" -> {
                val tempStrength = try {
                    value?.toFloat()
                } catch (e: Exception) {
                    null
                }
                if (tempStrength == null || tempStrength < 0 || tempStrength > 0.99) {
                    "设置错误，strength范围[0,0.99]（仅影响以图生图）"
                } else {
                    strength = tempStrength
                    "您的strength已设置为：$strength"
                }
            }
            "noise" -> {
                val tempNoise = try {
                    value?.toFloat()
                } catch (e: Exception) {
                    null
                }
                if (tempNoise == null || tempNoise < 0 || tempNoise > 0.99) {
                    "设置错误，noise范围[0,0.99]（仅影响以图生图）"
                } else {
                    noise = tempNoise
                    "您的noise已设置为：$noise"
                }
            }
            else -> {
                "发生错误"
            }
        }
    }
}
