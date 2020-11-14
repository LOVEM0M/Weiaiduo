package com.miyin.zhenbaoqi.ui.home.fragment

import android.os.Bundle
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.activity.VideoActivity
import com.miyin.zhenbaoqi.ui.home.adapter.VideoAdapter
import com.miyin.zhenbaoqi.ui.home.contract.VideoListContract
import com.miyin.zhenbaoqi.ui.home.presenter.VideoListPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class VideoFragment : BaseListFragment<VideoListContract.IView, VideoListContract.IPresenter>(), VideoListContract.IView {

    private var mList = mutableListOf<HomeVideoBean.DataBean>()
    private lateinit var mAdapter: VideoAdapter

    companion object {
        fun newInstance() = VideoFragment()
    }

    override fun getContentView() = R.layout.fragment_video

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = VideoAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
            mAdapter.setOnItemClickListener { _, _, position ->
                startActivity<VideoActivity>("bean" to mList[position])
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getVideoList(mPage, mCount)
    }

    override fun createPresenter() = VideoListPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getVideoListSuccess(bean: HomeVideoBean) {
        bean.run {
            if (current_page == 1) {
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
