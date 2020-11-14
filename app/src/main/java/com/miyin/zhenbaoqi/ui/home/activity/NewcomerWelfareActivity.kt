package com.miyin.zhenbaoqi.ui.home.activity

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CouponGetBean
import com.miyin.zhenbaoqi.bean.WelfareCouponBean
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.NewcomerWelfareAdapter
import com.miyin.zhenbaoqi.ui.home.contract.NewcomerWelfareContract
import com.miyin.zhenbaoqi.ui.home.presenter.NewcomerWelfarePresenter
import com.miyin.zhenbaoqi.ui.sort.fragment.GoodsFragment
import kotlinx.android.synthetic.main.activity_newcomer_welfare.*

class NewcomerWelfareActivity :BaseMvpActivity<NewcomerWelfareContract.IView, NewcomerWelfareContract.IPresenter>(), NewcomerWelfareContract.IView {

    private var mList = mutableListOf<WelfareCouponBean.DataBean>()
    private lateinit var mAdapter: NewcomerWelfareAdapter

    override fun getContentView() = R.layout.activity_newcomer_welfare

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("新人福利")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            mAdapter = NewcomerWelfareAdapter(mList)
            adapter = mAdapter
            layoutManager = object : GridLayoutManager(context, 2) {
                override fun canScrollVertically() = false
            }
        }

        val titleList = listOf("新人特惠")//, "急速拍卖"
        val fragmentList = listOf(GoodsFragment.newInstance(), GoodsFragment.newInstance())
        val adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offsetVertical ->
            if (Math.abs(offsetVertical) != 0) {
                if (Math.abs(offsetVertical) == appBarLayout.totalScrollRange) {
                    view_pager.setCanScroll(true)
                } else {
                    view_pager.setCanScroll(false)
                }
//                swipe_refresh_layout.isEnabled = false
            } else if (offsetVertical == 0) {
//                swipe_refresh_layout.isEnabled = true
                view_pager.setCanScroll(false)
            }
        })

        setOnClickListener(btn_receive)
    }

    override fun initData() {
        mPresenter?.canGetCoupon()
        mPresenter?.welfareCoupon()
    }

    override fun createPresenter() = NewcomerWelfarePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_receive -> mPresenter?.receiveCoupon()
        }
    }

    override fun canGetCouponSuccess(bean: CouponGetBean) {
        btn_receive.isEnabled = (bean.data?.isCan == 0)
    }

    override fun welfareCouponSuccess(bean: WelfareCouponBean) {
        bean.data?.run {
            mList = this.toMutableList()
            mAdapter.setNewData(mList)
        }
    }

    override fun receiveCouponSuccess() {
        btn_receive.isEnabled = false
    }

}
