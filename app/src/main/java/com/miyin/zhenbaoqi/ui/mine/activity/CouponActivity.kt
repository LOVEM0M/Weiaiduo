package com.miyin.zhenbaoqi.ui.mine.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.mine.fragment.CouponFragment
import kotlinx.android.synthetic.main.activity_coupon.*

class CouponActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_coupon

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("优惠券")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("可使用", "已使用", "已过期")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(CouponFragment.newInstance(index))
        }

        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

}
