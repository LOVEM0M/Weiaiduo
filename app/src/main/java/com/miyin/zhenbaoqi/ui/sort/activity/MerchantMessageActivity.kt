@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.ui.sort.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.util.ArrayMap
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.MerchantInfoBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.SeeMerchantEvalBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.live.activity.PullLiveActivity
import com.miyin.zhenbaoqi.ui.shop.dialog.ShopShareDialog
import com.miyin.zhenbaoqi.ui.shop.fragment.ShopDetailFragment
import com.miyin.zhenbaoqi.ui.sort.contract.MerchantMessageContract
import com.miyin.zhenbaoqi.ui.sort.presenter.MerchantMessagePresenter
import com.miyin.zhenbaoqi.utils.*
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_merchant_message.*
import kotlinx.android.synthetic.main.layout_shop_info.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat
import kotlin.math.abs

@SuppressLint("SetTextI18n")
class MerchantMessageActivity : BaseMvpActivity<MerchantMessageContract.IView, MerchantMessageContract.IPresenter>(), MerchantMessageContract.IView, OnDialogCallback {

    private var mMerchantId = 0
    private var mMerchantInfoBean: MerchantInfoBean.DataBean? = null
    private var mMerchantState = 1
    private lateinit var mWXAPI: IWXAPI
    private var mLiveRoomBean: MerchantInfoBean.DataBean.LiveRoomBean? = null

    override fun useEventBus() = true

    override fun getContentView(): Int {
        mMerchantId = intent.getIntExtra("merchant_id", 0)
        return R.layout.activity_merchant_message
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("店铺信息", rightTitle = "分享")
        immersionBar { statusBarDarkFont(true) }
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)

        setOnClickListener(cl_shop_info, tv_shop_attention, cl_container)

        ll_container.background = DrawableCreator.Builder()
                .setSolidColor(Color.WHITE)
                .setCornersRadius(0f, 0f, getDimension(R.dimen.dp_5), getDimension(R.dimen.dp_5))
                .build()

