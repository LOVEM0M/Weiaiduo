package com.miyin.zhenbaoqi.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val SDF_THREAD_LOCAL = ThreadLocal<SimpleDateFormat>()

    fun getDefaultFormat() = getDateFormat("yyyy-MM-dd HH:mm:ss")

    fun getDateFormat(pattern: String): SimpleDateFormat {
        var simpleDateFormat: SimpleDateFormat? = SDF_THREAD_LOCAL.get()
        if (simpleDateFormat == null) {
            simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            SDF_THREAD_LOCAL.set(simpleDateFormat)
        } else {
            simpleDateFormat.applyPattern(pattern)
        }
        return simpleDateFormat
    }

    fun millis2String(timestamp: Long) = getDefaultFormat().format(Date(timestamp))!!

    fun millis2String(timestamp: Long, pattern: String) = getDateFormat(pattern).format(Date(timestamp))!!

    fun date2String(date: Date) = getDefaultFormat().format(date)

    fun date2String(date: Date, pattern: String) = getDateFormat(pattern).format(date)!!

    fun getFriendlyTime(timestamp: Long): String {
        val delta = (Date().time - timestamp) / 1000
        if (delta <= 0) return "刚刚"
        if (delta / (60 * 60 * 24) > 0) return millis2String(timestamp, "yyyy-MM-dd")
        if (delta / (60 * 60) > 0) return "${delta / (60 * 60)}小时前"
        if (delta / 60 > 0) return "${delta / 60}分钟前"
        return "刚刚"
    }

}
