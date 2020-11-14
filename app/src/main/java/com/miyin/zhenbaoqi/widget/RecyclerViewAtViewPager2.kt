package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class RecyclerViewAtViewPager2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    private var mStartX = 0
    private var mStartY = 0

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = ev.x.toInt()
                mStartY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()

                val disX = abs(endX - mStartX)
                val disY = abs(endY - mStartY)
                if (disX > disY) {
                    parent.requestDisallowInterceptTouchEvent(canScrollHorizontally(mStartX - endX))
                } else {
                    parent.requestDisallowInterceptTouchEvent(canScrollVertically(mStartY - endY))
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}