package com.miyin.zhenbaoqi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver

class SoftKeyBoardListener(activity: Activity) {

    /* Activity 的根视图 */
    private val mRootView = activity.window.decorView
    /* 纪录根视图的显示高度 */
    private var mRootViewVisibleHeight = 0
    private var mOnSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var softKeyBoardListener: SoftKeyBoardListener? = null

        fun setListener(activity: Activity, onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
            softKeyBoardListener = SoftKeyBoardListener(activity)
            softKeyBoardListener?.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener)
        }

        fun removeListener() {
            if (null != softKeyBoardListener) {
                softKeyBoardListener?.removeSoftKeyBoardChangeListener()
                softKeyBoardListener = null
            }
        }

    }

    private val mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        /* 获取当前根视图在屏幕上显示的大小 */
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)
        val visibleHeight = r.height()
        if (mRootViewVisibleHeight == 0) {
            mRootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
        /* 根视图显示高度没有变化，可以看作软键盘显示/隐藏状态没有改变 */
        if (mRootViewVisibleHeight == visibleHeight) {
            return@OnGlobalLayoutListener
        }
        /* 根视图显示高度变小超过 200，可以看作软键盘显示了 */
        if (mRootViewVisibleHeight - visibleHeight > 200) {
            mOnSoftKeyBoardChangeListener?.keyBoardShow(mRootViewVisibleHeight - visibleHeight)
            mRootViewVisibleHeight = visibleHeight
            return@OnGlobalLayoutListener
        }
        /* 根视图显示高度变大超过 200，可以看作软键盘隐藏了 */
        if (visibleHeight - mRootViewVisibleHeight > 200) {
            mOnSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - mRootViewVisibleHeight)
            mRootViewVisibleHeight = visibleHeight
        }
    }

    init {
        /* 监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变 */
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    private fun removeSoftKeyBoardChangeListener() {
        mRootView.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    private fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
        mOnSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)

        fun keyBoardHide(height: Int)
    }

}
