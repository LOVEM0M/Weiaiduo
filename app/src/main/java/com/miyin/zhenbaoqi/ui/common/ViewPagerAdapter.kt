package com.miyin.zhenbaoqi.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, private val mTitleList: List<String>, private val mFragmentList: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(i: Int) = mFragmentList[i]

    override fun getCount() = mTitleList.size

    override fun getPageTitle(position: Int) = mTitleList[position]

}
