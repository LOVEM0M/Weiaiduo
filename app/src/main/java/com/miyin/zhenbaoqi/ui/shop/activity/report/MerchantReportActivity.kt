package com.miyin.zhenbaoqi.ui.shop.activity.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.FundsReportFragment
import com.miyin.zhenbaoqi.ui.shop.fragment.LiveReportFragment
import com.miyin.zhenbaoqi.ui.shop.fragment.ManageReportFragment
import kotlinx.android.synthetic.main.activity_merchant_report.*

class MerchantReportActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_merchant_report

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("店铺报表")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("资金报表", "直播报表", "经营报表")
        val fragmentList = listOf<Fragment>(FundsReportFragment.newInstance(), LiveReportFragment.newInstance(), ManageReportFragment.newInstance())
        val adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

}
