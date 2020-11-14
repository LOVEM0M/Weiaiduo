package com.miyin.zhenbaoqi.widget

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.PopupWindow

class PopWindow(contentView: View, width: Int, height: Int, focusable: Boolean) : PopupWindow(contentView, width, height, focusable) {

    override fun showAsDropDown(anchor: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val visibleFrame = Rect()
            anchor.getGlobalVisibleRect(visibleFrame)
            val height = anchor.resources.displayMetrics.heightPixels - visibleFrame.bottom
            setHeight(height)
        }
        super.showAsDropDown(anchor)
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val visibleFrame = Rect()
            anchor.getGlobalVisibleRect(visibleFrame)
            val height = anchor.resources.displayMetrics.heightPixels - visibleFrame.bottom
            setHeight(height)
        }
        super.showAsDropDown(anchor, xoff, yoff)
    }

}
