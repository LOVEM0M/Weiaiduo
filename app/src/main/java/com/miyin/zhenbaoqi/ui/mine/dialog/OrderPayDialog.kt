package com.miyin.zhenbaoqi.ui.mine.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_order_pay.*

class OrderPayDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance() = OrderPayDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView() = R.layout.dialog_order_pay

    override fun initView(view: View) {
        setOnClickListener(tv_balance_pay, tv_ali_pay, tv_wx_pay)
    }

    override fun setGravity() = Gravity.BOTTOM

    override fun setWidth() = MATCH_PARENT

    override fun onClick(v: View?) {
        val payType = when (v?.id) {
            R.id.tv_balance_pay -> 1
            R.id.tv_ali_pay -> 2
            R.id.tv_wx_pay -> 3
            else -> 0
        }
        mOnDialogCallback?.onDialog("", payType)
        dismiss()
    }

}
