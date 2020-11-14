package com.miyin.zhenbaoqi.widget.flow_layout

import android.view.View

abstract class TagAdapter(list: List<Any>) {

    private var mList: List<Any> = list
    private var mOnDataChangedListener: OnDataChangedListener? = null

    interface OnDataChangedListener {
        fun onChanged()
    }

    fun setOnDataChangedListener(listener: OnDataChangedListener?) {
        mOnDataChangedListener = listener
    }

    val count: Int
        get() = mList.size

    fun notifyDataChanged() {
        if (mOnDataChangedListener != null) mOnDataChangedListener!!.onChanged()
    }

    fun getItem(position: Int): Any {
        return mList[position]
    }

    abstract fun getView(parent: FlowLayout, position: Int, data: Any): View

    open fun onSelected(position: Int, view: View?) {

    }

    open fun unSelected(position: Int, view: View?) {

    }

    fun setSelected(position: Int, t: Any): Boolean {
        return false
    }

}
