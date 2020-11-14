package com.miyin.zhenbaoqi.widget.flow_layout

import android.content.Context
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout

class TagView(context: Context) : FrameLayout(context), Checkable {

    private var isChecked = false
    val tagView: View
        get() = getChildAt(0)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val states = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            View.mergeDrawableStates(states, CHECK_STATE)
        }
        return states
    }

    override fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            isChecked = checked
            refreshDrawableState()
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    companion object {
        private val CHECK_STATE = intArrayOf(android.R.attr.state_checked)
    }

}
