package com.miyin.zhenbaoqi.ui.home.dialog

import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.activity.NewcomerWelfareActivity
import kotlinx.android.synthetic.main.dialog_newcomer_welfare.*

class NewcomerWelfareDialog :BaseDialogFragment() {

    companion object {
        fun newInstance() = NewcomerWelfareDialog()
    }

    override fun getContentView() = R.layout.dialog_newcomer_welfare

    override fun initView(view: View) {
        iv_cover.setOnClickListener {
            startActivity<NewcomerWelfareActivity>()
            dismiss()
        }
        view_dismiss.setOnClickListener { dismiss() }
    }

    override fun setAnimation() = 0

}
