package com.miyin.zhenbaoqi.ui.sort.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.R.id.iv_wx_pay
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.dialog_pay.*

class PayDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mPrice: String? = null
    private var mType: String? = null
    private var mSelectIndex = 3

    companion object {
        fun newInstance(price: String, type: String = "common") = PayDialog().apply {
            val bundle = Bundle()
            bundle.putString("price", price)
            bundle.putString("type", type)
            arguments = bundle
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mPrice = getString("price")
            mType = getString("type")
        }
        return R.layout.dialog_pay
    }

    override fun initView(view: View) {
        tv_price.text = SpanUtils()
                .append(mPrice!!).setFontSize(30, true).setForegroundColor(0xFF3A3A3A.toInt())
                .append(" 元").setFontSize(14, true).setForegroundColor(0xFF727272.toInt())
                .create()

        if (mType == "auction") {
            tv_title.text = "出价保证金"
            tv_tip.visibility = View.VISIBLE
        } else {
            tv_title.text = "确认支付"
            tv_tip.visibility = View.GONE
        }

        iv_wx_pay.isSelected = true
        iv_ali_pay.isSelected = false

        setOnClickListener(fl_wx_pay, iv_wx_pay, fl_ali_pay, iv_ali_pay, tv_confirm)
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_wx_pay, R.id.iv_wx_pay -> {
                iv_wx_pay.isSelected = true
                iv_ali_pay.isSelected = false
                mSelectIndex = 3
            }
            R.id.fl_ali_pay, R.id.iv_ali_pay -> {
                iv_wx_pay.isSelected = false
                iv_ali_pay.isSelected = true
                mSelectIndex = 2
            }
            R.id.tv_confirm -> {
                mOnDialogCallback?.onDialog("pay", mSelectIndex)
                dismiss()
            }
        }
    }

}
