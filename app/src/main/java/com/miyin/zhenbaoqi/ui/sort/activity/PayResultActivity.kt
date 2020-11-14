package com.miyin.zhenbaoqi.ui.sort.activity

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.sort.contract.PayResultContract
import com.miyin.zhenbaoqi.ui.sort.presenter.PayResultPresenter

class PayResultActivity : BaseMvpActivity<PayResultContract.IView, PayResultContract.IPresenter>(), PayResultContract.IView {

    override fun getContentView() = R.layout.activity_pay_result

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("在线支付", leftImage = R.drawable.ic_back_white, titleColor = R.color.white, bgColor = 0)
        immersionBar()
    }

    override fun initData() {

    }

    override fun createPresenter() = PayResultPresenter()

}
