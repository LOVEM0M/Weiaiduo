package com.miyin.zhenbaoqi.ui.mine.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.HelpCenterAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.HelpCenterContract
import com.miyin.zhenbaoqi.ui.mine.presenter.HelpCenterPresenter
import kotlinx.android.synthetic.main.activity_help_center.*
import kotlinx.android.synthetic.main.layout_refresh.*

class HelpCenterActivity : BaseListActivity<HelpCenterContract.IView, HelpCenterContract.IPresenter>(), HelpCenterContract.IView {

    private var mCateId = 0
    private var mList = mutableListOf<ArticleBean.ArticleListBean>()
    private lateinit var mAdapter: HelpCenterAdapter

    override fun getContentView() = R.layout.activity_help_center

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("帮助中心", rightTitle = "平台规则")
        immersionBar { statusBarDarkFont(true) }

        tv_shopping.isSelected = true

        recycler_view.run {
            mAdapter = HelpCenterAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                WebActivity.openActivity(context, bean.arti_name!!, bean.url!!)
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }

        setOnClickListener(tv_shopping, tv_auction, tv_after_sale, tv_shop, tv_phone_customer, tv_online_customer)
    }

    override fun initData() {
        mPresenter?.getArticleList(mCateId, mPage, mCount)
    }

    override fun createPresenter() = HelpCenterPresenter()

    override fun onRightClick() {
        WebActivity.openActivity(this, "平台规则", Constant.SYSTEM_RULE)
    }

    override fun reload() {
        mPage
        initData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_shopping -> {
                if (mCateId == 0) return

                mCateId = 0
                setState()
            }
            R.id.tv_auction -> {
                if (mCateId == 1) return

                mCateId = 1
                setState()
            }
            R.id.tv_after_sale -> {
                if (mCateId == 2) return

                mCateId = 2
                setState()
            }
            R.id.tv_shop -> {
                if (mCateId == 3) return

                mCateId = 3
                setState()
            }
            R.id.tv_phone_customer -> {
                val intent = Intent(Intent.ACTION_DIAL)
                val data = Uri.parse("tel:15368664771")
                intent.data = data
                startActivity(intent)
            }
            R.id.tv_online_customer -> {
                WebActivity.openActivity(this, "官方客服", Constant.SYSTEM_CUSTOMER)
            }
        }
    }

    private fun setState() {
        tv_shopping.isSelected = mCateId == 0
        tv_auction.isSelected = mCateId == 1
        tv_after_sale.isSelected = mCateId == 2
        tv_shop.isSelected = mCateId == 3

        mList.clear()
        mPage = 1
        initData()
    }

    override fun getArticleListSuccess(bean: ArticleBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            smart_refresh_layout.setEnableLoadMore(current_page != pages)
        }
    }

}
