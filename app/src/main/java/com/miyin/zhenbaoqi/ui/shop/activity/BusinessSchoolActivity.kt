package com.miyin.zhenbaoqi.ui.shop.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.BusinessSchoolFragment
import kotlinx.android.synthetic.main.activity_business_school.*

class BusinessSchoolActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_business_school

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("${getString(R.string.app_name)}商学院")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("新手商家", "商家成长")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(BusinessSchoolFragment.newInstance(index + 6))
        }

        val adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)
    }

    override fun initData() {

    }


}
