package com.miyin.zhenbaoqi.ui.sort.fragment

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.NewGoodsBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.ui.sort.adapter.GoodsAdapter
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsContract
import com.miyin.zhenbaoqi.ui.sort.presenter.GoodsPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class GoodsFragment : BaseListFragment<GoodsContract.IView, GoodsContract.IPresenter>(), GoodsContract.IView {

    private var mList = mutableListOf<NewGoodsBean.ListBean>()
    private lateinit var mAdapter: GoodsAdapter

    companion object {
        fun newInstance() = GoodsFragment()
    }

    override fun getContentView() = R.layout.fragment_goods

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = GoodsAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                when (bean.goods_type) {
                    1 -> { // 普通商品
                        startActivity<GoodsDetailActivity>("goods_id" to bean.goods_id)
                    }
                    2, 3 -> { // 秒杀商品,竞拍商品
                        startActivity<AuctionDetailActivity>("goods_id" to bean.goods_id)
                    }
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getNewGoodsList(mPage, mCount)
    }

    override fun createPresenter() = GoodsPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getNewGoodsListSuccess(bean: NewGoodsBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.removeAllFooterView()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val isLoadMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(isLoadMore)
            if (!isLoadMore) {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

}
