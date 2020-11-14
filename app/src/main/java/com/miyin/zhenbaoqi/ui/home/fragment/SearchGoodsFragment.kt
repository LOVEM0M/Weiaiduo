package com.miyin.zhenbaoqi.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.adapter.GoodsSearchAdapter
import com.miyin.zhenbaoqi.ui.home.contract.SearchGoodsContract
import com.miyin.zhenbaoqi.ui.home.presenter.SearchGoodsPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class SearchGoodsFragment : BaseListFragment<SearchGoodsContract.IView, SearchGoodsContract.IPresenter>(), SearchGoodsContract.IView {

    private var mContent: String? = null
    private var mType = 0
    private var mList = mutableListOf<GoodsSearchBean.ListBean>()
    private lateinit var mAdapter: GoodsSearchAdapter

    companion object {
        fun newInstance(content: String, type: Int) = SearchGoodsFragment().apply {
            arguments = Bundle().apply {
                putString("content", content)
                putInt("type", type)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mContent = getString("content")
            mType = getInt("type")
        }
        return R.layout.fragment_search_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = GoodsSearchAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                if (mType == 0) {
                    startActivity<GoodsDetailActivity>("goods_id" to mList[position].goods_id)
                } else {
                    startActivity<AuctionDetailActivity>("goods_id" to mList[position].goods_id)
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.searchGoods(mPage, 10, mContent!!, mType)
    }

    override fun createPresenter() = SearchGoodsPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    fun researchGoods(content: String) {
        mContent = content
        reload()
    }

    override fun searchGoodsSuccess(bean: GoodsSearchBean) {
        bean.run {
            if (mPage == 1) {
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
