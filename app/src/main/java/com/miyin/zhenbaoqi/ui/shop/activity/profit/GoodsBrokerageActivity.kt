package com.miyin.zhenbaoqi.ui.shop.activity.profit

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.GoodsBrokerageFragment
import kotlinx.android.synthetic.main.activity_goods_brokerage.*

class GoodsBrokerageActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_goods_brokerage

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("商品佣金")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("全部", "待入账", "已完成")
        val fragmentList = listOf(GoodsBrokerageFragment.newInstance(), GoodsBrokerageFragment.newInstance(), GoodsBrokerageFragment.newInstance())
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

}
