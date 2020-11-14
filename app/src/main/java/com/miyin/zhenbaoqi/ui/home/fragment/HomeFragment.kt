package com.miyin.zhenbaoqi.ui.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.home.activity.*
import com.miyin.zhenbaoqi.ui.home.adapter.SelectFirstGoodsAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.TabAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.TopAdapter
import com.miyin.zhenbaoqi.ui.home.contract.HomeContract
import com.miyin.zhenbaoqi.ui.home.presenter.HomePresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.SortActivity
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home1.view.*
import kotlinx.android.synthetic.main.layout_refresh.*


class HomeFragment : BaseListFragment<HomeContract.IView, HomeContract.IPresenter>(), HomeContract.IView {
    private lateinit var mView: View
    private var mOnTabSelectCallback: OnTabSelectCallback? = null
    private val mBannerList = mutableListOf<HomeBannerBean.ListBean>()
    private var mIsClickBanner = false
    private lateinit var mTabAdapter: TabAdapter
    private lateinit var mTopAdapter: TopAdapter
    private lateinit var mBottomAdapter: SelectFirstGoodsAdapter
    private var mTitleList = mutableListOf<CityBean.CityListBean>()
    private var mBottomList = mutableListOf<SelectFirstGoodsBean.ListBean>()
    private var mList = mutableListOf<RestoreBean.ListBean>()
    private var mSelectIndex = 0
    private var mIsClick = false
    private var cateId1 = 0
    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun useImmersionBar() = true

    override fun getContentView() = R.layout.fragment_home1
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback

    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mView = view
        rv_tab.run {
            mTabAdapter = TabAdapter(mList)
            mTabAdapter.setHeaderAndEmpty(true)
            adapter = mTabAdapter
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            mTabAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mList[position].goods_id)
            }
        }
        recycler_view_top.run {
            mTopAdapter = TopAdapter(mTitleList)
        adapter = mTopAdapter
        val centerLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        layoutManager = centerLayoutManager
        mTopAdapter.setOnItemChildClickListener { _, view, position ->
            cateId1 = mTitleList[position].dict_id
            if (view.id == R.id.tv_title) {
                if (mSelectIndex != position) {
                    mIsClick = true
                    mSelectIndex = position
                    mTopAdapter.setPosition(position)
//                        moveToCenter(position)
//                        view_pager.currentItem = position
                }
            }
        }
    }

   recycler_view.run {
            mBottomAdapter = SelectFirstGoodsAdapter(mBottomList)
            mBottomAdapter.setHeaderAndEmpty(true)
            adapter = mBottomAdapter
            layoutManager = LinearLayoutManager(context)
            mBottomAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mBottomList[position].goods_id)
            }
        }

            setOnClickListener(ll_search_home, ll_classify,
                ll_home_tab_1, ll_home_tab_2, ll_home_tab_3, ll_home_tab_4, ll_home_tab_5,
                ll_home_tab_6, ll_home_tab_7, ll_home_tab_8, ll_home_tab_9, ll_home_tab_10,
                tv_new_vip)
    }

     override fun reload() {
        initData()
    }

    override fun initData() {
        mPresenter?.homeBanner()
        mPresenter?.getCategoryList("goods_category")
        mPresenter?.getRestoreList(1,3)
        mPresenter?.getSelectFirstGoodsList(10,mPage,mCount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun createPresenter() = HomePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_search_home -> startActivity<SearchActivity>()
            R.id.ll_home_tab_1 -> startActivity<SnacksActivity>("title" to "母婴用品")
            R.id.ll_home_tab_2 -> startActivity<SnacksActivity>("title" to "网红零食")
            R.id.ll_home_tab_3 -> startActivity<SnacksActivity>("title" to "爱衣馆")
            R.id.ll_home_tab_4 -> startActivity<SnacksActivity>("title" to "口红专区")
            R.id.ll_home_tab_5 -> startActivity<SnacksActivity>("title" to "红酒香槟")
            R.id.ll_home_tab_6 -> startActivity<SignInActivity>()
            R.id.ll_home_tab_7 -> startActivity<FriutActivity>()
            R.id.ll_home_tab_8 -> startActivity<BargainActivity>()
            R.id.ll_home_tab_9 -> startActivity<IntegralActivity>()
            R.id.ll_home_tab_10 -> startActivity<NewProWeekActivity>()
            R.id.ll_classify -> startActivity<SortActivity>()
            R.id.tv_new_vip -> startActivity<NewVipActivity>()
        }
    }


    override fun getHomeBannerSuccess(bean: HomeBannerBean) {
        bean.list?.let { it ->
            mBannerList.clear()
            mBannerList.addAll(it)
            mView.banner.setImages(it)
                    .setImageLoader(object : ImageLoader() {
                        override fun displayImage(context: Context, path: Any?, imageView: View) {
                            (imageView as ImageView).loadImg((path as HomeBannerBean.ListBean).photo)
                        }
                    })
                    .setOnBannerListener { position ->
                        if (!mIsClickBanner) {
                            mIsClickBanner = true
                            val bannerBean = it[position]
                            mPresenter?.bannerClick(bannerBean.banner_Id, position)
                        }
                    }
                    .start()
        }
    }

    override fun bannerClickSuccess(position: Int) {
        mIsClickBanner = false
        val bannerBean = mBannerList[position]
        if (!bannerBean.url.isNullOrEmpty()) {
            WebActivity.openActivity(context!!, bannerBean.title ?: "", bannerBean.url!!)
        }
    }

    override fun getSelectFirstGoodsListSuccess(bean: SelectFirstGoodsBean) {
        with(bean) {
            if (current_page == 1) {//这个page
                mBottomList = list!!.toMutableList()
                mBottomAdapter?.setNewData(mBottomList)
                smart_refresh_layout.finishRefresh()
            } else {
                mBottomAdapter?.addData(list!!)
                smart_refresh_layout.finishLoadMore()
            }
        }
    }

    override fun getRestoreListSuccess(bean: RestoreBean) {
        with(bean) {
            if (current_page == 1) {//这个page
                mList = list!!.toMutableList()
                mTabAdapter?.setNewData(mList)
            } else {
                mTabAdapter?.addData(list!!)

            }
        }
    }

    override fun getCategoryListSuccess(bean: CityBean) {
        mTitleList.clear()

        mTitleList = bean.dicts!!.toMutableList()

        val recommendBean = CityBean.CityListBean().apply {
            dict_id = 0
            code_name = "推荐"
        }
        mTitleList.add(0, recommendBean)

        mTopAdapter.setNewData(mTitleList)

    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mTabAdapter.setEmptyView(R.layout.view_empty, rv_tab)
                mBottomAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
            2 -> {
                mIsClickBanner = false
            }
        }
    }


    private fun moveToCenter(position: Int) {
        val firstVisibleItemPosition = (recycler_view_top.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val childAt = recycler_view_top.getChildAt(position - firstVisibleItemPosition)
        if (childAt != null) {
            val x = childAt.top - recycler_view_top.height / 2
            recycler_view_top.smoothScrollBy(x, 0)
            mTopAdapter.setPosition(position)
        }
    }

}
