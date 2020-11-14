package com.miyin.zhenbaoqi.http

import com.miyin.zhenbaoqi.utils.SPUtils
import com.orhanobut.logger.Logger
import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 添加 Header 参数
        // "de966b7d-51c6-4419-8fa5-b348878ee9a9"
        val token = SPUtils.getToken()
        Logger.d("token == $token")
        val newRequest = request.newBuilder()
                .header("client", "3")
                .header("token", token)
                .build()

        return chain.proceed(newRequest)
    }

}
