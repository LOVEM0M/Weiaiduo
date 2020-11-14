package com.miyin.zhenbaoqi.utils

import android.content.Context
import com.miyin.zhenbaoqi.App

object SPUtils {

    private val mSharedPref = App.context.getSharedPreferences("zhenbaoqi", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        mSharedPref.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        mSharedPref.edit().putBoolean(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        mSharedPref.edit().putInt(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = "") = mSharedPref.getString(key, defaultValue)
            ?: ""

    fun getBoolean(key: String, defaultValue: Boolean = false) = mSharedPref.getBoolean(key, defaultValue)

    fun getInt(key: String, defaultValue: Int = 0) = mSharedPref.getInt(key, defaultValue)

    fun remove(key: String) {
        mSharedPref.edit().remove(key).apply()
    }

    fun contains(key: String) = mSharedPref.contains(key)

    fun clear() {
        mSharedPref.edit().clear().apply()
    }

    fun getToken() = getString("token")

}
