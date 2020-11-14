package com.miyin.zhenbaoqi.ui.shop.activity

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantGoodsDetailContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantGoodsDetailPresenter

class MerchantGoodsDetailActivity : BaseMvpActivity<MerchantGoodsDetailContract.IView, MerchantGoodsDetailContract.IPresenter>(), MerchantGoodsDetailContract.IView {

    override fun getContentView(): Int {
        return R.layout.activity_merchant_goods_datail
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("商品详情")
        immersionBar { statusBarDarkFont(true) }
    }

    override fun initData() {

    }

    override fun createPresenter() = MerchantGoodsDetailPresenter()

}
