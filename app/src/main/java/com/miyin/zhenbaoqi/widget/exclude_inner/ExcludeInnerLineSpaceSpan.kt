package com.miyin.zhenbaoqi.widget.exclude_inner

import android.graphics.Paint
import android.text.style.LineHeightSpan

class ExcludeInnerLineSpaceSpan(height: Int) : LineHeightSpan {

    // TextView 行高
    private var mHeight = height

    override fun chooseHeight(text: CharSequence, start: Int, end: Int, spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt) {
        // 原始行高
        val originHeight = fm.descent - fm.ascent
        if (originHeight <= 0) {
            return
        }
        // 计算比例值
        val ratio = mHeight * 1.0f / originHeight
        // 根据最新行高，修改descent
        fm.descent = Math.round(fm.descent * ratio)
        // 根据最新行高，修改 ascent
        fm.ascent = fm.descent - mHeight
    }

}
