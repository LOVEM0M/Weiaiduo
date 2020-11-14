package com.miyin.zhenbaoqi.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

object Internals {

    @JvmStatic
    fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val key = it.first
            val value = it.second
            when (value) {
                null -> intent.putExtra(key, null as Serializable?)
                is Int -> intent.putExtra(key, value)
                is Long -> intent.putExtra(key, value)
                is CharSequence -> intent.putExtra(key, value)
                is String -> intent.putExtra(key, value)
                is Float -> intent.putExtra(key, value)
                is Double -> intent.putExtra(key, value)
                is Char -> intent.putExtra(key, value)
                is Short -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                is Serializable -> intent.putExtra(key, value)
                is Bundle -> intent.putExtra(key, value)
                is Parcelable -> intent.putExtra(key, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(key, value)
                    value.isArrayOf<String>() -> intent.putExtra(key, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(key, value)
                    else -> throw Exception("Intent extra $key has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(key, value)
                is LongArray -> intent.putExtra(key, value)
                is FloatArray -> intent.putExtra(key, value)
                is DoubleArray -> intent.putExtra(key, value)
                is CharArray -> intent.putExtra(key, value)
                is ShortArray -> intent.putExtra(key, value)
                is BooleanArray -> intent.putExtra(key, value)
                else -> throw Exception("Intent extra $key has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

}
