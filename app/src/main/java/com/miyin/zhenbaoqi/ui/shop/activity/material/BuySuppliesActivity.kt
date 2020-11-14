package com.miyin.zhenbaoqi.ui.shop.activity.material

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity

class BuySuppliesActivity : BaseActivity() {

    override fun getContentView(): Int {
        return R.layout.activity_buy_supplies
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("物料购买")
        immersionBar { statusBarDarkFont(true) }
    }

    override fun initData() {

    }

}
