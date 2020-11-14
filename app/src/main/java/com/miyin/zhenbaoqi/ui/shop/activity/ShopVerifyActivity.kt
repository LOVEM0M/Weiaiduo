package com.miyin.zhenbaoqi.ui.shop.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.ShopVerifyFragment
import kotlinx.android.synthetic.main.activity_shop_verify.*

class ShopVerifyActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_shop_verify

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("品质店铺核实 ")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("待核实", "已核实")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEach {
            fragmentList.add(ShopVerifyFragment.newInstance(it))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

}
