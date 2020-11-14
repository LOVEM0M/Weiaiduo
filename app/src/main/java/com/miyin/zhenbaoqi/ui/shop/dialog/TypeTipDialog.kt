package com.miyin.zhenbaoqi.ui.shop.dialog

import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.dialog_type_tip.*

class TypeTipDialog : BaseDialogFragment() {

    companion object {
        fun newInstance() = TypeTipDialog()
    }

    override fun getContentView() = R.layout.dialog_type_tip

    override fun initView(view: View) {
        isCancelable = false

        btn_commit.setOnClickListener {
            SPUtils.putBoolean("is_show_type_tip", true)
            dismiss()
        }
    }

    override fun setAnimation() = 0

}
