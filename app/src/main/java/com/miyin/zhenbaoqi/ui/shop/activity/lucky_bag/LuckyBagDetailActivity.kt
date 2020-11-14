package com.miyin.zhenbaoqi.ui.shop.activity.lucky_bag

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.shop.adapter.LuckyBagDetailAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.LuckyBagDetailContract
import com.miyin.zhenbaoqi.ui.shop.presenter.LuckyBagDetailPresenter
import kotlinx.android.synthetic.main.activity_lucky_bag_detail.*

class LuckyBagDetailActivity : BaseMvpActivity<LuckyBagDetailContract.IView, LuckyBagDetailContract.IPresenter>(), LuckyBagDetailContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: LuckyBagDetailAdapter

    override fun getContentView() = R.layout.activity_lucky_bag_detail

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("福袋记录")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = LuckyBagDetailAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }

        iv_avatar.loadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4284247397,1836376440&fm=26&gp=0.jpg")
        tv_shop_name.text = "张珊烤肉店的福袋"
        tv_total_price.text = "50元"
        tv_name.text = "福袋已领完/已过去\n获得专属粉丝3个，店铺关注4个\n注：未领取金额将于24小时过期后原路退还"
        tv_price.text = "已领取2.6元/100元"
    }

    override fun initData() {

    }

    override fun createPresenter() = LuckyBagDetailPresenter()

}
