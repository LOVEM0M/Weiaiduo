package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.CollegeBean
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.BusinessSchoolAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessSchoolContract
import com.miyin.zhenbaoqi.ui.shop.presenter.BusinessSchoolPresenter
import kotlinx.android.synthetic.main.layout_refresh.*

class BusinessSchoolFragment : BaseListFragment<BusinessSchoolContract.IView, BusinessSchoolContract.IPresenter>(), BusinessSchoolContract.IView {

    private var mState = 0
    private var mList = mutableListOf<CollegeBean.ListBean>()
    private lateinit var mAdapter: BusinessSchoolAdapter

    companion object {
        fun newInstance(state: Int) = BusinessSchoolFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_business_school
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = BusinessSchoolAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                WebActivity.openActivity(context, bean.arti_name ?: "", bean.url ?: "")
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getArticleCollegeList(mState, mPage, mCount)
    }

    override fun createPresenter() = BusinessSchoolPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getArticleCollegeListSuccess(bean: CollegeBean) {
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
