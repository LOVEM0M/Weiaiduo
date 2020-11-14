package com.miyin.zhenbaoqi.http

import com.miyin.zhenbaoqi.constant.Constant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {

    val mApiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(OkHttpUtils.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        retrofit.create(ApiService::class.java)
    }

}
