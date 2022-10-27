package com.evolvedghost.translate.data

// 百度翻译
data class BDTranslateData(
    val from: String,
    val to: String,
    val trans_result: List<BDTransResult>
)

data class BDTransResult(
    val src: String,
    val dst: String
)