package com.miyin.zhenbaoqi.ui.shop.activity.business

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.shop.adapter.BusinessAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessContract
import com.miyin.zhenbaoqi.ui.shop.presenter.BusinessPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class BusinessActivity : BaseListActivity<BusinessContract.IView, BusinessContract.IPresenter>(), BusinessContract.IView {

    private var mList = mutableListOf<ArticleBean.ArticleListBean>()
    private lateinit var mAdapter: BusinessAdapter

    override fun getContentView() = R.layout.activity_business

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("商家活动")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            mAdapter = BusinessAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<BusinessDetailActivity>(Constant.INTENT_REQUEST_CODE, "bean" to mList[position])
            }
        }

        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getArticleActivity(mPage, mCount)
    }

    override fun createPresenter() = BusinessPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun getArticleActivitySuccess(bean: ArticleBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }

            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

}
