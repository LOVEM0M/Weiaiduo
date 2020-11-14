package com.miyin.zhenbaoqi.http

import android.os.Handler
import android.os.Looper
import com.miyin.zhenbaoqi.BuildConfig
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object OkHttpUtils {

    private val mHandler = Handler(Looper.getMainLooper())
    val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                .writeTimeout(30000L, TimeUnit.MILLISECONDS)
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LoggerInterceptor())
        }
        builder.addInterceptor(HeaderInterceptor())
                .build()
    }

    fun get(url: String, params: Array<Pair<String, Any>>, successBlock: (String) -> Unit, failureBlock: (String) -> Unit) {
        var requestUrl = url
        if (params.isNotEmpty()) {
            params.forEachIndexed { index, pair ->
                requestUrl += if (index == 0) {
                    "?${pair.first}=${pair.second}"
                } else {
                    "&${pair.first}=${pair.second}"
                }
            }
        }
        val request = Request.Builder()
                .url(requestUrl)
                .get()
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                mHandler.post {
                    failureBlock(e?.message ?: "未知错误")
                }
                call?.cancel()
            }

            override fun onResponse(call: Call?, response: Response?) {
                mHandler.post {
                    val json = response?.body()!!.string()
                    successBlock(json)
                }
                call?.cancel()
            }
        })
    }

    fun post(url: String, params: Array<Pair<String, Any?>>, successBlock: (String) -> Unit, failureBlock: (String) -> Unit) {
        val builder = FormBody.Builder()
        if (params.isNotEmpty()) {
            params.forEach {
                builder.add(it.first, it.second.toString())
            }
        }
        val request = Request.Builder()
                .url(url)
                .post(builder.build())
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                mHandler.post {
                    failureBlock(e?.message ?: "未知错误")
                }
                call?.cancel()
            }

            override fun onResponse(call: Call?, response: Response?) {
                mHandler.post {
                    val json = response?.body()!!.string()
                    successBlock(json)
                }
                call?.cancel()
            }
        })
    }

    fun postBody(url: String, params: Array<Pair<String, Any?>>, successBlock: (String) -> Unit, failureBlock: (String) -> Unit) {
        val jsonObject = JSONObject()
        if (params.isNotEmpty()) {
            params.forEach {
                jsonObject.put(it.first, it.second)
            }
        }
        val mediaType = MediaType.parse("application/json;charset=UTF-8")
        val requestBody = RequestBody.create(mediaType, jsonObject.toString())
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                mHandler.post {
                    failureBlock(e?.message ?: "未知错误")
                }
                call?.cancel()
            }

            override fun onResponse(call: Call?, response: Response?) {
                mHandler.post {
                    val json = response?.body()!!.string()
                    successBlock(json)
                }
                call?.cancel()
            }
        })
    }

    fun postFile(url: String, key: String, path: String, successBlock: (String) -> Unit, failureBlock: (String) -> Unit) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("multipart-formdata"), file)
        builder.addFormDataPart(key, file.name, requestBody)
        val request = Request.Builder().url(url).post(builder.build()).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                mHandler.post {
                    failureBlock(e?.message ?: "未知错误")
                }
                call?.cancel()
            }

            override fun onResponse(call: Call?, response: Response?) {
                mHandler.post {
                    val json = response?.body()!!.string()
                    successBlock(json)
                }
                call?.cancel()
            }
        })
    }

}