        val titleList = listOf(/*"拍卖", */"一口价")
        val fragmentList = listOf(/*ShopDetailFragment.newInstance(mMerchantId, 1), */ShopDetailFragment.newInstance(mMerchantId, 0))
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offsetVertical ->
            if (abs(offsetVertical) != 0) {
                if (abs(offsetVertical) == appBarLayout.totalScrollRange) {
                    view_pager.setCanScroll(true)
                } else {
                    view_pager.setCanScroll(false)
                }
//                swipe_refresh_layout.isEnabled = false
            } else if (offsetVertical == 0) {
//                swipe_refresh_layout.isEnabled = true
                view_pager.setCanScroll(false)
            }
        })

        swipe_refresh_layout.isEnabled = false
    }

    override fun initData() {
        mPresenter?.getMerchantInfo(mMerchantId)
    }

    override fun createPresenter() = MerchantMessagePresenter()

    override fun onRightClick() {
        mMerchantInfoBean?.let {
            val dialog = ShopShareDialog.newInstance(it, mMerchantId)
            dialog.show(supportFragmentManager, "shareShop")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_shop_info -> {
                if (null == mMerchantInfoBean) {
                    showToast("暂无店铺信息")
                    return
                }
                startActivity<MerchantAuthActivity>("bean" to mMerchantInfoBean, "merchant_id" to mMerchantId,
                        "merchant_state" to mMerchantState)
            }
            R.id.tv_shop_attention -> mPresenter?.updateMerchantState(mMerchantState, mMerchantId)
            R.id.cl_container -> {
                mLiveRoomBean?.run {
                    mPresenter?.liveRoomEntry(room_id)
                }
            }
        }
    }

    override fun getMerchantInfoSuccess(bean: ResponseBean) {
        if (bean is MerchantInfoBean) {
            bean.data?.run {
                mMerchantInfoBean = this

                iv_background.placeholder(merchants_back, R.drawable.ic_merchant_bg)
                iv_cover.loadImgAll(head_img, R.drawable.ic_merchant_header_default, RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL))
                tv_name.text = if (merchants_name.isNullOrEmpty()) "店主比较懒，还未设置名字~" else merchants_name
                tv_desc.text = if (merchants_subtitle.isNullOrEmpty()) "店主比较懒，还未设置介绍~" else merchants_subtitle
                if (quality_retention_money > 0) {
                    visible(iv_vip)
                } else {
                    gone(iv_vip)
                }

                if (null == live_room) {
                    gone(cl_container, tv_live_open)
                    visible(tv_live_close, fl_live_container)
                } else {
                    visible(cl_container, tv_live_open, fl_live_container)
                    gone(tv_live_close)
                    mLiveRoomBean = live_room

                    live_room?.run {
                        val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
                        iv_live_cover.transform(face_url, transform)

                        tv_live_count.text = "${popularity}人观看"
                        tv_live_title.text = room_name
                    }
                }

                tv_shop_warranty.text = SpanUtils()
                        .appendLine(FormatUtils.formatNumber(quality_retention_money  )).setForegroundColor(Color.BLACK)
                        .setFontSize(20, true)
                        .appendLine("质保金")
                        .create()
                tv_shop_impression.text = SpanUtils()
                        .appendLine(merchants_grade.toString()).setForegroundColor(Color.BLACK)
                        .setFontSize(20, true)
                        .appendLine("店铺评分")
                        .create()
                val attentionCount = if (focus_number > 10000) {
                    val decimalFormat = DecimalFormat("0.0")
                    decimalFormat.format(focus_number / 10000) + "w"
                } else {
                    focus_number.toString()
                }
                tv_shop_attention_count.text = SpanUtils()
                        .appendLine(attentionCount).setForegroundColor(Color.BLACK)
                        .setFontSize(20, true)
                        .appendLine("关注")
                        .create()

                mMerchantState = focus_state
                updateMerchantStateSuccess(focus_state)
            }
        } else if (bean is SeeMerchantEvalBean) {
            bean.data?.let {
                tv_shop_evaluate.text = "${it.size}条"

                if (it.isEmpty()) {
                    gone(flow_layout)
                    visible(tv_empty)
                } else {
                    gone(tv_empty)
                    visible(flow_layout)

                    val list = if (it.size > 6) {
                        it.subList(0, 6)
                    } else {
                        it
                    }
                    flow_layout.setAdapter(object : TagAdapter(list) {
                        override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                            layoutParams = FrameLayout.LayoutParams(context.getDimensionPixelSize(R.dimen.dp_110), context.getDimensionPixelSize(R.dimen.dp_25)).apply {
                                leftMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                                topMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                            }
                            gravity = Gravity.CENTER
                            setTextColor(0xFFC33A3A.toInt())
                            text = (data as SeeMerchantEvalBean.DataBean).evaluation_label
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_13))
                            background = DrawableCreator.Builder()
                                    .setSolidColor(0xFFF9E7E3.toInt())
                                    .setCornersRadius(context.getDimension(R.dimen.dp_3))
                                    .build()
                        }
                    })
                }
            }
        }
    }

    override fun updateMerchantStateSuccess(focusState: Int) {
        mMerchantState = focusState
        if (focusState == 0) {
            tv_shop_attention.text = "已关注"
            tv_shop_attention.isSelected = true
        } else {
            tv_shop_attention.text = "+ 关注"
            tv_shop_attention.isSelected = false
        }
    }

    override fun liveRoomEntrySuccess(bean: LiveEntryRoomBean) {
        mLiveRoomBean?.run {
            startActivity<PullLiveActivity>("room_id" to room_id, "play_url" to play_url, "cover_url" to face_url,
                    "name" to room_name, "icon_url" to icon_url, "fans_count" to fans_no, "is_focus" to (mMerchantState == 0),
                    "merchant_id" to merchants_id, "user_id" to user_id, "account_name" to bean.data?.accuount_name, "position" to -1)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(arrayMap: ArrayMap<String, Any>) {
        if (arrayMap["title"] == "update_merchant_state") {
            mMerchantState = arrayMap["merchant_state"].toString().toInt()
            updateMerchantStateSuccess(mMerchantState)
        } else if (arrayMap.containsKey("type")) {
            val type = arrayMap["type"].toString()
            if (type == "refreshLiveList") {
                val position = arrayMap["position"].toString().toDouble().toInt()
                if (position != -1) {
                    updateMerchantStateSuccess(0)
                }
            }
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "shareFriend") {
                Glide.with(applicationContext).asBitmap().load(mMerchantInfoBean?.merchants_back).into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val merchantName = if (mMerchantInfoBean?.merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mMerchantInfoBean?.merchants_name!!
                        val merchantDesc = if (mMerchantInfoBean?.merchants_subtitle.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else mMerchantInfoBean?.merchants_subtitle!!
                        WXOptionUtils.openProgram(mWXAPI, "gh_e93b10fb159e", "/pages/store/store?id=${mMerchantId}&inviteCode=${SPUtils.getInt("user_id")}",
                                merchantName, merchantDesc, resource)
                    }
                })
            }
        }
    }
}
