package com.miyin.zhenbaoqi.ui.shop.activity.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.ManagerVideoFragment
import kotlinx.android.synthetic.main.activity_manager_video.*

class ManagerVideoActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_manager_video

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("视频管理", rightTitle = "发布")
        immersionBar { statusBarDarkFont(true) }

        val stateList = listOf(1, 2, 0)
        val titleList = listOf("待审核", "已上架", "草稿箱")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(ManagerVideoFragment.newInstance(stateList[index]))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size
    }

    override fun initData() {

    }

    override fun onRightClick() {
        startActivity<ReleaseVideoActivity>()
    }

}
