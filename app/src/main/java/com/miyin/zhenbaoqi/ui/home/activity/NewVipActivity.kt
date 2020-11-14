package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.RestoreBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.home.adapter.NewVipAdapter
import com.miyin.zhenbaoqi.ui.home.contract.NewVipContract
import com.miyin.zhenbaoqi.ui.home.presenter.NewVipPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.activity_new_vip.*
import kotlinx.android.synthetic.main.layout_refresh.*


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class NewVipActivity : BaseListActivity<NewVipContract.IView, NewVipContract.IPresenter>(), NewVipContract.IView {
    private var mList = mutableListOf<RestoreBean.ListBean>()
    private lateinit var mAdapter: NewVipAdapter

    companion object {
        fun newInstance() = NewVipActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        recycler_view.run {
            mAdapter = NewVipAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mList[position].goods_id)
            }
        }
        setOnClickListener(ll_goback)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_goback -> finish()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_new_vip
    }
    override fun reload() {
        initData()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun initData() {
        mPresenter?.getRestoreList(mPage, mCount)
    }

    override fun createPresenter() = NewVipPresenter()

    override fun getRestoreListSuccess(bean: RestoreBean) {
        with(bean) {
            if (current_page == 1) {//这个page
                mList = list!!.toMutableList()
                mAdapter?.setNewData(mList)

            } else {
                mAdapter?.addData(list!!)
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

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
        }
    }

}