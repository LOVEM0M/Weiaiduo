package com.miyin.zhenbaoqi.ui.shop.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_photo_tip.*

@Deprecated("")
class PhotoTipDialog : BaseDialogFragment() {

    private var mPosition = 0
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(position: Int) = PhotoTipDialog().apply {
            arguments = Bundle().apply {
                putInt("position", position)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mPosition = getInt("position")
        }
        return R.layout.dialog_photo_tip
    }

    override fun initView(view: View) {
        setOnClickListener(tv_set_photo, tv_cancel)
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_set_photo -> {
                mOnDialogCallback?.onDialog("set_photo", mPosition)
                dismiss()
            }
            R.id.tv_cancel -> dismiss()
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
