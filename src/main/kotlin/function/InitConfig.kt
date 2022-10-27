package com.evolvedghost.function

import com.evolvedghost.MiraiNovelaiNaifu.logger
import com.evolvedghost.MiraiNovelaiNaifu.reload
import com.evolvedghost.MainConfig
import com.evolvedghost.MainConfig.concurrent
import com.evolvedghost.MainConfig.height
import com.evolvedghost.MainConfig.noise
import com.evolvedghost.MainConfig.sampler
import com.evolvedghost.MainConfig.samples
import com.evolvedghost.MainConfig.scale
import com.evolvedghost.MainConfig.seed
import com.evolvedghost.MainConfig.steps
import com.evolvedghost.MainConfig.strength
import com.evolvedghost.MainConfig.width
import com.evolvedghost.MiraiNovelaiNaifu.save
import com.evolvedghost.TranslateConfig
import com.evolvedghost.data.tagTranslate
import net.mamoe.mirai.utils.info

val samplerSet = setOf("k_euler_ancestral", "k_euler", "k_lms", "plms", "ddim")

fun initConfig() {
    MainConfig.reload()
    TranslateConfig.reload()
    if (samples < 1 || samples > 100) {
        logger.info { "samples 数值错误已还原默认值 1" }
        samples = 1
    }
    if (steps < 1 || steps > 50) {
        logger.info { "steps 数值错误已还原默认值 28" }
        steps = 28
    }
    if (scale < 1.1 || scale > 100) {
        logger.info { "scale 数值错误已还原默认值 12" }
        scale = 12f
    }
    if ((seed != -1L) && ((seed < 0) || (seed > 4294967295))) {
        logger.info { "seed 数值错误已还原默认值 -1" }
        seed = -1
    }
    if (!samplerSet.contains(sampler)) {
        logger.info { "sampler 数值错误已还原默认值 k_euler_ancestral" }
        sampler = "k_euler_ancestral"
    }
    if (width % 64 != 0 || width < 64 || width > 1024) {
        logger.info { "width 数值错误已还原默认值 512" }
        width = 512
    }
    if (height % 64 != 0 || height < 64 || height > 1024) {
        logger.info { "height 数值错误已还原默认值 512" }
        height = 512
    }
    if (strength < 0 || strength > 0.99) {
        logger.info { "strength 数值错误已还原默认值 0.7" }
        strength = 0.7f
    }
    if (noise < 0 || noise > 0.99) {
        logger.info { "noise 数值错误已还原默认值 0.2" }
        noise = 0.2f
    }
    if (concurrent) {
        drawCoolMap.clear()
    }
    tagTranslate
    MainConfig.save()
    TranslateConfig.save()
}