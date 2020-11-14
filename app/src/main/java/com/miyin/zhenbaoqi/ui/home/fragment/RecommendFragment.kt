package com.miyin.zhenbaoqi.ui.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.home.activity.LeakDetectionActivity
import com.miyin.zhenbaoqi.ui.home.activity.NewcomerWelfareActivity
import com.miyin.zhenbaoqi.ui.home.activity.PreferredActivity
import com.miyin.zhenbaoqi.ui.home.adapter.HeaderLiveAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.RecommendAdapter
import com.miyin.zhenbaoqi.ui.home.contract.RecommendContract
import com.miyin.zhenbaoqi.ui.home.dialog.NewcomerWelfareDialog
import com.miyin.zhenbaoqi.ui.home.presenter.RecommendPresenter
import com.miyin.zhenbaoqi.ui.live.activity.PullLiveActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.header_recommend.view.*
import kotlinx.android.synthetic.main.layout_refresh.*
import java.util.*

class RecommendFragment : BaseListFragment<RecommendContract.IView, RecommendContract.IPresenter>(), RecommendContract.IView {

    private var mList = mutableListOf<HomeGoodsHotBean.DataBean>()
    private lateinit var mAdapter: RecommendAdapter

    /* 好货直播 */
    private var mHeaderLiveList = mutableListOf<LiveHotBean.LiveHotDataBean>()
    private lateinit var mHeaderLiveAdapter: HeaderLiveAdapter
    /* 为你推荐 */
//    private var mHeaderRecommendList = mutableListOf<String>()
//    private lateinit var mHeaderRecommendAdapter: HeaderRecommendAdapter
    /* 推荐店铺 */
//    private var mHeaderShopList = mutableListOf<String>()
//    private lateinit var mHeaderShopAdapter: HeaderShopAdapter

    private lateinit var mHeaderView: View
    private val mBannerList = mutableListOf<HomeBannerBean.ListBean>()
    private var mIsClickBanner = false

    companion object {
        fun newInstance() = RecommendFragment()
    }

