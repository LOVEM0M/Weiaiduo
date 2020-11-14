package com.miyin.zhenbaoqi.http

import android.util.Log
import com.orhanobut.logger.Logger
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import com.google.gson.JsonSyntaxException
import java.nio.charset.UnsupportedCharsetException

class LoggerInterceptor : Interceptor {

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body()
        var body: String? = null
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            body = buffer.readString(charset)
        }

        Logger.d("发送请求: method：" + request.method() + "\nurl：" + request.url() + "\n请求参数: " + body)

        val response = chain.proceed(request)
        val responseBody = response.body()

        val source = responseBody?.source()
        source?.request(java.lang.Long.MAX_VALUE)
        val buffer = source?.buffer()

        var charset = UTF8
        val contentType = responseBody?.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8)
            } catch (e: UnsupportedCharsetException) {
                e.printStackTrace()
            }
        }
        val rBody = buffer?.clone()?.readString(charset)

        try {
            Logger.d("收到响应: code:" + response.code())
            Log.e("response",response.toString())
            Logger.json(rBody)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }

        return response
    }

}
