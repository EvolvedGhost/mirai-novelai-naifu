package com.evolvedghost.naifu.data

data class PostData(
    // 标签
    val prompt: String,
    // 反标签
    val uc: String,
    val width: Int,
    val height: Int,
    val scale: Float,
    val sampler: String,
    val steps: Int,
    val seed: Long,
    val n_samples: Int,
    var image: String?,
    var noise: Float?,
    var strength: Float?
)