package com.miyin.zhenbaoqi.ui.sort.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.base.fragment.BaseStateFragment
import com.miyin.zhenbaoqi.bean.CommodityBean
import com.miyin.zhenbaoqi.ui.sort.adapter.RightAdapter
import com.miyin.zhenbaoqi.ui.sort.adapter.LeftAdapter
import com.miyin.zhenbaoqi.ui.sort.contract.SortContract
import com.miyin.zhenbaoqi.ui.sort.presenter.SortPresenter
import com.miyin.zhenbaoqi.utils.SmoothLayoutManager
import kotlinx.android.synthetic.main.fragment_sort.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.activity.SearchActivity
import com.miyin.zhenbaoqi.ui.sort.activity.CategoryActivity
import com.miyin.zhenbaoqi.ui.sort.dialog.CateListShareDialog
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.net.URLEncoder

class SortFragment : BaseStateFragment<SortContract.IView, SortContract.IPresenter>(), SortContract.IView, OnDialogCallback {

    private val mFragmentList = mutableListOf<Fragment>()
    private var mTitleList = mutableListOf<CityBean.DataBean>()
    private lateinit var mLeftAdapter: LeftAdapter
    private val mCommodityList = mutableListOf<CommodityBean>()
    private lateinit var mRightAdapter: RightAdapter
    private var mSelectIndex = 0
    private var mIsClick = false
    private var mRecursiveIndex = 0
    private lateinit var mWXAPI: IWXAPI

    companion object {
        fun newInstance() = SortFragment()
    }

    override fun useImmersionBar() = true

