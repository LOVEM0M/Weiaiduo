package com.miyin.zhenbaoqi.ui.shop.dialog

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.shop.adapter.DelayBidAdapter
import kotlinx.android.synthetic.main.dialog_delay_bid.*

class DelayBidDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private val mList = listOf(1, 3, 5, 10, 15, 20, 25, 30)
    private lateinit var mAdapter: DelayBidAdapter
    private var mSeconds = 0

    companion object {
        fun newInstance() = DelayBidDialog()
    }

    override fun getContentView() = R.layout.dialog_delay_bid

    override fun initView(view: View) {
        mSeconds = mList[0]

        recycler_view.run {
            mAdapter = DelayBidAdapter(mList)
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_title) {
                    mSeconds = mList[position]
                    mAdapter.setPosition(position)
                }
            }
        }

        tv_confirm.setOnClickListener {
            mOnDialogCallback?.onDialog("seconds", mSeconds)
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
