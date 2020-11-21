@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.ui.sort.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BasePayActivity
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.miyin.zhenbaoqi.ui.shop.dialog.ShopShareDialog
import com.miyin.zhenbaoqi.ui.sort.dialog.AddPriceDialog
import com.miyin.zhenbaoqi.widget.ShowAllTextView
import kotlinx.android.synthetic.main.layout_refresh.*
import kotlinx.android.synthetic.main.header_auction_detail.view.*
import kotlinx.android.synthetic.main.layout_shop_bottom.*
import kotlinx.android.synthetic.main.layout_shop_info.view.*
import com.miyin.zhenbaoqi.ui.sort.adapter.AddPriceAdapter
import com.miyin.zhenbaoqi.ui.sort.adapter.AuctionGoodsAdapter
import com.miyin.zhenbaoqi.ui.sort.contract.AuctionDetailContract
import com.miyin.zhenbaoqi.ui.sort.dialog.PayDialog
import com.miyin.zhenbaoqi.ui.sort.presenter.AuctionDetailPresenter
import com.miyin.zhenbaoqi.utils.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.youth.banner.loader.ImageLoaderInterface
import kotlinx.android.synthetic.main.layout_shop_bottom.tv_entry_shop
import kotlinx.android.synthetic.main.layout_shop_bottom.tv_private_message
import kotlinx.android.synthetic.main.layout_shop_info.*
import kotlinx.android.synthetic.main.pop_category.view.*
import java.net.URLEncoder
import java.util.*

@SuppressLint("SetTextI18n")
class AuctionDetailActivity : BasePayActivity<AuctionDetailContract.IView, AuctionDetailContract.IPresenter>(), AuctionDetailContract.IView, OnDialogCallback {

    private var mGoodsId = 0
    private var mCountDownTimer: CountDownTimer? = null
    private var mList = mutableListOf<AuctionGoodsBean.ListBean>()
    private lateinit var mAdapter: AuctionGoodsAdapter
    private var mHeaderView: View? = null
    private lateinit var mAddPriceAdapter: AddPriceAdapter
    private var mAddPriceList = mutableListOf<AuctionGoodsRecordBean.ListBean>()
    private var mUserId = 0
    private var mMerchantName: String? = null
    private var mMerchantId = 0
    private var mAddPrice = 0.0f
    private var mMerchantState = 1
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mGoodsDesc: String? = null
    private var mEndTime = 0L
    private var mStartTime = 0L
    private var mServerTime = 0L
    private var mIsPaySecurityDeposit = 1
    private var mEnsureAmount = 0L
    private var mAuctionState = 4
    private var mBean: GoodsDetailBean? = null
    private lateinit var mWXAPI: IWXAPI
    private var mBannerList = mutableListOf<String>()
    private var mCollectState = 0
    private var mGoodsFreight = 0
    private var mState = -1

    override fun getContentView(): Int {
        with(intent) {
            mGoodsId = getIntExtra("goods_id", 0)
            mState = getIntExtra("state", 0)
        }
        return R.layout.activity_auction_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("拍品详情", rightTitle = "分享")
        immersionBar { statusBarDarkFont(true) }
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)

        recycler_view.run {
            mAdapter = AuctionGoodsAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                startActivity<AuctionDetailActivity>("goods_id" to mList[position].goods_id)
            }

