package com.miyin.zhenbaoqi.ui.mine.fragment

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import com.gyf.immersionbar.ImmersionBar

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.CollectBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.CollectAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.CollectContract
import com.miyin.zhenbaoqi.ui.mine.presenter.CollectPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.activity_recharge.*
import kotlinx.android.synthetic.main.activity_upload_photo.*
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.layout_refresh.*
import kotlinx.android.synthetic.main.layout_refresh.recycler_view

class CollectFragment : BaseListFragment<CollectContract.IView, CollectContract.IPresenter>(), CollectContract.IView {

    private var mList = mutableListOf<CollectBean.CollectListBean>()
    private lateinit var mAdapter: CollectAdapter

    companion object {
        fun newInstance() = CollectFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
          visible(tv_title_bar)
        recycler_view.run {
            mAdapter = CollectAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                startActivity<GoodsDetailActivity>("goods_id" to bean.goods_id)
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun getContentView() = R.layout.fragment_collect

    override fun initData() {
        mPresenter?.getCollectList(mPage, mCount)
    }

    override fun createPresenter() = CollectPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getCollectListSuccess(bean: CollectBean) {
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
