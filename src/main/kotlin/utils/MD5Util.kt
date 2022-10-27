package com.evolvedghost.utils

import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object MD5Util {
    fun getMD5Str(str: String): String {
        var digest: ByteArray? = null
        try {
            val md5 = MessageDigest.getInstance("md5")
            digest = md5.digest(str.toByteArray(charset("utf-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        //16是表示转换为16进制数
        return BigInteger(1, digest).toString(16)
    }
}