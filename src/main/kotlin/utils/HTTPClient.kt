package com.evolvedghost.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class HTTPClient(
    private val url: String,
    private val cTimeout: Long,
    private val rTimeout: Long,
    private val ignoreCertificate: Boolean
) {
    fun post(json: String): Response {
        //调用请求
        val requestBody = json.let {
            val contentType = "application/json".toMediaType()
            json.toRequestBody(contentType)
        }
        val client = getClient()
        val request = Request.Builder().url(url).post(requestBody).build()
        return client.newCall(request).execute()
    }

    fun get(): Response {
        val client = getClient()
        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute()
    }

    private fun getClient(): OkHttpClient {
        val builder = if (ignoreCertificate) {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val newBuilder = OkHttpClient.Builder()
            newBuilder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            newBuilder.hostnameVerifier { _: String?, _: SSLSession? -> true }
            newBuilder
        } else {
            OkHttpClient.Builder()
        }
        return builder
            .connectTimeout(cTimeout, TimeUnit.SECONDS)
            .readTimeout(rTimeout, TimeUnit.SECONDS)
            .build();
    }
    // 忽略证书错误
    private var trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    )
}