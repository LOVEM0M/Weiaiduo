package com.miyin.zhenbaoqi.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.SearchShopBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.adapter.SearchShopAdapter
import com.miyin.zhenbaoqi.ui.home.contract.SearchShopContract
import com.miyin.zhenbaoqi.ui.home.presenter.SearchShopPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class SearchShopFragment : BaseListFragment<SearchShopContract.IView, SearchShopContract.IPresenter>(), SearchShopContract.IView {

    private var mContent: String? = null
    private var mList = mutableListOf<SearchShopBean.DataBean>()
    private lateinit var mAdapter: SearchShopAdapter

    companion object {
        fun newInstance(content: String) = SearchShopFragment().apply {
            arguments = Bundle().apply {
                putString("content", content)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mContent = getString("content")
        }
        return R.layout.fragment_search_shop
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()
        recycler_view.run {
            mAdapter = SearchShopAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { _, _, position ->
                startActivity<MerchantMessageActivity>("merchant_id" to mList[position].merchants_id)
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.searchShop(mPage, 10, mContent ?: "")
    }

    override fun createPresenter() = SearchShopPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    fun researchGoods(content: String) {
        mContent = content
        reload()
    }

    override fun searchShopSuccess(bean: SearchShopBean) {
        bean.run {
            if (mPage == 1) {
                mList = data!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(data!!)
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
