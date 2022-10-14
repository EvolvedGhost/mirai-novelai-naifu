package com.evolvedghost.naifu.data

data class ReturnVal(
    val success: Boolean,
    val image: MutableList<ByteArray>,
    val error: String,
)