package com.miyin.zhenbaoqi.ui.shop.activity.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.ShopOrderFragment
import kotlinx.android.synthetic.main.activity_shop_order.*

class ShopOrderActivity : BaseActivity() {

    private var mTag = 0

    override fun getContentView(): Int {
        mTag = intent.getIntExtra("tag", 0)
        return R.layout.activity_shop_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("我的订单", rightImage = R.drawable.ic_search)
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("待付款", "待发货", "待收货", "已完成", "售后", "全部")
        val stateList = listOf(1, 2, 3, 5, 6, 0)
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(ShopOrderFragment.newInstance(stateList[index]))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.currentItem = mTag
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun onRightClick() {
        startActivity<ShopOrderSearchActivity>()
        // startActivity<BulkShipActivity>()
    }

    override fun initData() {

    }

}
