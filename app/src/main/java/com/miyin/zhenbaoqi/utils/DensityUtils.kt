package com.miyin.zhenbaoqi.utils

import android.content.res.Resources

object DensityUtils {

    private val mDisplayMetrics = Resources.getSystem().displayMetrics

    fun getScreenWidth() = mDisplayMetrics.widthPixels

    fun getScreenHeight() = mDisplayMetrics.heightPixels

    fun dp2px(dpValue: Float): Int {
        val scale = mDisplayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale = mDisplayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(spValue: Float): Int {
        val fontScale = mDisplayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        val fontScale = mDisplayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

}
