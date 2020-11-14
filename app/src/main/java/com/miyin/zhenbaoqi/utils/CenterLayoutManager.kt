package com.miyin.zhenbaoqi.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics

class CenterLayoutManager : androidx.recyclerview.widget.LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun smoothScrollToPosition(recyclerView: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State?, position: Int) {
        val smoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView.context) {
            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 100f / displayMetrics.densityDpi
            }
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

}
