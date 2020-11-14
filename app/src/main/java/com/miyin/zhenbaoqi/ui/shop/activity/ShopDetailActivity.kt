package com.miyin.zhenbaoqi.ui.shop.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.MerchantInfoBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.SeeMerchantEvalBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.placeholder
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.fragment.ShopDetailFragment
import com.miyin.zhenbaoqi.ui.sort.contract.MerchantMessageContract
import com.miyin.zhenbaoqi.ui.sort.presenter.MerchantMessagePresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_shop_detail.*
import java.text.DecimalFormat
import kotlin.math.abs

class ShopDetailActivity : BaseMvpActivity<MerchantMessageContract.IView, MerchantMessageContract.IPresenter>(), MerchantMessageContract.IView {

    private var mTitle: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mTitle = getStringExtra("title")
        }
        return R.layout.activity_shop_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar(mTitle!!)
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("猜你喜欢", "即将截拍")
        val merchantId = SPUtils.getInt("merchant_id")
        val fragmentList = listOf(ShopDetailFragment.newInstance(merchantId, 0), ShopDetailFragment.newInstance(merchantId, 1))
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
    }

    override fun initData() {
        mPresenter?.getMerchantInfo(SPUtils.getInt("merchant_id"))
    }

    override fun createPresenter() = MerchantMessagePresenter()

    @SuppressLint("SetTextI18n")
    override fun getMerchantInfoSuccess(bean: ResponseBean) {
        if (bean is MerchantInfoBean) {
            bean.data?.run {
                iv_cover.placeholder(merchants_back, R.drawable.ic_merchant_bg)
                iv_avatar.placeholder(head_img, R.drawable.ic_merchant_header_default)
                tv_shop_name.text = merchants_name
                tv_desc.text = merchants_subtitle

                tv_warranty.text = SpanUtils()
                        .appendLine(FormatUtils.formatNumber(quality_retention_money / 100f)).setForegroundColor(Color.BLACK)
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
                tv_attention_count.text = SpanUtils()
                        .appendLine(attentionCount).setForegroundColor(Color.BLACK)
                        .setFontSize(20, true)
                        .appendLine("关注")
                        .create()
            }
        } else if (bean is SeeMerchantEvalBean) {
            bean.data?.run {
                tv_shop_evaluate.text = "${size}条"

                val list = if (size > 6) {
                    subList(0, 6)
                } else {
                    this
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

    override fun updateMerchantStateSuccess(focusState: Int) {

    }

    override fun liveRoomEntrySuccess(bean: LiveEntryRoomBean) {

    }

}
