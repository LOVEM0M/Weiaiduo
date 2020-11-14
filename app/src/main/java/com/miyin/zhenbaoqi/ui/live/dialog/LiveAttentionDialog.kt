package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Bundle
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import kotlinx.android.synthetic.main.dialog_live_attention.*

class LiveAttentionDialog : BaseDialogFragment() {

    private var mHeadImg: String? = null
    private var mMerchantName: String? = null
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(headImg: String, merchantName: String) = LiveAttentionDialog().apply {
            arguments = Bundle().apply {
                putString("head_img", headImg)
                putString("merchant_name", merchantName)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mHeadImg = getString("head_img")
            mMerchantName = getString("merchant_name")
        }
        return R.layout.dialog_live_attention
    }

    override fun initView(view: View) {
        iv_avatar.loadImg(mHeadImg)
        tv_title.text = mMerchantName

        tv_attention.setOnClickListener {
            mOnDialogCallback?.onDialog("attention", 0)
        }
    }

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