            mHeaderView = LayoutInflater.from(applicationContext).inflate(R.layout.header_auction_detail, recycler_view, false).apply {
                rv_inner.run {
                    isFocusableInTouchMode = false
                    isFocusable = false

                    mAddPriceAdapter = AddPriceAdapter(mAddPriceList)
                    adapter = mAddPriceAdapter
                    layoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(context) {
                        override fun canScrollVertically() = false
                    }
                }
                banner.setImages(mBannerList)
                        .setImageLoader(object : ImageLoaderInterface {
                            override fun createImageView(context: Context?, path: Any?): View {
                                val url = path.toString().toLowerCase(Locale.getDefault())
                                return if (url.contains("mp4")) {
                                    JzvdStd(this@AuctionDetailActivity)
                                } else {
                                    ImageView(context)
                                }
                            }

                            override fun displayImage(context: Context, path: Any?, view: View) {
                                val url = path.toString()
                                if (url.contains("mp4")) {
                                    (view as JzvdStd).run {
                                        thumbImageView.loadImg(url)
                                        setUp(url, "")
                                        startVideo()
                                    }
                                } else {
                                    (view as ImageView).loadImg(url)
                                }
                            }
                        })
                        /*.setOnBannerListener {
                            val goodsVideo = mBean?.data?.goods_video
                            val position: Int
                            val bannerList = if (goodsVideo.isNullOrEmpty()) {
                                position = it
                                mBannerList
                            } else {
                                position = it - 1
                                mBannerList.subList(1, mBannerList.size)
                            }
                            MaxImageView.maxImageView(this@AuctionDetailActivity, bannerList, position)
                        }*/
                        .start()
                banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(p0: Int) {

                    }

                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        if (position != 0) {
                            Jzvd.releaseAllVideos()
                        }
                    }
                })
            }
            mAdapter.addHeaderView(mHeaderView)
        }

        setOnClickListener(mHeaderView?.tv_attention, tv_shop_attention, tv_entry_shop, tv_private_message, ll_add_price,
                mHeaderView?.view_jianzhenbao, mHeaderView?.tv_explain, mHeaderView?.tv_collect)
        smart_refresh_layout.setEnableLoadMore(false)
        smart_refresh_layout.setOnRefreshListener {
            initData()
            it.finishRefresh(Constant.DELAY_TIME)
        }
    }

    override fun initData() {
        mPresenter?.getAuctionGoodsDetail(mGoodsId)
        mPresenter?.getAuctionGoodsList(mGoodsId)
    }

    override fun createPresenter() = AuctionDetailPresenter()

    override fun onRightClick() {
        /*mBean?.data?.merchants?.let {
            val bean = MerchantInfoBean.DataBean().apply {
                merchants_back = it.merchants_back
                head_img = it.head_img
                merchants_name = it.merchants_name
                merchants_subtitle = it.merchants_subtitle
                quality_retention_money = it.quality_balance.toInt()
            }
            val dialog = ShopShareDialog.newInstance(bean, mMerchantId)
            dialog.show(supportFragmentManager, "goodsShare")
        }*/
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    override fun onDestroy() {
        if (null != mCountDownTimer) {
            mCountDownTimer?.cancel()
            mCountDownTimer = null
        }
        MaxImageView.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_attention, R.id.tv_shop_attention -> mPresenter?.updateMerchantState(mMerchantState, mMerchantId)
            R.id.tv_explain -> WebActivity.openActivity(applicationContext, "拍卖说明", Constant.AUCTION_EXPLAIN)
            R.id.tv_entry_shop -> {
                startActivity<MerchantMessageActivity>("merchant_id" to mMerchantId)
            }
            R.id.tv_private_message -> {
                if (mUserId == SPUtils.getInt("user_id")) {
                    showToast("用户不能和自己聊天!")
                    return
                }

                val merchantName = if (mMerchantName.isNullOrEmpty()) mUserId.toString() else mMerchantName
                val json = "{\"goodsImg\": \"$mGoodsImg\",\"goods_amount\": \"${mBean?.data?.goodsAmount}\",\"goodsName\":\"$mGoodsName\", \"goods_id\":\"$mGoodsId\", \"type\":\"1\", \"goods_type\":\"2\"}"
                startActivity<OnlineCustomerActivity>("title" to merchantName, "user_id" to mUserId, "data" to json)
            }
            R.id.ll_add_price -> {
                if (mServerTime < mStartTime) {
                    showToast("还未到竞拍时间")
                } else if (mAuctionState == 1) {
                    if (mEnsureAmount != 0L) {
                        if (mIsPaySecurityDeposit != 0) {
                            addPrice()
                        } else {
                            if (mUserId == SPUtils.getInt("user_id")) {
                                showToast("不能购买自己的产品")
                                return
                            }
                            val dialog = PayDialog.newInstance(FormatUtils.formatNumber(mEnsureAmount / 100f), "auction")
                            dialog.show(supportFragmentManager, "ensureAmount")
                        }
                    } else {
                        addPrice()
                    }
                } else if (mAuctionState == 2) {
                    if (mState != 4) {
                        val data = mAddPriceList[0]
                        if (data.state == 2 && !data.order_number.isNullOrEmpty()) {
                            val goodsPrice = data.auction_bid_amount - mEnsureAmount
                            startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE, "price" to goodsPrice, "goodsImg" to mGoodsImg,
                                    "goodsFreight" to mGoodsFreight, "goodsName" to mGoodsName, "goods_id" to mGoodsId, "from" to "auction",
                                    "order_number" to data.order_number, "shop_name" to mMerchantName, "isSeven" to (mBean?.data?.isSeven == 0))
                        }
                    }
                }
            }
            R.id.view_jianzhenbao -> WebActivity.openActivity(applicationContext, "线上鉴宝", Constant.ZHEN_BAO_FU_WU)
            R.id.tv_collect -> mPresenter?.updateCollectState(mGoodsId, mCollectState)
        }
    }

    private fun addPrice() {
        if (mAddPrice == 0.0f) {
            return
        }
        if (mUserId == SPUtils.getInt("user_id")) {
            showToast("不能给自己的产品加价")
            return
        }

        val dialog = AddPriceDialog.newInstance(FormatUtils.formatNumber(mAddPrice))
        dialog.show(supportFragmentManager, "addPrice")
    }

    override fun getAuctionGoodsDetailSuccess(bean: GoodsDetailBean) {
        mBean = bean
        bean.data?.run {
//            mAddPrice = add_amount / 100f
            mGoodsImg = goodsImg
            mGoodsName = goodsName
            mGoodsDesc = goodsDescribe
//            mEndTime = end_time_timestamp
//            mStartTime = start_time_timestamp
//            mServerTime = service_time
//            mIsPaySecurityDeposit = is_quality
//            mEnsureAmount = ensure_amount
//            mAuctionState = auction_state
            mGoodsFreight = goodsFreight

            mHeaderView?.run {
//                tv_goodsName.text = goodsName
                tv_introduce.maxShowLines = 3
                tv_introduce.setMyText(goodsDescribe ?: "")
                tv_introduce.setOnAllSpanClickListener(object : ShowAllTextView.ShowAllSpan.OnAllSpanClickListener {
                    override fun onClick(widget: View) {
                        tv_introduce.maxShowLines = 100
                    }
                })

                mBannerList.clear()
//                if (!goods_video.isNullOrEmpty()) {
//                    mBannerList.add(goods_video!!)
//                }
                if (!goodsImg.isNullOrEmpty()) {
                    if (goodsImg!!.contains(",")) {
                        goodsImg!!.split(",").forEach {
                            mBannerList.add(it)
                        }
                    } else {
                        mBannerList.add(goodsImg!!)
                    }
                }
                banner.update(mBannerList)

                if (goodsFreight == 0) {
                    tv_free_shipping.text = "全国包邮"
                } else {
                    tv_free_shipping.text = "邮费5元"
                }
                if (isSeven == 0) {
                    visible(tv_seven)
                } else {
                    gone(tv_seven)
                }
                tv_guide_price.text = SpanUtils()
                        .append("指导价：")
                        .append("¥${FormatUtils.formatNumber(goodsOriginalAmount / 100f)}")
                        .create()

               /* when (auction_state) {
                    1 -> {
                        if (service_time >= start_time_timestamp) {
                            tv_auction_state.text = "正在竞拍"
                            val time = end_time_timestamp - service_time
                            mCountDownTimer = object : CountDownTimer(time, 1000) {
                                override fun onFinish() {
                                    mAuctionState = 4
                                    mPresenter?.callHandleGoods()
                                }

                                override fun onTick(timestamp: Long) {
                                    if (timestamp < 24 * 60 * 60 * 1000) {
                                        val millis2String = TimeUtils.millis2String(timestamp - TimeZone.getDefault().rawOffset, "HH:mm:ss")
                                        val timeArray = millis2String.split(":")
                                        tv_count_down.text = SpanUtils()
                                                .append("距截拍：")
                                                .append(" ${timeArray[0]} ").setFontSize(16, true).setBold()
                                                .append("时")
                                                .append(" ${timeArray[1]} ").setFontSize(16, true).setBold()
                                                .append("分")
                                                .append(" ${timeArray[2]}").setFontSize(16, true).setBold()
                                                .append("秒")
                                                .create()
                                    } else {
                                        val millis2String = TimeUtils.millis2String(timestamp - TimeZone.getDefault().rawOffset, "dd:HH:mm")
                                        val timeArray = millis2String.split(":")
                                        tv_count_down.text = SpanUtils()
                                                .append("距截拍：")
                                                .append(" ${timeArray[0]} ").setFontSize(16, true).setBold()
                                                .append("天")
                                                .append(" ${timeArray[1]} ").setFontSize(16, true).setBold()
                                                .append("时")
                                                .append(" ${timeArray[2]}").setFontSize(16, true).setBold()
                                                .append("分")
                                                .create()
                                    }
                                }
                            }
                            mCountDownTimer?.start()
                        } else {
                            tv_auction_state.text = "还未开始"
                        }
                    }
                    2 -> {
                        if (mState == 4) {
                            tv_auction_state.text = "交易结束"
                            ll_add_price.isEnabled = false
                            gone(tv_red_package)
                        } else {
                            tv_auction_state.text = "已结束"
                            tv_change_text.text = "立即支付"
                            gone(tv_red_package)
                        }
                    }
                    3 -> {
                        tv_auction_state.text = "已结束"
                        ll_add_price.isEnabled = false

                        tv_change_text.text = "${TimeUtils.millis2String(end_time_timestamp, "MM月dd日 HH:mm:ss")}拍卖结束"
                        gone(tv_red_package)
                    }
                }

                tv_init_price.text = "¥${FormatUtils.formatNumber(start_amount / 100f)}"
                tv_add_price.text = "¥${FormatUtils.formatNumber(add_amount / 100f)}/次"
                tv_security_deposit.text = "¥${FormatUtils.formatNumber(ensure_amount / 100f)}"
                */
//                updateCollectStateSuccess(collection_state)
            }
        }
//        bean.data?.merchants?.run {
//            mMerchantId = merchants_id
//            mMerchantName = merchants_name
//            mMerchantState = is_focus
//            mUserId = user_id
//
//            updateMerchantStateSuccess(is_focus)
//            mHeaderView?.run {
//                val transform = RoundCornersTransform(getDimension(R.dimen.dp_4), RoundCornersTransform.CornerType.ALL)
//                iv_photo.loadImgAll(head_img, R.drawable.ic_merchant_header_default, transform)
//                tv_shop_name.text = if (merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_name
//                tv_warranty.text = "质保金：${FormatUtils.formatNumber(quality_balance / 100f)}"
//                tv_attention_count.text = "关注数：$focus_size"
//                if (quality_balance > 0) {
//                    visible(iv_header_vip)
//                } else {
//                    gone(iv_header_vip)
//                }
//
//                iv_cover.loadImgAll(head_img, R.drawable.ic_merchant_header_default, transform)
//                tv_name.text = if (merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_name
//                tv_desc.text = if (merchants_subtitle.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_subtitle
//
//                tv_shop_warranty.text = SpanUtils()
//                        .appendLine(FormatUtils.formatNumber(quality_balance / 100f)).setFontSize(18, true).setForegroundColor(0xFF202020.toInt())
//                        .append("质保金")
//                        .create()
//                tv_shop_impression.text = SpanUtils()
//                        .appendLine("$evaluation_size").setFontSize(18, true).setForegroundColor(0xFF202020.toInt())
//                        .append("店铺印象")
//                        .create()
//                tv_shop_attention_count.text = SpanUtils()
//                        .appendLine("$focus_size").setFontSize(18, true).setForegroundColor(0xFF202020.toInt())
//                        .append("关注")
//                        .create()
//            }
//        }
//
//        mPresenter?.getAuctionGoodsRecord(mGoodsId, 1, 500)
    }

    override fun getAuctionGoodsRecordSuccess(bean: AuctionGoodsRecordBean) {
        mHeaderView?.run {
            tv_refresh_price_time.text = "更新出价\n${TimeUtils.millis2String(System.currentTimeMillis(), "HH:mm:ss")}"
        }
        bean.list?.let {
            mAddPriceList = it.toMutableList()

            val list = if (mAddPriceList.size > 4) {
                mAddPriceList.subList(0, 4)
            } else {
                mAddPriceList
            }
            mAddPriceAdapter.setNewData(list)

            if (mAddPriceList.size > 4) {
                val footerView = TextView(applicationContext).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, context.getDimensionPixelSize(R.dimen.dp_45))
                    gravity = Gravity.CENTER
                    text = "查看更多"
                    setTextColor(0xFF4196F5.toInt())
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_14))
                    setOnClickListener {
                        mAddPriceAdapter.removeAllFooterView()
                        mAddPriceAdapter.setNewData(mAddPriceList)
                    }
                }
                mAddPriceAdapter.addFooterView(footerView)
            }
        }

        if (mAuctionState == 4) {
            mAddPriceAdapter.setAuctionState(true)
            val data = mAddPriceAdapter.data

            val userId = data[0].user_id
            if (userId == SPUtils.getInt("user_id")) {
                mAuctionState = 2

                tv_change_text.text = "立即支付"
                gone(tv_red_package)
            } else {
                mAuctionState = 3
                ll_add_price.isEnabled = false

                tv_change_text.text = "${TimeUtils.millis2String(mEndTime, "MM月dd日 HH:mm:ss")}拍卖结束"
                gone(tv_red_package)
            }
        } else if (mAuctionState == 2) {
            mAddPriceAdapter.setAuctionState(true)
            mHeaderView?.tv_auction_state?.text = "已结束"

            tv_change_text.text = "立即支付"
        } else if (mAuctionState == 3) {
            mAddPriceAdapter.setAuctionState(true)
            mHeaderView?.tv_auction_state?.text = "已结束"

            ll_add_price.isEnabled = false
            tv_change_text.text = "${TimeUtils.millis2String(mEndTime, "MM月dd日 HH:mm:ss")}拍卖结束"
        }
    }

    override fun getAuctionGoodsListSuccess(bean: AuctionGoodsBean) {
        bean.data?.let {
            if (it.isEmpty()) {
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            } else {
                mList = it.toMutableList()
                mAdapter.setNewData(mList)
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

    override fun updateMerchantStateSuccess(focusState: Int) {
        mMerchantState = focusState
        if (focusState == 0) {
            mHeaderView?.tv_attention!!.text = "已关注"

            tv_shop_attention.text = "已关注"
            tv_shop_attention.isSelected = true
        } else {
            mHeaderView?.tv_attention!!.text = "+ 关注"

            tv_shop_attention.text = "+ 关注"
            tv_shop_attention.isSelected = false
        }
    }

    override fun auctionBindGoodsSuccess() {
        mPresenter?.getAuctionGoodsRecord(mGoodsId, 1, 500)
    }

    override fun callHandleGoodsSuccess() {
        mPresenter?.getAuctionGoodsRecord(mGoodsId, 1, 500)
    }

    override fun auctionSecurityDepositSuccess(bean: PayResultBean, payType: Int) {
        when (payType) {
            1 -> {

            }
            2 -> {
                onAliCallback(bean.alipayBody ?: "")
            }
            3 -> {
//                onWXCallback(bean)
            }
        }
    }

    override fun updateCollectStateSuccess(collectState: Int) {
        mCollectState = collectState
        mHeaderView?.tv_collect!!.isSelected = collectState == 0
    }

    override fun onFailure(msg: String, flag: Int) {
        if (msg == "auctionGoodsRecord" && mAuctionState == 4) {
            mAddPriceAdapter.setAuctionState(true)
            mAuctionState = 3
            ll_add_price.isEnabled = false

            tv_change_text.text = "${TimeUtils.millis2String(mEndTime, "MM月dd日 HH:mm:ss")}拍卖结束"
            gone(tv_red_package)
        }
    }

    override fun onAliPaySuccess() {
        mIsPaySecurityDeposit = 1
    }

    override fun onWXPaySuccess() {
        mIsPaySecurityDeposit = 1
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            /*when (obj) {
                "add_price" -> mPresenter?.auctionBindGoods(mGoodsId)
                "pay" -> mPresenter?.auctionSecurityDeposit(mGoodsId, flag)
                "shareFriend" -> {
                    val merchantsBack: Any = if (mBean?.data?.merchants?.merchants_back.isNullOrEmpty()) {
                        R.drawable.ic_merchant_share_bg
                    } else {
                        mBean?.data?.merchants?.merchants_back!!
                    }
                    Glide.with(applicationContext).asBitmap().load(merchantsBack).into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val merchantName = if (mBean?.data?.merchants?.merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mBean?.data?.merchants?.merchants_name!!
                            val merchantDesc = if (mBean?.data?.merchants?.merchants_subtitle.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mBean?.data?.merchants?.merchants_subtitle!!
                            WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", "/pages/store/store?id=${mMerchantId}&inviteCode=${SPUtils.getInt("user_id")}",
                                    merchantName, merchantDesc, resource)
                        }
                    })
                }
                "shareFriendCircle" -> {
                    val merchantsBack: Any = if (mBean?.data?.merchants?.merchants_back.isNullOrEmpty()) {
                        R.drawable.ic_merchant_share_bg
                    } else {
                        mBean?.data?.merchants?.merchants_back!!
                    }
                    Glide.with(applicationContext).asBitmap().load(merchantsBack).into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val headImg = SPUtils.getString("head_img")
                            val nickName = SPUtils.getString("nick_name")
                            val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
                            WXOptionUtils.share(mWXAPI, shareUrl, mGoodsName!!, mGoodsDesc!!, resource, true)
                        }
                    })
                }
            }*/
        }
    }

}