    override fun getContentView() = R.layout.fragment_sort

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mImmersionBar.titleBar(title_bar).statusBarDarkFont(true).init()
        mWXAPI = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID, true)
        showLoading()

        rv_left.run {
            mLeftAdapter = LeftAdapter(mTitleList)
            adapter = mLeftAdapter
            val centerLayoutManager = LinearLayoutManager(context)
            layoutManager = centerLayoutManager
            mLeftAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_title) {
                    if (mSelectIndex != position) {
                        mIsClick = true
                        mSelectIndex = position
                        mLeftAdapter.setPosition(position)
                        //centerLayoutManager.smoothScrollToPosition(rv_left, RecyclerView.State(), position)
                        moveToCenter(position)

                        view_pager.currentItem = position

//                        val index = mCommodityList.indexOfFirst { it.itemType == 0 && it.index == position }
//                        rv_right.smoothScrollToPosition(index)
                    }
                }
            }
        }

        rv_right.run {
            mRightAdapter = RightAdapter(mCommodityList)
            adapter = mRightAdapter
            val gridLayoutManager = SmoothLayoutManager(context, 3)
            layoutManager = gridLayoutManager
            mRightAdapter.setOnItemClickListener { _, _, position ->
                var cateId1 = mTitleList[mCommodityList[position].index].dictId
                if (cateId1 == 0) {
                    cateId1 = mCommodityList[position].parentId
                }
                val cateId2 = mCommodityList[position].dictId
                val cate2Name = mCommodityList[position].title
                val cateCover = mCommodityList[position].cover
                startActivity<CategoryActivity>("cate_id1" to cateId1, "cate_id2" to cateId2, "cate2_name" to cate2Name,
                        "cate_cover" to cateCover)
            }
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (mCommodityList[position].itemType == 0) 3 else 1
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (mIsClick && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mIsClick = false
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!mIsClick) {
                        if (recyclerView.layoutManager is GridLayoutManager) {
                            val layoutManager = recyclerView.layoutManager as GridLayoutManager
                            val position = layoutManager.findFirstVisibleItemPosition()

                            val index = mCommodityList[position].index
                            if (mSelectIndex != index) {
                                mSelectIndex = index
                                moveToCenter(mSelectIndex)
                            }
                        }
                    }
                }
            })
        }

        setOnClickListener(tv_share, tv_share_bottom, tv_search)
    }

    override fun initData() {
        mPresenter?.getCategoryList("goods_category")
    }

    override fun createPresenter() = SortPresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_share, R.id.tv_share_bottom -> {
                val dialog = CateListShareDialog.newInstance()
                dialog.show(fragmentManager!!, "cateListShare")
                dialog.setOnDialogCallback(this)
            }
            R.id.tv_search -> startActivity<SearchActivity>()
        }
    }

    private fun moveToCenter(position: Int) {
        val firstVisibleItemPosition = (rv_left.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val childAt = rv_left.getChildAt(position - firstVisibleItemPosition)
        if (childAt != null) {
            val y = childAt.top - rv_left.height / 2
            rv_left.smoothScrollBy(0, y)
            mLeftAdapter.setPosition(position)
        }
    }

    override fun getCategoryListSuccess(bean: CityBean) {
        mTitleList.clear()
        mCommodityList.clear()
        mFragmentList.clear()

        mTitleList = bean.data!!.toMutableList()

        val recommendBean = CityBean.DataBean().apply {
            dictId = 0
            codeName = "推荐类目"
        }
        mTitleList.add(0, recommendBean)

        mLeftAdapter.setNewData(mTitleList)

        mTitleList.forEach {
            mFragmentList.add(ChildSortFragment.newInstance(it))
        }
        view_pager.run {
            val adapter = MyAdapter(this@SortFragment, mFragmentList)
            setAdapter(adapter)
            offscreenPageLimit = mTitleList.size
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    moveToCenter(position)
                }
            })
        }

        // mPresenter?.getRecommend()
    }

    override fun getRecommendSuccess(bean: CityBean) {
        mCommodityList.add(CommodityBean(0, mTitleList[mRecursiveIndex].codeName!!, index = mRecursiveIndex, dictId = mTitleList[mRecursiveIndex].dictId))
        bean.data?.forEach {
            mCommodityList.add(CommodityBean(1, it.codeName!!, it.codeValue, it.dictId, mRecursiveIndex, it.parentId as Int))
        }

        mRecursiveIndex++
        mPresenter?.getSecondLevel(mTitleList[mRecursiveIndex].dictId)
    }

    override fun getSecondLevelSuccess(bean: CityBean) {
        mCommodityList.add(CommodityBean(0, mTitleList[mRecursiveIndex].codeName!!, index = mRecursiveIndex, dictId = mTitleList[mRecursiveIndex].dictId))
        bean.data?.forEach {
            mCommodityList.add(CommodityBean(1, it.codeName!!, it.codeValue, it.dictId, mRecursiveIndex))
        }

        if (mRecursiveIndex == mTitleList.size - 1) {
            mRightAdapter.setNewData(mCommodityList)
            showNormal()
        } else {
            mRecursiveIndex++
            mPresenter?.getSecondLevel(mTitleList[mRecursiveIndex].dictId)
        }
    }

    override fun onFailure() {
        mCommodityList.add(CommodityBean(0, mTitleList[mRecursiveIndex].codeName!!, index = mRecursiveIndex, dictId = mTitleList[mRecursiveIndex].dictId))

        mRecursiveIndex++
        mPresenter?.getSecondLevel(mTitleList[mRecursiveIndex].dictId)
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cate_list_share_bg)
                WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", "/pages/list/list?inviteCode=${SPUtils.getInt("user_id")}", "【唯爱多】向你推荐", "唯爱严选，匠心好物", bitmap)
            } else if (obj == "shareFriendCircle") {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cate_list_share_bg)
                val headImg = SPUtils.getString("head_img")
                val nickName = SPUtils.getString("nick_name")
                val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
                WXOptionUtils.share(mWXAPI, shareUrl, "【唯爱多】向你推荐", "唯爱严选，匠心好物", bitmap, true)
            }
        }
    }

    internal class MyAdapter(fragment: Fragment, private val list: List<Fragment>) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = list[position]

        override fun getItemCount() = list.size
    }

}
