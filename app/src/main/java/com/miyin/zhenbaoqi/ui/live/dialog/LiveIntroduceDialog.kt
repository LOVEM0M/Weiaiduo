package com.miyin.zhenbaoqi.ui.live.dialog

import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment

class LiveIntroduceDialog : BaseDialogFragment() {

    companion object {
        fun newInstance() = LiveIntroduceDialog()
    }

    override fun getContentView(): Int {
        return R.layout.dialog_live_introduce
    }

    override fun initView(view: View) {

    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
