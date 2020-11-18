package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.FirstCategoryBean
import com.miyin.zhenbaoqi.bean.WeekNewGoodsBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.home.adapter.TopAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.WeekNewGoodsAdapter
import com.miyin.zhenbaoqi.ui.home.contract.NewProWeekContract
import com.miyin.zhenbaoqi.ui.home.presenter.NewProWeekPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.activity_new_products_week.*
import kotlinx.android.synthetic.main.activity_new_products_week.recycler_view_top
import kotlinx.android.synthetic.main.layout_refresh.*


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class NewProWeekActivity : BaseListActivity<NewProWeekContract.IView, NewProWeekContract.IPresenter>(), NewProWeekContract.IView {
    private var mName: String? = null
    private var mList = mutableListOf<WeekNewGoodsBean.ListBean>()
    private lateinit var mAdapter: WeekNewGoodsAdapter
    private lateinit var mTopAdapter: TopAdapter
    private var mTitleList = mutableListOf<FirstCategoryBean.DataBean>()
    private var mSelectIndex = 0
    private var mIsClick = false
    private var cateId1 = 0
    companion object {
        fun newInstance() = NewProWeekActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        recycler_view_top.run {
            mTopAdapter = TopAdapter(mTitleList)
            adapter = mTopAdapter
            val centerLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            layoutManager = centerLayoutManager
            mTopAdapter.setOnItemChildClickListener { _, view, position ->
                cateId1 = mTitleList[position].dictId
                if (view.id == R.id.tv_title) {
                    if (mSelectIndex != position) {
                        mIsClick = true
                        mSelectIndex = position
                        mTopAdapter.setPosition(position)
                    }
                }
            }
        }
        recycler_view.run {
            mAdapter = WeekNewGoodsAdapter(mList)
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
    override fun reload() {
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }
    override fun getContentView(): Int {
        return R.layout.activity_new_products_week
    }

    override fun initData() {
        mPresenter?.getCategoryList()
        mPresenter?.getWeekNewGoodsList(mPage, mCount)
    }

    override fun createPresenter() = NewProWeekPresenter()

    override fun getCategoryListSuccess(bean: FirstCategoryBean) {
        mTitleList.clear()

        mTitleList = bean.data!!.toMutableList()

        val recommendBean = FirstCategoryBean.DataBean().apply {
            dictId = 0
            codeName = "全部"
        }
        mTitleList.add(0, recommendBean)

        mTopAdapter.setNewData(mTitleList)

    }

    override fun getWeekNewGoodsListSuccess(bean: WeekNewGoodsBean) {
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