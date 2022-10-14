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
    val image: String?,
    val noise: Float?,
    val strength: Float?
)