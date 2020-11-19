package com.miyin.zhenbaoqi.ui.sort.activity

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.SearchGoodsBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.sort.adapter.CategoryAdapter
import com.miyin.zhenbaoqi.ui.sort.contract.CategoryContract
import com.miyin.zhenbaoqi.ui.sort.dialog.CategoryShareDialog
import com.miyin.zhenbaoqi.ui.sort.presenter.CategoryPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils.hideKeyboard
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.noober.background.drawable.DrawableCreator
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.layout_refresh.*
import java.net.URLEncoder

class CategoryActivity : BaseListActivity<CategoryContract.IView, CategoryContract.IPresenter>(), CategoryContract.IView, OnDialogCallback {

    private var mSearchParam: String? = null
    private var mCateId1 = 0
    private var mCateId2 = 0
    private var mCateId3 = 0
    private var mState = 3
    private var mMinAmount = 0L
    private var mMaxAmount = 0L
    private var mCate2Name: String? = null
    private var mCateCover: String? = null

    private var mList = mutableListOf<SearchGoodsBean.ListBean>()
    private lateinit var mAdapter: CategoryAdapter
    private var mSelectIndex = 0
    private lateinit var mWXAPI: IWXAPI

    override fun getContentView(): Int {
        with(intent) {
            mCateId1 = getIntExtra("cate_id1", 0)
            mCateId2 = getIntExtra("cate_id2", 0)
            mCate2Name = getStringExtra("cate2_name")
            mCateCover = getStringExtra("cate_cover")
        }
        return R.layout.activity_category
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        showLoading()

        tv_price.isSelected = true

        recycler_view.run {
            mAdapter = CategoryAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, view, position ->
                val goodsId = mList[position].goods_id
                if (mState == 3) {
                    val intent = Intent(this@CategoryActivity, GoodsDetailActivity::class.java)
                    intent.putExtra("goods_id", goodsId)
                    val cover = Pair<View, String>(view.findViewById(R.id.iv_cover), "goods_image")
                    val title = Pair<View, String>(view.findViewById(R.id.tv_title), "goods_title")
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CategoryActivity, cover, title)
                    startActivityForResult(intent, Constant.INTENT_REQUEST_CODE, optionsCompat.toBundle())
//                    startActivity<GoodsDetailActivity>("goods_id" to goodsId)
                } else {
                    startActivityForResult<AuctionDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to goodsId)
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }

        et_search.run {
            addTextChangedListener(object : EditWatcher() {
                override fun afterTextChanged(editable: Editable?) {
                    mSearchParam = editable.toString().trim { it <= ' ' }
                }
            })
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    reload()
                    hideKeyboard(et_search)
                    return@OnEditorActionListener true
                }

