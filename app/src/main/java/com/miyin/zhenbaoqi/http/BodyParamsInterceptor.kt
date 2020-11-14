package com.miyin.zhenbaoqi.http

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.utils.JSONUtils
import okhttp3.*

class BodyParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var body = request.body()

        if (body is FormBody) {
            val formBody = body
            val map = ArrayMap<String, Any>()
            // 从 FormBody 中拿到请求参数，放入 FormMap 中
            for (i in 0 until formBody.size()) {
                map[formBody.name(i)] = formBody.value(i)
            }
            // 将 FormMap 转化为 JSON
            val json = JSONUtils.gson.toJson(map)
            // 重新修改 body 的内容
            body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        }

        request = request.newBuilder()
                .post(body)
                .build()
        return chain.proceed(request)
    }

}
