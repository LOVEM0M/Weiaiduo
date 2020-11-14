package com.miyin.zhenbaoqi.ui.message.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.dialog_customer_operate.*

class CustomerOperateDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mType = 0

    companion object {
        fun newInstance(type: Int) = CustomerOperateDialog().apply {
            arguments = Bundle().apply {
                putInt("type", type)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mType = getInt("type")
        }
        return R.layout.dialog_customer_operate
    }

    override fun initView(view: View) {
        val merchantId = SPUtils.getInt("merchant_id")
        if (merchantId == 0) {
            tv_report.visibility = View.VISIBLE
            tv_add_black_list.visibility = View.GONE
        } else {
            if (mType == 1) {
                tv_report.visibility = View.GONE
                tv_add_black_list.visibility = View.VISIBLE
            } else {
                tv_report.visibility = View.VISIBLE
                tv_add_black_list.visibility = View.VISIBLE
                view_empty.visibility = View.GONE
            }
        }

        setOnClickListener(tv_report, tv_add_black_list)
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_report -> {
                mOnDialogCallback?.onDialog("report", 0)
                dismiss()
            }
            R.id.tv_add_black_list -> {
                mOnDialogCallback?.onDialog("addBlackList", 1)
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
