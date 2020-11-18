@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.ui.sort.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.ArrayMap
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.http.OkHttpUtils
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.miyin.zhenbaoqi.ui.shop.activity.release_goods.ReleaseGoodsActivity
import com.miyin.zhenbaoqi.ui.sort.adapter.GoodsDetailPhotoAdapter
import com.miyin.zhenbaoqi.ui.sort.adapter.GoodsInfoAdapter
import com.miyin.zhenbaoqi.ui.sort.adapter.GoodsNoticeAdapter
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsDetailContract
import com.miyin.zhenbaoqi.ui.sort.dialog.AddGoodsCountDialog
import com.miyin.zhenbaoqi.ui.sort.dialog.GoodsShareDialog
import com.miyin.zhenbaoqi.ui.sort.presenter.GoodsDetailPresenter
import com.miyin.zhenbaoqi.utils.*
import com.noober.background.drawable.DrawableCreator
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.youth.banner.loader.ImageLoaderInterface
import kotlinx.android.synthetic.main.activity_goods_detail.*
import okhttp3.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.*

@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class GoodsDetailActivity : BaseMvpActivity<GoodsDetailContract.IView, GoodsDetailContract.IPresenter>(), GoodsDetailContract.IView, OnDialogCallback {

    private var mGoodsId = 0
    private var mSource = 0
    private var mState = 0
    private var mBannerList = mutableListOf<String>()
    private var mCollectState = 1
    private var mBean: GoodsDetailBean? = null
    private lateinit var mWXAPI: IWXAPI
    private lateinit var mAdapter: GoodsDetailPhotoAdapter
    private val mGoodsInfoList = mutableListOf<ArrayMap<String, String>>()
    private lateinit var mGoodsInfoAdapter: GoodsInfoAdapter
    private lateinit var mGoodsNoticeAdapter: GoodsNoticeAdapter
    private var mCountDownTimer: CountDownTimer? = null
    private var mBannerHeight = 0
    private var mTitleBarHeight = 0
    private var mStickyHeaderHeight = 0
    private var mItemWidth = 0f
    private var mLineWidth = 0
    private var mOffset = 0f
    private var mLastPosition = 0
    private var mHideHeight = 0

    override fun useEventBus() = true

    override fun getContentView(): Int {
        with(intent) {
            mGoodsId = getIntExtra("goodsId", 0)
            mSource = getIntExtra("source", 0)
            mState = getIntExtra("state", 0)
        }
        return R.layout.activity_goods_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("商品详情", rightTitle = "分享")
        mTitleBar.alpha = 0f
        immersionBar { statusBarDarkFont(true) }
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)

        val screenWidth = DensityUtils.getScreenWidth()
        mItemWidth = screenWidth / 2.0f
        view_line.post {
            mLineWidth = view_line.width
            mOffset = (mItemWidth - mLineWidth) / 2
            startAnim(0)
        }

        fl_sticky_header.post {
            mStickyHeaderHeight = fl_sticky_header.height
        }
        ll_goods_notice_header.post {
            Logger.d("top == ${ll_goods_notice_header.top}")
        }
        Logger.d("top == 11 ${getDimensionPixelSize(R.dimen.dp_360)}")

        recycler_view.run {
            mAdapter = GoodsDetailPhotoAdapter(listOf())
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemClickListener { adapter, _, position ->
                MaxImageView.maxImageView(this@GoodsDetailActivity, adapter.data as List<String>, position)
            }
        }

        rv_goods_info.run {
            mGoodsInfoAdapter = GoodsInfoAdapter(mGoodsInfoList)
            adapter = mGoodsInfoAdapter
            layoutManager = LinearLayoutManager(context)
        }

        rv_goods_notice.run {
            mGoodsNoticeAdapter = GoodsNoticeAdapter(listOf("", "", "", ""))
            adapter = mGoodsNoticeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        banner.setImages(mBannerList)
                .setImageLoader(object : ImageLoaderInterface {
                    override fun createImageView(context: Context?, path: Any): View {
                        val url = path.toString().toLowerCase(Locale.getDefault())
                        return if (url.contains("mp4")) {
                            JzvdStd(this@GoodsDetailActivity)
                        } else {
                            ImageView(context)
                        }
                    }

                    override fun displayImage(context: Context?, path: Any?, view: View?) {
                        val url = path.toString()
                        if (url.contains("mp4")) {
                            (view as JzvdStd).run {
                                thumbImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                thumbImageView.loadImg(url)
                                setUp(url, "")
                                startVideo()
                            }
                        } else {
                            (view as ImageView).loadImg(url)
                        }
                    }
                })
                .setOnBannerListener {
                    val goodsVideo = mBean?.data?.goods?.goods_video
                    val position: Int
                    val bannerList = if (goodsVideo.isNullOrEmpty()) {
                        position = it
                        mBannerList
                    } else {
                        position = it - 1
                        mBannerList.subList(1, mBannerList.size)
                    }
                    MaxImageView.maxImageView(this@GoodsDetailActivity, bannerList, position)
                }
                .start()
        banner.setOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
        banner.post {
            mBannerHeight = banner.height
        }
        mTitleBar.post {
            mTitleBarHeight = mTitleBar.height
            consecutive_scroller_layout.stickyOffset = mTitleBarHeight
        }

//        if (mSource == 0) {
//            tv_entry_shop.text = "购物车"
//            tv_entry_shop.isSelected = false
//
//            tv_private_message.text = "客服"
//            tv_private_message.isSelected = false
//
//            tv_pay.text = "立即购买"
//        } else {
//            tv_entry_shop.text = "编辑"
//            tv_entry_shop.isSelected = true
//
//            tv_private_message.text = "继续发布"
//            tv_private_message.isSelected = true
//
//            tv_pay.text = "分享给买家"
//        }


        tv_goods_detail.isSelected = true

        consecutive_scroller_layout.setOnVerticalScrollChangeListener { _, scrollY, _ ->
            if (mHideHeight == 0) {
                computeHideHeight()
            }
            if (scrollY >= mBannerHeight) {
                mTitleBar.alpha = 1f

                if (scrollY >= ll_goods_notice_header.top + mHideHeight) {
                    tv_goods_detail.isSelected = false
                    tv_goods_notice.isSelected = true

                    startAnim(1)
                } else {
                    tv_goods_detail.isSelected = true
                    tv_goods_notice.isSelected = false

                    startAnim(0)
                }
            } else {
                mTitleBar.alpha = scrollY * 1f / mBannerHeight
            }
        }
        consecutive_scroller_layout.setOnStickyChangeListener { oldStickyView, newStickyView ->
            when {
                null != newStickyView -> {
                    tv_goods_detail.isEnabled = true
                    tv_goods_notice.isEnabled = true
                }
                null != oldStickyView -> {
                    tv_goods_detail.isEnabled = false
                    tv_goods_notice.isEnabled = false
                }
                else -> {
                    tv_goods_detail.isEnabled = false
                    tv_goods_notice.isEnabled = false
                }
            }
        }

//        setOnClickListener(tv_collect,tv_goods_detail, tv_goods_notice,
//                tv_entry_shop, tv_private_message, tv_pay)
        setOnClickListener(tv_collect,tv_goods_detail, tv_goods_notice, tv_private_message, tv_pay)
    }

    override fun initData() {
        mPresenter?.getGoodsDetail(mGoodsId)
    }

    override fun createPresenter() = GoodsDetailPresenter()

    override fun onRightClick() {
        if (null == mBean) {
            showToast(getString(R.string.network_error))
            return
        }
        val dialog = GoodsShareDialog.newInstance(mBean!!)
        dialog.show(supportFragmentManager, "goodsShare")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
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
            mCountDownTimer!!.cancel()
            mCountDownTimer = null
        }
        MaxImageView.clear()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_collect -> {
                if (mSource == 0) {
                    mPresenter?.updateCollectState(mGoodsId, mCollectState)
                }
            }
            R.id.tv_goods_detail -> {
                tv_goods_detail.isSelected = true
                tv_goods_notice.isSelected = false

                consecutive_scroller_layout.scrollTo(0, fl_sticky_header.bottom - mTitleBarHeight - mStickyHeaderHeight)
            }
            R.id.tv_goods_notice -> {
                tv_goods_detail.isSelected = false
                tv_goods_notice.isSelected = true

                consecutive_scroller_layout.scrollTo(0, ll_goods_notice_header.top + mHideHeight)
            }
            R.id.tv_shop_attention -> startActivity<MerchantMessageActivity>("merchant_id" to mBean?.data?.goods?.merchants_id)
//            R.id.tv_entry_shop -> {
//                if (mSource == 0) {
//                    startActivity<MerchantMessageActivity>("merchant_id" to mBean?.data?.goods?.merchants_id)
//                } else {
//                    if (mState == 1) {
//                        startActivity<ReleaseGoodsActivity>("goods_detail_bean" to mBean)
//                    } else {
//                        showToast("该商品不是下架产品，暂时不能修改！")
//                    }
//                }
//            }
            R.id.tv_private_message -> {
                if (mSource == 0) {
                    mBean?.data?.run {
                        if (merchants?.user_id == SPUtils.getInt("user_id")) {
                            showToast("用户不能和自己聊天!")
                            return
                        }

                        val merchantName = if (merchants?.merchants_name.isNullOrEmpty()) merchants?.user_id.toString() else merchants?.merchants_name
                        val json = "{\"goods_img\": \"${goods?.goods_img}\",\"goods_amount\": \"${goods?.goods_amount}\",\"goods_name\":\"${goods?.goods_name}\", \"goods_id\":\"$mGoodsId\", \"type\":\"1\", \"goods_type\":\"1\"}"
                        startActivity<OnlineCustomerActivity>("title" to merchantName, "user_id" to merchants?.user_id, "data" to json)
                    }
                } else {
                    startActivity<ReleaseGoodsActivity>()
                }
            }
            R.id.tv_pay -> {
                if (mSource == 0) {
                    mBean?.data?.goods?.run {
                        if (is_restriction == 1) {
                            val goodsName = goods_name ?: ""
                            val dialog = AddGoodsCountDialog.newInstance(goods_img, goodsName, goods_amount, inventory)
                            dialog.setOnDialogCallback(this@GoodsDetailActivity)
                            dialog.show(supportFragmentManager, "addGoodsCount")
                        } else {
                            val shopName = mBean?.data?.merchants?.merchants_name
                            startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE, "goods_name" to goods_name,
                                    "goods_img" to goods_img, "price" to goods_amount, "goods_id" to mGoodsId, "from" to "general",
                                    "goods_freight" to goods_freight, "count" to 1, "shop_name" to shopName, "is_seven" to (is_seven == 0))
                        }
                    }
                } else {
                    if (null == mBean) {
                        showToast(getString(R.string.network_error))
                        return
                    }
                    val dialog = GoodsShareDialog.newInstance(mBean!!)
                    dialog.show(supportFragmentManager, "goodsShare")
                }
            }
        }
    }

    private fun startAnim(position: Int) {
        val startX = mLastPosition * mItemWidth + mOffset
        val endX = position * mItemWidth + mOffset
        val translate = TranslateAnimation(startX, endX, 0f, 0f)
        translate.duration = 200
        translate.fillAfter = true
        view_line.startAnimation(translate)
        mLastPosition = position
    }

    override fun getGoodsDetailSuccess(bean: GoodsDetailBean) {
        mBean = bean

        bean.data?.goods?.run {
            if (state != 0 && mSource == 0) {
                showToast("该商品不存在")
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                /* Banner */
                mBannerList.clear()
                if (!goods_video.isNullOrEmpty()) {
                    mBannerList.add(goods_video!!)
                }
                if (!goods_img.isNullOrEmpty()) {
                    if (goods_img!!.contains(",")) {
                        goods_img!!.split(",").forEach {
                            mBannerList.add(it)
                        }
                    } else {
                        mBannerList.add(goods_img!!)
                    }
                }
                banner.update(mBannerList)

                tv_goods_name.text = goods_name
                tv_goods_price.text = SpanUtils()
                        .append("¥ ").setFontSize(15, true)
                        .append(FormatUtils.formatNumber(goods_amount / 100f)).setBold()
                        .create()
                tv_stock.text = "库存剩余：${inventory}件"
                if (goods_original_amount != 0L) {
                    tv_origin_price.text = "¥" + FormatUtils.formatNumber(goods_original_amount / 100f)
                    tv_origin_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    gone(tv_origin_price)
                }
                if (goods_freight == 0) {
                    tv_free_shipping.text = "全国包邮"
                } else {
                    tv_free_shipping.text = "邮费5元"
                }
                if (is_seven == 0) {
                    visible(tv_seven)
                } else {
                    gone(tv_seven)
                }

                val merchantId = SPUtils.getInt("merchant_id")
                if (merchantId != 0) {
                    visible(tv_profit)
                    tv_profit.text = "赚 ¥${FormatUtils.formatNumber(goods_amount / 100.0 / 100 * 9.5)}"
                } else {
                    gone(tv_profit)
                }

                /* 更新收藏状态 */
                updateCollectStateSuccess(collection_state)
                tv_goods_desc.text = goods_describe

                val list: MutableList<Any> = if (goods_video.isNullOrEmpty()) {
                    mBannerList.toMutableList()
                } else {
                    mBannerList.subList(1, mBannerList.size).toMutableList()
                }
                mAdapter.setNewData(list)
                computeHideHeight()

                /* 秒杀倒计时 */
                if (goods_type == 2) {
                    if (service_time < end_time_timestamp) {
                        val timestamp = end_time_timestamp - service_time
                        mCountDownTimer = object : CountDownTimer(timestamp, 1000) {
                            override fun onFinish() {
                                tv_count_down.text = ""
                                tv_pay.isEnabled = false
                            }

                            override fun onTick(millisUntilFinished: Long) {
                                val millis2String = if (millisUntilFinished < 24 * 60 * 60 * 1000) {
                                    TimeUtils.millis2String(millisUntilFinished - TimeZone.getDefault().rawOffset, "HH时mm分ss秒")
                                } else {
                                    TimeUtils.millis2String(millisUntilFinished - TimeZone.getDefault().rawOffset, "dd天HH时mm分")
                                }
                                tv_count_down.text = "限时秒杀：$millis2String"
                            }
                        }
                        mCountDownTimer?.start()
                    } else {
                        tv_count_down.text = ""
                        tv_pay.isEnabled = false
                    }
                }

                /* 商品基本信息 */
                val titleList = listOf("尺寸", "材质", "产地", "重量")
                val descList = listOf(measure, texture, place, weight)
                titleList.forEachIndexed { index, s ->
                    val arrayMap = ArrayMap<String, String>()
                    arrayMap["title"] = s
                    arrayMap["desc"] = descList[index]
                    mGoodsInfoList.add(arrayMap)
                }
                mGoodsInfoAdapter.setNewData(mGoodsInfoList)

                /* 足迹 -- insert database */
                mPresenter?.insertFootprint(goods_id, if (goods_video.isNullOrEmpty()) mBannerList[0] else mBannerList[1], goods_amount)
            }
            bean.data?.merchants?.run {
                /* 店铺信息 */
                val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
//                iv_cover.loadImgAll(head_img, R.drawable.ic_merchant_header_default, transform)
//                tv_name.text = if (merchants_name.isNullOrEmpty()) "店主比较懒，还未设置名字~" else merchants_name
//                tv_desc.text = if (merchants_subtitle.isNullOrEmpty()) "店主比较懒，还未设置介绍~" else merchants_subtitle
//                if (quality_balance > 0) {
//                    visible(iv_vip)
//                } else {
//                    invisible(iv_vip)
//                }

//                tv_shop_warranty.text = SpanUtils()
//                        .appendLine(FormatUtils.formatNumber(quality_balance / 100f))
//                        .setFontSize(18, true).setBold().setForegroundColor(Color.BLACK)
//                        .append("质保金")
//                        .create()
//                tv_shop_impression.text = SpanUtils()
//                        .appendLine(merchants_grade ?: "0")
//                        .setFontSize(18, true).setBold().setForegroundColor(Color.BLACK)
//                        .append("店铺印象")
//                        .create()
//                tv_shop_attention_count.text = SpanUtils()
//                        .appendLine(focus_size.toString())
//                        .setFontSize(18, true).setBold().setForegroundColor(Color.BLACK)
//                        .append("关注")
//                        .create()
            }
        }
    }

    private fun computeHideHeight() {
        val list = mAdapter.data
        mHideHeight = if (list.size > 2) {
            getDimensionPixelSize(R.dimen.dp_360) * (list.size - 2)
        } else {
            -(mTitleBarHeight + mStickyHeaderHeight)
        }
    }

    override fun updateCollectStateSuccess(collectState: Int) {
        mCollectState = collectState
        tv_collect.isSelected = collectState == 0
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                mBean?.data?.goods?.run {
                    if (!goods_img.isNullOrEmpty()) {
                        val goodsImg = when {
                            goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                            else -> goods_img
                        }
                        Glide.with(applicationContext).asBitmap().load(goodsImg).into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", "/pages/goodDetail/goodDetail?id=${mGoodsId}&inviteCode=${SPUtils.getInt("user_id")}",
                                        goods_name!!, goods_describe!!, resource)
                            }
                        })
                    }
                }
            } else if (obj == "shareFriendCircle") {
                mBean?.data?.goods?.run {
                    if (!goods_img.isNullOrEmpty()) {
                        val goodsImg = when {
                            goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                            else -> goods_img
                        }
                        Glide.with(applicationContext).asBitmap().load(goodsImg).into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val headImg = SPUtils.getString("head_img")
                                val nickName = SPUtils.getString("nick_name")
                                val shareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
                                WXOptionUtils.share(mWXAPI, shareUrl, goods_name!!, goods_describe!!, resource, true)
                            }
                        })
                    }
                }
            }
        } else if (obj is Int) {
            mBean?.data?.goods?.run {
                val shopName = mBean?.data?.merchants?.merchants_name
                startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE, "goods_name" to goods_name,
                        "goods_img" to goods_img, "price" to goods_amount, "goods_id" to mGoodsId, "from" to "general",
                        "goods_freight" to goods_freight, "count" to obj, "shop_name" to shopName, "is_seven" to (is_seven == 0))
            }
        }
    }

    private fun getAccessToken() {
        val params = arrayOf<Pair<String, Any>>("grant_type" to "client_credential",
                "appid" to "wx90b3ac2231df8c82", "secret" to "1433bf1d2c8c8ff965a4739d0634c7d6")
        OkHttpUtils.get("https://api.weixin.qq.com/cgi-bin/token", params, {
            val jsonObject = JSONObject(it)
            if (jsonObject.has("access_token")) {
                val accessToken = jsonObject.getString("access_token")
                getQRCode(accessToken)
            }
        }, {
            ToastUtils.showToast(it)
        })
    }

    private fun getQRCode(accessToken: String) {
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val json = "{\"width\":430,\"path\":\"/pages/goodDetail/goodDetail?id=${mGoodsId}\",\"scene\":\"pages\"}"
        val requestBody = RequestBody.create(mediaType, json)

        val request = Request.Builder()
                .url("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=$accessToken")
                .post(requestBody)
                .build()

        val call = OkHttpUtils.okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    ToastUtils.showToast(e.message ?: "未知错误")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()?.byteStream()
                val drawable = Drawable.createFromStream(inputStream, "wechatqrcode")
                runOnUiThread {
                    val dialog = GoodsShareDialog.newInstance(mBean!!)
                    dialog.setDrawable(drawable)
                    dialog.show(supportFragmentManager, "goodsShare")
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "refreshTakeOff") {
            finish()
        }
    }

}
