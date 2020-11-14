package com.miyin.zhenbaoqi.ui.sort.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_add_price.*

class AddPriceDialog : BaseDialogFragment() {

    private var mPrice: String? = null
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(price: String) = AddPriceDialog().apply {
            arguments = Bundle().apply {
                putString("price", price)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        mPrice = arguments?.getString("price")
        return R.layout.dialog_add_price
    }

    override fun initView(view: View) {
        tv_price.text = mPrice

        setOnClickListener(iv_close, btn_commit)
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onDetach() {
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
        super.onDetach()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> dismiss()
            R.id.btn_commit -> {
                mOnDialogCallback?.onDialog("add_price", 0)
                dismiss()
            }
        }
    }

}
