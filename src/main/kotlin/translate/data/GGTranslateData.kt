package com.evolvedghost.translate.data

// 谷歌翻译
data class GGTranslateData(
    val sentences: List<GGTranslateSentences>
)

data class GGTranslateSentences(
    val trans: String,
    val orig: String,
)

