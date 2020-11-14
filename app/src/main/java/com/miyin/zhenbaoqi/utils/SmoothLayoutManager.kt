package com.miyin.zhenbaoqi.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class SmoothLayoutManager(context: Context, spanCount: Int) : androidx.recyclerview.widget.GridLayoutManager(context, spanCount) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

}