                false
            })
            hint = mCate2Name
        }

        setOnClickListener(iv_back, tv_share, tv_new_shelves, tv_intercept, tv_price, tv_filter, tv_reset, tv_confirm)

        mPresenter?.getThirdLevel(mCateId2)
    }

    override fun initData() {
        mPresenter?.getSearchGoodsList(mCateId1, mCateId2, mCateId3, mPage, mCount, mSearchParam, mState,
                mMinAmount, mMaxAmount)
    }

    override fun createPresenter() = CategoryPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_share -> {
                val dialog = CategoryShareDialog.newInstance(mCateId1, mCateId2, mCateId3, mCate2Name!!)
                dialog.show(supportFragmentManager, "categoryShare")
            }
            R.id.tv_new_shelves -> {
                setSelect(1)
                reload()
            }
            R.id.tv_intercept -> {
                setSelect(2)
                reload()
            }
            R.id.tv_price -> {
                setSelect(3)
                reload()
            }
            R.id.tv_filter -> drawer_layout.openDrawer(GravityCompat.END)
            R.id.tv_reset -> {
                mMinAmount = 0
                mMaxAmount = 0

                et_min_amount.setText("")
                et_max_amount.setText("")
            }
            R.id.tv_confirm -> {
                val minAmount = et_min_amount.text.toString().trim { it <= ' ' }
                val maxAmount = et_max_amount.text.toString().trim { it <= ' ' }
                if (minAmount.isEmpty()) {
                    showToast("请输入最低价")
                    return
                }
                if (maxAmount.isEmpty()) {
                    showToast("请输入最高价")
                    return
                }

                mMinAmount = (minAmount.toFloat() * 100).toLong()
                mMaxAmount = (maxAmount.toFloat() * 100).toLong()
                if (mMaxAmount < mMinAmount) {
                    showToast("最低价不能高于最高价")
                    return
                }

                drawer_layout.closeDrawer(GravityCompat.END)
                reload()
            }
        }
    }

    private fun setSelect(position: Int) {
        mState = position

        mMinAmount = 0
        mMaxAmount = 0
        et_min_amount.setText("")
        et_max_amount.setText("")

        tv_new_shelves.isSelected = position == 1
        tv_intercept.isSelected = position == 2
        tv_price.isSelected = position == 3
    }

    override fun getThirdLevelSuccess(bean: CityBean) {
        bean.data?.run {
            ll_container.removeAllViews()

            val data = CityBean.DataBean().apply {
                dictId = 0
                codeName = "全部"
            }
            val list = this.toMutableList()
            list.add(0, data)
            list.forEachIndexed { index, cityListBean ->
                val textView = TextView(applicationContext).apply {
                    layoutParams = FrameLayout.LayoutParams(context.getDimensionPixelSize(R.dimen.dp_58), context.getDimensionPixelSize(R.dimen.dp_22)).apply {
                        leftMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                        if (index == list.size - 1) {
                            rightMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                        }
                    }

                    tag = index
                    gravity = Gravity.CENTER
                    setTextColor(DrawableCreator.Builder()
                            .setSelectedTextColor(Color.WHITE)
                            .setUnSelectedTextColor(0xFF6A6A6A.toInt())
                            .buildTextColor())
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_10))
                    text = cityListBean.codeName
                    background = DrawableCreator.Builder()
                            .setSelectedSolidColor(0xFFE36C6C.toInt(), 0xFFF4F5F7.toInt())
                            .setCornersRadius(context.getDimension(R.dimen.dp_22))
                            .build()
                    if (index == 0) {
                        isSelected = true
                    }

                    setOnClickListener {
                        val tag = it.tag.toString().toInt()
                        if (mSelectIndex != tag) {
                            ll_container.getChildAt(mSelectIndex).isSelected = false
                            isSelected = true
                            mSelectIndex = tag
                        }
                        //获取屏幕宽度
                        val screenWidth = resources.displayMetrics.widthPixels
                        // 计算控件居正中时距离左侧屏幕的距离
                        val middleLeftPosition = (screenWidth - it.width) / 2
                        // 正中间位置需要向左偏移的距离
                        val offset = it.left - middleLeftPosition
                        // 让水平的滚动视图按照执行的x的偏移量进行移动
                        horizontal_scroll_view.smoothScrollTo(offset, 0)

                        mCateId3 = cityListBean.dictId
                        reload()
                    }
                }
                ll_container.addView(textView)
            }
        }
    }

    override fun getSearchGoodsListSuccess(bean: SearchGoodsBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (!hasMore) {
                mAdapter.addNoMoreDataFooter()
            } else {
                mAdapter.removeAllFooterView()
            }
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cate_list_share_bg)
                val path = "pages/searchStatus/searchStatus?fid=${mCateId1}&cid=${mCateId2}&type=1&value=${mCate2Name}&inviteCode=${SPUtils.getInt("user_id")}"
                WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", path, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap)
            } else if (obj == "shareFriendCircle") {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cate_list_share_bg)
                val headImg = SPUtils.getString("head_img")
                val nickName = SPUtils.getString("nick_name")
                val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
                WXOptionUtils.share(mWXAPI, shareUrl, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap, true)
            }
        }
    }

}
