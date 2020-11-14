package com.miyin.zhenbaoqi.ui.shop.dialog

import android.content.Context
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_live_manager.*

class LiveManagerDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        @JvmStatic
        fun newInstance() = LiveManagerDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView() = R.layout.dialog_live_manager

    override fun initView(view: View) {
        setOnClickListener(tv_live, tv_manager_goods, tv_cancel)
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_live -> {
                mOnDialogCallback?.onDialog("live", 0)
                dismiss()
            }
            R.id.tv_manager_goods -> {
                mOnDialogCallback?.onDialog("goods", 0)
                dismiss()
            }
            R.id.tv_cancel -> dismiss()
        }
    }

}
