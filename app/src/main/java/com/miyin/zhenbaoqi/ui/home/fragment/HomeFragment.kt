package com.miyin.zhenbaoqi.ui.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.miyin.zhenbaoqi.ui.home.activity.*
import com.miyin.zhenbaoqi.ui.home.adapter.FirstCategoryGoodsAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.FirstCategorySecondAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.TabAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.TopAdapter
import com.miyin.zhenbaoqi.ui.home.contract.HomeContract
import com.miyin.zhenbaoqi.ui.home.presenter.HomePresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.SortActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home1.view.*
import kotlinx.android.synthetic.main.layout_refresh.*


class HomeFragment : BaseListFragment<HomeContract.IView, HomeContract.IPresenter>(), HomeContract.IView {
    private lateinit var mView: View
    private var mOnTabSelectCallback: OnTabSelectCallback? = null
    private val mBannerList = mutableListOf<HomeBannerBean.DataBean>()

    //    private var mIsClickBanner = false
    private lateinit var mTabAdapter: TabAdapter
    private lateinit var mTopAdapter: TopAdapter

    private lateinit var mBottomAdapter: FirstCategoryGoodsAdapter
    private var mTitleList = mutableListOf<FirstCategoryBean.DataBean>()
    private var mBottomList = mutableListOf<FirstCategoryGoodsBean.DataBeanX.DataBean>()
    private var mList = mutableListOf<VipFirstFreegoodsBean.DataBeanX.DataBean>()
    private var mSelectIndex = 0
    private var mIsClick = false
    private var cateId1 = 0
    private var names : MutableList<String> = ArrayList()
    private var cateId1List : MutableList<Int> = ArrayList()

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
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goodsId" to mList[position].goodsId)
            }
        }
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
                        mPresenter?.getFirstCategoryGoodsList(cateId1,mPage,mCount)
//                        mPresenter?.getFirstCategoryGoodsList(10,mPage,mCount)
                    }
                }
            }
        }

        recycler_view.run {
            mBottomAdapter = FirstCategoryGoodsAdapter(mBottomList)
            mBottomAdapter.setHeaderAndEmpty(true)
            adapter = mBottomAdapter
            layoutManager = LinearLayoutManager(context)
            mBottomAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goodsId" to mBottomList[position].goodsId)
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
        mPresenter?.getBannerCategory()
        mPresenter?.getFirstCategoryList()
        mPresenter?.getVipFirstFreegoodsList(1, 3)
//        mPresenter?.getFirstCategoryGoodsList(cateId1,mPage,mCount)
        mPresenter?.getFirstCategoryGoodsList(10,mPage,mCount)
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
            R.id.ll_home_tab_1 -> startActivity<SnacksActivity>("title" to names?.get(0),"cateId1" to cateId1List.get(0))
            R.id.ll_home_tab_2 -> startActivity<SnacksActivity>("title" to names?.get(1),"cateId1" to cateId1List.get(1))
            R.id.ll_home_tab_3 -> startActivity<SnacksActivity>("title" to names?.get(2),"cateId1" to cateId1List.get(2))
            R.id.ll_home_tab_4 -> startActivity<SnacksActivity>("title" to names?.get(3),"cateId1" to cateId1List.get(3))
            R.id.ll_home_tab_5 -> startActivity<SnacksActivity>("title" to names?.get(4),"cateId1" to cateId1List.get(4))
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
        bean.data?.let { it ->
            mBannerList.clear()
            mBannerList.addAll(it)
            mView.banner.setImages(it)
                    .setImageLoader(object : ImageLoader() {
                        override fun displayImage(context: Context, path: Any?, imageView: View) {
                            (imageView as ImageView).loadImg((path as HomeBannerBean.DataBean).photo)
                        }
                    })
                    .setOnBannerListener { position ->
//                        if (!mIsClickBanner) {
//                            mIsClickBanner = true
//                            val bannerBean = it[position]
////                            mPresenter?.bannerClick(bannerBean.bannerId, position)
//                        }
                    }
                    .start()
        }
    }

    override fun getBannerCategorySuccess(bean: BannerCategoryBean) {//将数据添加给首页5个banner
        with(bean) {
            for (i in 0 until data!!.size) {
                if (i == 0) {
                    GlideEngine.loadImage(mView.iv_home_tab_1, Uri.parse(data!![i].codeValue))
                    mView.tv_home_tab_1.setText(data!![i].codeName)
                } else if (i == 1) {
                    GlideEngine.loadImage(mView.iv_home_tab_2, Uri.parse(data!![i].codeValue))
                    mView.tv_home_tab_2.setText(data!![i].codeName)
                } else if (i == 2) {
                    GlideEngine.loadImage(mView.iv_home_tab_3, Uri.parse(data!![i].codeValue))
                    mView.tv_home_tab_3.setText(data!![i].codeName)
                } else if (i == 3) {
                    GlideEngine.loadImage(mView.iv_home_tab_4, Uri.parse(data!![i].codeValue))
                    mView.tv_home_tab_4.setText(data!![i].codeName)
                } else if (i == 4) {
                    GlideEngine.loadImage(mView.iv_home_tab_5, Uri.parse(data!![i].codeValue))
                    mView.tv_home_tab_5.setText(data!![i].codeName)
                }
                names!!.add(data!![i].codeName!!)
                cateId1List!!.add(data!![i].dictId!!)
            }
        }
    }

//    override fun bannerClickSuccess(position: Int) {
//        mIsClickBanner = false
//        val bannerBean = mBannerList[position]
//        if (!bannerBean.url.isNullOrEmpty()) {
//            WebActivity.openActivity(context!!, bannerBean.title ?: "", bannerBean.url!!)
//        }
//    }


    override fun getVipFirstFreegoodsListSuccess(bean: VipFirstFreegoodsBean) {
        with(bean.data) {
            if (this!!.total == 1) {//这个page
                mList = data!!.toMutableList()
                mTabAdapter?.setNewData(mList)
            } else {
                mTabAdapter?.addData(data!!)
            }
        }
    }

    override fun getFirstCategoryListSuccess(bean: FirstCategoryBean) {
        mTitleList.clear()

        mTitleList = bean.data!!.toMutableList()

        val recommendBean = FirstCategoryBean.DataBean().apply {
            dictId = 0
            codeName = "推荐"
        }
        mTitleList.add(0, recommendBean)

        mTopAdapter.setNewData(mTitleList)

    }

    override fun getFirstCategoryGoodsListSuccess(bean: FirstCategoryGoodsBean) {
        with(bean) {
            if(data?.data!!.size==0){
                mBottomAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
            else {
                if (current_page == 1) {
                    mBottomList = data!!.data!!.toMutableList()
                    mBottomAdapter?.setNewData(mBottomList)
                    smart_refresh_layout.finishRefresh()
                } else {
                    mBottomList.clear()
                    mBottomAdapter?.addData(data!!.data!!)
                    mBottomAdapter.notifyDataSetChanged()
                    smart_refresh_layout.finishLoadMore()
                }
            }
        }
    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mTabAdapter.setEmptyView(R.layout.view_empty, rv_tab)
            }
//            2 -> {
//                mIsClickBanner = false
//            }
        }
    }


    private fun moveToCenter(position: Int) {
//        val firstVisibleItemPosition = (recycler_view_top.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//
//        val childAt = recycler_view_top.getChildAt(position - firstVisibleItemPosition)
//        if (childAt != null) {
//            val x = childAt.top - recycler_view_top.height / 2
//            recycler_view_top.smoothScrollBy(x, 0)
//            mTopAdapter.setPosition(position)
//        }
    }

}
