package com.miyin.zhenbaoqi.ui.shop.dialog

import android.content.Context
import android.text.Editable
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_refuse.*

class RefuseDialog : BaseDialogFragment() {

    private var mContent: String? = null
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance() = RefuseDialog()
    }

    override fun getContentView() = R.layout.dialog_refuse

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun initView(view: View) {
        et_content.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }
            }
        })

        tv_confirm.setOnClickListener {
            if (mContent.isNullOrEmpty()) {
                ToastUtils.showToast("请填写拒绝原因")
                return@setOnClickListener
            }
            mOnDialogCallback?.onDialog(mContent!!, 0)
            dismiss()
        }
        tv_cancel.setOnClickListener { dismiss() }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}