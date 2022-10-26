package com.evolvedghost.translate.data

data class TranslateData(
    val type: String,
    val errorCode: Int,
    val elapsedTime: Int,
    val translateResult: List<List<TranslateResult>>
)

data class TranslateResult(
    val src: String,
    val tgt: String,
)