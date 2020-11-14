package com.miyin.zhenbaoqi.ui.shop.activity

import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.activity.release_goods.ReleaseGoodsActivity
import com.miyin.zhenbaoqi.ui.shop.fragment.DraftGoodsFragment
import com.miyin.zhenbaoqi.ui.shop.fragment.ManagerAuctionFragment
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_manager_auction.*
import kotlinx.android.synthetic.main.title_bar.*

class ManagerAuctionActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_manager_auction

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("拍品管理", rightTitle = "发布拍品")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("竞拍中", "已成功", "已流拍", "草稿箱")
        val fragmentList = listOf(ManagerAuctionFragment.newInstance(1), ManagerAuctionFragment.newInstance(2), ManagerAuctionFragment.newInstance(3), DraftGoodsFragment.newInstance())
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size

        tv_right_title.setOnClickListener {
            val merchantHeadImg = SPUtils.getString("merchant_head_img")
            val merchantName = SPUtils.getString("merchant_name")
            val merchantDesc = SPUtils.getString("merchant_desc")

            if (merchantHeadImg.isEmpty() or merchantName.isEmpty() or merchantDesc.isEmpty()) {
                ToastUtils.showToast("请完善店铺信息")
                startActivity<MerchantInfoActivity>()
            } else {
                startActivity<ReleaseGoodsActivity>("auction_goods" to true)
                finish()
            }
        }
    }

    override fun initData() {

    }

}
