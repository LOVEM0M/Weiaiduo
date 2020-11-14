package com.miyin.zhenbaoqi.ui.shop.activity

import android.app.AlertDialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.BlackListBean
import com.miyin.zhenbaoqi.ui.shop.adapter.BlackListAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.BlackListContract
import com.miyin.zhenbaoqi.ui.shop.presenter.BlackListPresenter
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_black_list.*
import kotlinx.android.synthetic.main.layout_refresh.*

class BlackListActivity : BaseListActivity<BlackListContract.IView, BlackListContract.IPresenter>(), BlackListContract.IView {

    private var mList = mutableListOf<BlackListBean.DataBean>()
    private lateinit var mAdapter: BlackListAdapter

    override fun getContentView() = R.layout.activity_black_list

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("黑名单管理")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            mAdapter = BlackListAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_operate) {
                    AlertDialog.Builder(this@BlackListActivity)
                            .setTitle("温馨提示")
                            .setMessage("您确定要将此用户从黑名单中移除吗？")
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                mPresenter?.removeBlackList(mList[position].user_id)
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                }
            }
        }

        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getBlackList(mPage, mCount)
    }

    override fun createPresenter() = BlackListPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getBlackListSuccess(bean: BlackListBean) {
        with(bean) {
            if (current_page == 1) {
                mList = data!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(data!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableAutoLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }

        tv_count.text = SpanUtils()
                .append("已拉黑")
                .append("${mList.size}").setForegroundColor(0xFFFE2121.toInt())
                .append("人")
                .create()
    }

    override fun removeBlackListSuccess() {
        reload()
    }

}
