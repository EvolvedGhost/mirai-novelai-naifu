package com.evolvedghost

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MainConfig : AutoSavePluginConfig("AiConfig") {
    @ValueDescription("调试模式（非常吵捏）")
    var debug: Boolean by value(false)

    @ValueDescription("群管理员能否管理机器人")
    var adminMode: Boolean by value(true)

    @ValueDescription("超级管理员，无论是不是群管理都能管理机器人")
    var adminSuper: MutableList<Long> by value(mutableListOf())

    @ValueDescription("允许群聊")
    var groupAllow: Boolean by value(true)

    @ValueDescription("允许私聊")
    var personalAllow: Boolean by value(true)

    @ValueDescription("当前两者关闭后在该名单的仍可使用（群号QQ号均可）")
    var whiteList: MutableList<Long> by value(mutableListOf())

    @ValueDescription("图片等待事件（秒）（方便手机用户以图生图）")
    var imageWaitTime: Int by value(60)

    @ValueDescription("是否允许无限制的申请AI绘图")
    var concurrent: Boolean by value(true)

    @ValueDescription("若不允许无限制绘图冷却时间为（秒）")
    var coolDownTime: Long by value(10L)

    @ValueDescription("是否允许用户自定义设置参数（除了samples（出图数量）和bannedContent（强制屏蔽的用户输入标签）其他都会被用户指令覆盖）")
    var custom: Boolean by value(true)

    @ValueDescription("Api访问地址")
    var apiSrc: String by value("http://127.0.0.1:6969/")

    @ValueDescription("连接超时时间（秒）")
    var connectTimeout: Long by value(10L)

    @ValueDescription("读取超时时间（秒）")
    var readTimeout: Long by value(60L)

    @ValueDescription("忽略证书错误（如果报证书相关错误请打开）")
    var ignoreCertError: Boolean by value(false)

    @ValueDescription("强制翻译中文TAG，请前往TranslateConfig配置详细参数")
    var translateTags: Boolean by value(false)

    @ValueDescription("除了用户的标签以外，额外强制加入的标签（将加载标签前面），清空则为不加")
    var additionalPrompt: String by value("masterpiece, best quality, ")

    @ValueDescription("默认的反标签，AI会尽量减少以下标签的可能性")
    var undesiredContent: String by value("nsfw, lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry")

    @ValueDescription("强制屏蔽的用户输入标签")
    var bannedContent: MutableList<String> by value(mutableListOf("nsfw", "naked", "r18"))

    @ValueDescription("图片宽，必须64的倍数，范围[64,1024]")
    var width: Int by value(512)

    @ValueDescription("图片高，必须64的倍数，范围[64,1024]")
    var height: Int by value(512)

    @ValueDescription("出图数量，范围[1,100]")
    var samples: Int by value(1)

    @ValueDescription("AI迭代次数，数值高效果较好响应更慢，范围[1,50]")
    var steps: Int by value(28)

    @ValueDescription("在高比例下，图片会更贴合标签，具有更精细的细节和清晰度。低比例通常会带来更大的创作自由度，但清晰度会降低，范围[1.1,100.0]")
    var scale: Float by value(12f)

    @ValueDescription("随机种子，若为-1则为随机生成，范围[0,4294967295]")
    var seed: Long by value(-1L)

    @ValueDescription("采样器，可选项：【推荐：k_euler_ancestral、k_euler、k_lms；其他：plms、ddim】")
    var sampler: String by value("k_euler_ancestral")

    @ValueDescription("控制上传图像的更改程度，较低的强度将生成更接近原始图像的图像，范围[0,0.99]，仅限以图出图")
    var strength: Float by value(0.7f)

    @ValueDescription("较高的噪声将增加添加到上载图像中的细节，但如果过高则会导致瑕疵。通常来说，噪声应始终小于强度，范围[0,0.99]，仅限以图出图")
    var noise: Float by value(0.2f)
}

object TranslateConfig : AutoSavePluginConfig("TranslateConfig") {
    @ValueDescription(
        """
        使用的翻译API：
        1、有道翻译（绝大部分IP不能使用）
        2、谷歌翻译（需配置代理）
        3、百度翻译（需申请APIKEY：https://fanyi-api.baidu.com/
    """
    )
    val translateApi: Int by value(1)

    @ValueDescription("谷歌翻译/基础词库是否使用代理")
    val useProxy: Boolean by value(true)

    @ValueDescription("代理地址（HTTP）")
    val proxyAddress: String by value("127.0.0.1")

    @ValueDescription("代理端口")
    val proxyPort: Int by value(10809)

    @ValueDescription("百度翻译APP ID")
    val baiduAppId: String by value()

    @ValueDescription("百度翻译密钥")
    val baiduSecretKey: String by value()
}