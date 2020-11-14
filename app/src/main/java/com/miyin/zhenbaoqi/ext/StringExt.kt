package com.miyin.zhenbaoqi.ext

import com.miyin.zhenbaoqi.utils.JSONUtils

inline fun <reified T> String.toEntityList() = JSONUtils.fromListJSON<T>(this)
