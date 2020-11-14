package com.miyin.zhenbaoqi.ui.mine.activity

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.mine.fragment.CollectFragment
import com.miyin.zhenbaoqi.ui.mine.fragment.ShopFragment
import kotlinx.android.synthetic.main.activity_collect.*

class CollectActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_collect

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("关注/收藏")
        immersionBar { statusBarDarkFont(true) }
        val titleList = listOf("收藏的商品", "关注的店铺")
        val fragmentList = listOf(CollectFragment.newInstance(), ShopFragment.newInstance())
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

}
