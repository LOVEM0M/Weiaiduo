package com.miyin.zhenbaoqi.base

import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize

abstract class BaseAdapter<T>(list: List<T>) : BaseQuickAdapter<T, BaseViewHolder>(list) {

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(mContext).inflate(getContentView(), parent, false)
        return BaseViewHolder(view)
    }

    fun addNoMoreDataFooter() {
        if (footerLayoutCount != 0) {
            removeAllFooterView()
        }
        val context = App.context
        val footerView = TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, context.getDimensionPixelSize(R.dimen.dp_50))
            text = "已经到底啦~"
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, R.color.text_99))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_14))
        }
        addFooterView(footerView)
    }

    @LayoutRes
    protected abstract fun getContentView(): Int

}
