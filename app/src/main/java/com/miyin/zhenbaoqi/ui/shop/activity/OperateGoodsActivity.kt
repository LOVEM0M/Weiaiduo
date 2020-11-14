package com.miyin.zhenbaoqi.ui.shop.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.view.ViewGroup
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.OperateGoodsFragment
import kotlinx.android.synthetic.main.activity_operate_goods.*

class OperateGoodsActivity : BaseActivity() {

    private var mTitle = "创建商品"
    private var mRoomId = 0
    private var mIsLive = true
    private var mBean: LiveGoodsBean.LiveGoodsDataBean? = null
    private val mFragmentList = arrayListOf<Fragment>()

    override fun getContentView(): Int {
        with(intent) {
            mTitle = getStringExtra("title") ?: ""
            mRoomId = getIntExtra("room_id", 0)
            mIsLive = getBooleanExtra("is_live", true)
            mBean = getSerializableExtra("bean") as LiveGoodsBean.LiveGoodsDataBean?
        }
        return R.layout.activity_operate_goods
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar(mTitle)
        immersionBar {
            statusBarDarkFont(true)
            keyboardEnable(true)
        }

        val titleList = listOf("秒杀", "拍卖")
        mFragmentList.clear()
        titleList.forEach {
            mFragmentList.add(OperateGoodsFragment.newInstance(it, mRoomId, mBean, mIsLive))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, mFragmentList)
        tab_layout.setViewPager(view_pager)

        if (TextUtils.equals("编辑商品", mTitle)) {
            val viewGroup = tab_layout.getChildAt(0) as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                viewGroup.getChildAt(i).isClickable = false
            }
            view_pager.setCanScroll(false)
        } else {
            view_pager.setCanScroll(true)
        }
    }

    override fun initData() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                val currentItem = view_pager.currentItem
                mFragmentList[currentItem].onActivityResult(requestCode, resultCode, data)
//                OperateGoodsFragment.newInstance("tag").onActivityResult(requestCode,resultCode,data)
            }
        }
    }

}
