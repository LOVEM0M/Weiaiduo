package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.mine.fragment.OrderFragment
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : BaseActivity() {

    private var mTag = 0

    override fun getContentView(): Int {
        mTag = intent.getIntExtra("tag", 0)
        return R.layout.activity_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("我的订单", rightImage = R.drawable.ic_search)
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("全部", "待付款", "待发货", "待收货", "售后")
        val fragmentList = mutableListOf<Fragment>()

        titleList.forEachIndexed { index, _ ->
            fragmentList.add(OrderFragment.newInstance(index))
        }

        view_pager.run {
            val pagerAdapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
            adapter = pagerAdapter
            tab_layout.setViewPager(this)
            currentItem = mTag
            offscreenPageLimit = titleList.size
        }
    }

    override fun initData() {

    }

    override fun onRightClick() {
        startActivity<OrderSearchActivity>()
    }

}
