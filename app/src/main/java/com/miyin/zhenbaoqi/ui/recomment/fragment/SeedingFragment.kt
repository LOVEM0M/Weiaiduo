package com.miyin.zhenbaoqi.ui.recomment.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.recomment.adapter.SeedingAdapter
import com.miyin.zhenbaoqi.bean.SeedingBean
import com.miyin.zhenbaoqi.ui.recomment.contract.SeedingContract
import com.miyin.zhenbaoqi.ui.recomment.presenter.SeedingPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class SeedingFragment : BaseListFragment<SeedingContract.IView, SeedingContract.IPresenter>(), SeedingContract.IView {
    private var mOnTabSelectCallback: OnTabSelectCallback? = null
    private var mList = mutableListOf<SeedingBean.ListBean>()
    private  lateinit var mAdapter: SeedingAdapter

    companion object {
        fun newInstance() = SeedingFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback

    }

    override fun useImmersionBar() = true

    override fun getContentView() = R.layout.fragment_seeding

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
//        showLoading()
        recycler_view.run {
            mAdapter = SeedingAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mAdapter?.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mList[position].goods?.goods_id)
            }
        }
    }

    override fun initData() {
        mPresenter?.getSeedingGoodsList(mPage, "",mCount)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }
   override fun reload() {
//        showLoading()
        mPage = 1
        initData()
    }

    override fun createPresenter() = SeedingPresenter()
    override fun getSeedingGoodsListSuccess(bean: SeedingBean) {//调用种草页面接口
        with(bean) {
            if (current_page == 1) {//这个page
                mList = list!!.toMutableList()
                mAdapter?.setNewData(mList)
                smart_refresh_layout.finishRefresh()
            } else {
                mAdapter?.addData(list!!)
                smart_refresh_layout.finishLoadMore()

            }

//                val hasMore = current_page != pages
//                smart_refresh_layout.setEnableLoadMore(true)
//            if (hasMore) {
//                mAdapter?.removeAllFooterView()
//            } else {
//                mAdapter?.addNoMoreDataFooter()
//            }
        }
    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mAdapter?.setEmptyView(R.layout.view_empty, recycler_view)
            }
        }
    }

}







