package com.miyin.zhenbaoqi.ui.shop.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.ArrayMap
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.activity.video.ReleaseVideoActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ManagerVideoAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerVideoContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ManagerVideoPresenter
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ManagerVideoFragment : BaseListFragment<ManagerVideoContract.IView, ManagerVideoContract.IPresenter>(), ManagerVideoContract.IView {

    private var mState = 0
    private var mList = mutableListOf<HomeVideoBean.DataBean>()
    private lateinit var mAdapter: ManagerVideoAdapter

    companion object {
        fun newInstance(state: Int) = ManagerVideoFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
            }
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_manager_video
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ManagerVideoAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                if (view.id == R.id.tv_delete) {
                    AlertDialog.Builder(activity)
                            .setTitle("温馨提示")
                            .setMessage("确认要删除此视频吗？")
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                mPresenter?.deleteVideo(bean.id)
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                } else if (view.id == R.id.tv_edit) {
                    startActivity<ReleaseVideoActivity>("bean" to bean)
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getVideoList(mPage, mCount, mState)
    }

    override fun createPresenter() = ManagerVideoPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getVideoListSuccess(bean: HomeVideoBean) {
        with(bean) {
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

    override fun deleteVideoSuccess() {
        reload()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(arrayMap: ArrayMap<String, Any>) {
        if (arrayMap["title"] == "upload_video" && arrayMap["state"] == 1) {
            reload()
        }
    }

}
