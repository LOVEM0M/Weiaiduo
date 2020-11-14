package com.miyin.zhenbaoqi.ui.mine.dialog

import android.content.Context
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_delete_tip.*

class DeleteTipDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance() = DeleteTipDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView() = R.layout.dialog_delete_tip

    override fun initView(view: View) {
        tv_cancel.setOnClickListener { dismiss() }
        tv_confirm.setOnClickListener {
            mOnDialogCallback?.onDialog("delete", 0)
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun setAnimation() = 0

}
