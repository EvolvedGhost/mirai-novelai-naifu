package com.evolvedghost.translate.data

// 有道翻译
data class YDTranslateData(
    val type: String,
    val errorCode: Int,
    val elapsedTime: Int,
    val translateResult: List<List<YDTranslateResult>>
)

data class YDTranslateResult(
    val src: String,
    val tgt: String,
)