package com.miyin.zhenbaoqi.ui.shop.activity.lucky_bag

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.contract.LuckyBagContract
import com.miyin.zhenbaoqi.ui.shop.presenter.LuckyBagPresenter

class LuckyBagActivity : BaseMvpActivity<LuckyBagContract.IView, LuckyBagContract.IPresenter>(), LuckyBagContract.IView {

    override fun getContentView() = R.layout.activity_lucky_bag

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("福袋", rightTitle = "记录")
        immersionBar { statusBarDarkFont(true) }
    }

    override fun initData() {

    }

    override fun onRightClick() {
        startActivity<LuckyBagRecordActivity>()
    }

    override fun createPresenter() = LuckyBagPresenter()

}
