package com.miyin.zhenbaoqi.utils

import android.util.ArrayMap
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody

object JSONUtils {

    val gson = GsonBuilder().serializeNulls().create()

    fun createJSON(keyArray: Array<String>, valueArray: Array<Any>): RequestBody {
        val map = ArrayMap<String, Any>()
        keyArray.forEachIndexed { index, key ->
            map[key] = valueArray[index]
        }
        val json = gson.toJson(map)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
    }

    inline fun <reified T> fromJSON(json: String): T {
        return gson.fromJson(json, T::class.java)
    }

    inline fun <reified T> fromListJSON(json: String): List<T> {
        return gson.fromJson<List<T>>(json, ParameterizedTypeImpl(T::class.java))
    }

}