    override fun getContentView() = R.layout.fragment_recommend

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = RecommendAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mAdapter.setOnItemClickListener { _, _, position ->
                startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mList[position].goods_id)
            }

            mHeaderView = LayoutInflater.from(context).inflate(R.layout.header_recommend, this, false).apply {
                /*ShadowUtils.apply(fl_container, ShadowUtils.Config()
                        .setShadowColor(0x40EF0826)
                        .setShadowRadius(DensityUtils.dp2px(8f).toFloat())
                        .setShadowSize(DensityUtils.dp2px(10f)))*/
                /* 热门直播 */
                rv_goods_live.isFocusable = false
                rv_goods_live.isFocusableInTouchMode = false
                mHeaderLiveAdapter = HeaderLiveAdapter(mHeaderLiveList)
                rv_goods_live.adapter = mHeaderLiveAdapter
                rv_goods_live.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rv_goods_live.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        calculateScrollProportion(recyclerView, fl_live_line, view_live_line)
                    }
                })
                mHeaderLiveAdapter.setOnItemClickListener { _, _, position ->
                    val bean = mHeaderLiveList[position]
                    mPresenter?.liveRoomEntry(bean.room_id, position)
                }

                /* 为你推荐 */
                /*flow_layout.setAdapter(object : TagAdapter(listOf("翡翠", "和田玉", "手串")) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(context).apply {
                        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, context.getDimensionPixelSize(R.dimen.dp_18)).apply {
                            rightMargin = context.getDimensionPixelSize(R.dimen.dp_8)
                        }
                        gravity = Gravity.CENTER
                        setPadding(context.resources.getDimensionPixelSize(R.dimen.dp_9), 0, context.getDimensionPixelSize(R.dimen.dp_9), 0)
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_11))
                        setTextColor(Color.WHITE)
                        text = data.toString()
                        background = DrawableCreator.Builder()
                                .setSolidColor(0x26FFFFFF)
                                .setCornersRadius(context.getDimension(R.dimen.dp_18))
                                .build()
                    }
                })*/

                /*for (i in 0..6) {
                    mHeaderRecommendList.add("")
                }
                recycler_view.isFocusable = false
                recycler_view.isFocusableInTouchMode = false
                mHeaderRecommendAdapter = HeaderRecommendAdapter(mHeaderRecommendList)
                recycler_view.adapter = mHeaderRecommendAdapter
                recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)*/

                /* 推荐店铺 */
                /*val transform = CornerTransform(context, DensityUtils.dp2px(3f).toFloat())
                iv_cover.transform(url, transform)
                tv_shop_name.text = "捞面和田玉工作室"
                tv_warranty.text = "质保金：20800"
                tv_attention_count.text = "100人关注"
                for (i in 0..6) {
                    mHeaderShopList.add("")
                }
                rv_shop.isFocusable = false
                rv_shop.isFocusableInTouchMode = false
                mHeaderShopAdapter = HeaderShopAdapter(mHeaderShopList)
                rv_shop.adapter = mHeaderShopAdapter
                rv_shop.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rv_shop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        calculateScrollProportion(recyclerView, fl_shop_line, view_shop_line)
                    }
                })
                tv_total.text = "共160件宝贝，快去看看"*/

                setOnClickListener(ll_container, tv_treasure_market, iv_jian_lou, iv_jian_bao, iv_qi_pai, iv_te_mai, iv_welfare,
                        tv_preferred, tv_leak_detection)
            }
            mAdapter.addHeaderView(mHeaderView)
        }

        refreshAndLoadMore(smart_refresh_layout) { initData() }
        val couponDialogDate = SPUtils.getString("coupon_dialog_date")
        val currentDate = TimeUtils.date2String(Date(), "yyyy-MM-dd")
        if (currentDate != couponDialogDate) {
            mPresenter?.canGetCoupon()
        }
    }

    override fun initData() {
        if (mPage == 1) {
            mPresenter?.homeBanner()
            mPresenter?.getHotList()
        }
        mPresenter?.getHomeGoodsHotList(mPage, mCount)
    }

    override fun createPresenter() = RecommendPresenter()

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
            R.id.tv_treasure_market -> WebActivity.openActivity(context!!, "珍品市场", Constant.ZHEN_PIN)
            R.id.tv_leak_detection -> startActivity<LeakDetectionActivity>()
            R.id.tv_preferred -> startActivity<PreferredActivity>()
            R.id.iv_jian_lou -> WebActivity.openActivity(context!!, "1元捡漏", Constant.LEAK_COLLECT)
            R.id.iv_jian_bao, R.id.ll_container -> WebActivity.openActivity(context!!, "线上鉴宝", Constant.ZHEN_BAO_FU_WU)
            R.id.iv_qi_pai -> WebActivity.openActivity(context!!, "0元起购", Constant.START_SHOOT)
            R.id.iv_te_mai -> WebActivity.openActivity(context!!, "精品特卖", Constant.ZHEN_PIN_PRODUCT)
            R.id.iv_welfare -> startActivity<NewcomerWelfareActivity>()
        }
    }

    override fun canGetCouponSuccess(bean: CouponGetBean) {
        if (bean.data?.isCan == 0) {
            val currentDate = TimeUtils.date2String(Date(), "yyyy-MM-dd")
            SPUtils.putString("coupon_dialog_date", currentDate)

            val dialog = NewcomerWelfareDialog.newInstance()
            dialog.show(childFragmentManager, "newcomerWelfare")
        }
    }

    override fun getHomeGoodsHotListSuccess(bean: HomeGoodsHotBean) {
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

    override fun getHomeBannerSuccess(bean: HomeBannerBean) {
        bean.list?.let { it ->
            mBannerList.clear()
            mBannerList.addAll(it)

            mHeaderView.banner.setImages(it)
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

    override fun getHotListSuccess(bean: LiveHotBean) {
        bean.list?.run {
            visible(mHeaderView.rv_goods_live, mHeaderView.fl_goods_live)
            mHeaderLiveList.clear()
            mHeaderLiveList.addAll(this)
            mHeaderLiveAdapter.setNewData(mHeaderLiveList)
        }
    }

    override fun liveRoomEntrySuccess(data: LiveEntryRoomBean, position: Int) {
        val bean = mHeaderLiveList[position]
        startActivity<PullLiveActivity>("room_id" to bean.room_id,
                "play_url" to bean.play_url, "cover_url" to bean.face_url,
                "name" to bean.room_name, "icon_url" to bean.icon_url,
                "fans_count" to bean.fans_no, "is_focus" to bean.is_focus,
                "merchant_id" to bean.merchants_id, "user_id" to bean.user_id,
                "account_name" to data.data?.accuount_name, "position" to position)
    }

    override fun bannerClickSuccess(position: Int) {
        mIsClickBanner = false
        val bannerBean = mBannerList[position]
        if (!bannerBean.url.isNullOrEmpty()) {
            WebActivity.openActivity(context!!, bannerBean.title ?: "", bannerBean.url!!)
        }
    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
            1 -> {
                gone(mHeaderView.rv_goods_live, mHeaderView.fl_goods_live)
            }
            2 -> {
                mIsClickBanner = false
            }
        }
    }

    private fun calculateScrollProportion(recyclerView: RecyclerView, container: ViewGroup, line: View) {
        // 当前 RecyclerView 显示区域的高度。水平列表屏幕从左侧到右侧显示范围
        val extent = recyclerView.computeHorizontalScrollExtent()
        // 整体的高度，注意是整体，包括在显示区域之外的。
        val range = recyclerView.computeHorizontalScrollRange()
        // 已经滚动的距离，为 0 时表示已处于顶部。
        val offset = recyclerView.computeHorizontalScrollOffset()
        // 计算出溢出部分的宽度，即屏幕外剩下的宽度
        val maxEndX = range - extent
        // 计算比例
        val proportion = offset.toFloat() / maxEndX

        val layoutWidth = container.width
        val indicatorViewWidth = line.width
        // 可滑动的距离
        val scrollableDistance = layoutWidth - indicatorViewWidth
        // 设置滚动条移动
        line.translationX = scrollableDistance * proportion
    }

}
