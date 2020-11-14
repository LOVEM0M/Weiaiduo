package com.miyin.zhenbaoqi.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager : androidx.viewpager.widget.ViewPager {

    private var mCanScroll = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    fun setCanScroll(canScroll: Boolean) {
        mCanScroll = canScroll
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (mCanScroll)
            super.onInterceptTouchEvent(event)
        else
            false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (mCanScroll) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

}
