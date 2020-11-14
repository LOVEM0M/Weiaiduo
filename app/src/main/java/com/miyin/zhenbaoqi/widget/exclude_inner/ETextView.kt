package com.miyin.zhenbaoqi.widget.exclude_inner

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.text.Spanned
import android.text.SpannableStringBuilder
import android.util.AttributeSet

class ETextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * 排除每行文字间的 padding
     */
    fun setCustomText(text: CharSequence?) {
        if (text == null) {
            return
        }

        // 获得视觉定义的每行文字的行高
        val lineHeight = textSize.toInt()

        val ssb: SpannableStringBuilder
        if (text is SpannableStringBuilder) {
            ssb = text
            // 设置 LineHeightSpan
            ssb.setSpan(ExcludeInnerLineSpaceSpan(lineHeight), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            ssb = SpannableStringBuilder(text)
            // 设置 LineHeightSpan
            ssb.setSpan(ExcludeInnerLineSpaceSpan(lineHeight), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // 调用系统 setText( )方法
        setText(ssb)
    }

}
