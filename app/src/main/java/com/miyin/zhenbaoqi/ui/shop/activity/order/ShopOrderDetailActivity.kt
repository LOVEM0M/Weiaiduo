package com.miyin.zhenbaoqi.ui.shop.activity.order

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.bean.MerchantOrderDetailBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.mine.activity.order.ExpressDeliveryActivity
import com.miyin.zhenbaoqi.ui.mine.activity.order.LogisticsActivity
import com.miyin.zhenbaoqi.ui.shop.contract.ShopOrderDetailContract
import com.miyin.zhenbaoqi.ui.shop.dialog.RefuseDialog
import com.miyin.zhenbaoqi.ui.shop.presenter.ShopOrderDetailPresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.miyin.zhenbaoqi.widget.flow_layout.TagFlowLayout
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_shop_order_detail.*

@SuppressLint("SetTextI18n")
class ShopOrderDetailActivity : BaseMvpActivity<ShopOrderDetailContract.IView, ShopOrderDetailContract.IPresenter>(),
        ShopOrderDetailContract.IView, OnDialogCallback {

    private var mOrderNumber: String? = null
    private var mHeadImg: String? = null
    private var mRemark: String? = null
    private var mBean: MerchantOrderDetailBean? = null
    private var mAfterSaleBean: AfterSaleDetailBean? = null
    private var mUserInfo: String? = null
    private var mAddress: String? = null

    override fun getContentView(): Int {
        mHeadImg = intent.getStringExtra("avatar")
        mOrderNumber = intent.getStringExtra("order_number")
        mRemark = intent.getStringExtra("remark")
        return R.layout.activity_shop_order_detail
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("订单详情")
        immersionBar { statusBarDarkFont(true) }

        tv_remark.text = mRemark
        tv_copy_order_num.setOnClickListener {
            copyMsg(mOrderNumber ?: "")
            showToast("复制成功")
        }
        tv_copy.setOnClickListener {
            copyMsg("${mBean?.consignee} ${mBean?.phone_no} ${mBean?.address}")
            showToast("复制成功")
        }
    }

    override fun initData() {
        mPresenter?.getOrderDetail(mOrderNumber)
    }

    override fun createPresenter() = ShopOrderDetailPresenter()

    override fun getOrderDetailSuccess(bean: MerchantOrderDetailBean) {
        mBean = bean
        with(bean) {
            mUserInfo = "$consignee\u3000$phone_no"
            mAddress = address

            tv_status.text = when (state) {
                1 -> "待支付"
                2 -> "待发货"
                3 -> "已发货"
                4 -> "已签收"
                5 -> "已评价"
                6 -> "售后中"
                7 -> "已退款"
                8 -> "退款失败"
                9 -> "已关闭"
                else -> ""
            }
            tv_status_tip.text = when (state) {
                1 -> "待用户付款"
                2 -> "待商家发货"
                3 -> ""
                4 -> "买家已签收"
                5 -> ""
                6 -> ""
                7 -> ""
                8 -> "请联系官方客服处理"
                9 -> "已关闭"
                else -> ""
            }

            iv_avatar.loadImg(mHeadImg)
            tv_name.text = consignee
            tv_level.text = "V0青铜"

            tv_user_info.text = SpanUtils()
                    .append("收  货  人：").setForegroundColor(ContextCompat.getColor(applicationContext, R.color.text_99))
                    .append("$consignee\u3000$phone_no")
                    .create()
            tv_address.text = "$address"

            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            iv_cover.loadImg(goodsImg)
            tv_goods_name.text = goods_name
            tv_price.text = "¥${FormatUtils.formatNumber(order_amount  )}"
            tv_count.text = "数量x$pay_number"

            tv_goods_price.text = "¥${FormatUtils.formatNumber(order_amount * pay_number  )}"
            tv_coupon_price.text = "¥${FormatUtils.formatNumber(coupons_amount  )}"
            tv_pay_price.text = "¥${FormatUtils.formatNumber(pay_amount  )}"

            tv_order_num.text = order_number
            tv_order_time.text = TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm:ss")
            tv_pay_time.text = TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm:ss")

            flow_layout.run {
                val list = when (state) {
                    2 -> listOf("立即发货")
                    3 -> listOf("查看物流")
                    4 -> listOf("查看物流")
                    else -> listOf()
                }
                setAdapter(object : TagAdapter(list) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = getTextView(state, data.toString())
                })
                setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
                    override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                        when (state) {
                            2 -> {
                                startActivity<ExpressDeliveryActivity>("address" to address,
                                        "user_info" to mUserInfo, "source" to "merchant",
                                        "order_number" to order_number)
                                finish()
                            }
                            3 -> {
                                startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                        "order_number" to mOrderNumber, "source" to "shop")
                            }
                            4 -> {
                                startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo, "order_number" to mOrderNumber)
                            }
                            5 -> {
                            }
                            6 -> {
                                when (position) {
                                    0 -> mPresenter?.orderAfterAudit(3, mOrderNumber, "")
                                    1 -> {
                                        val dialog = RefuseDialog.newInstance()
                                        dialog.show(supportFragmentManager, "refuse")
                                    }
                                    2 -> {
                                        mBean.run {
                                            startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                    "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                    "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"))
                                        }
                                    }
                                    3 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                            "order_number" to mOrderNumber, "source" to "shop")
                                }
                            }
                            7 -> {
                                when (position) {
                                    0 -> mPresenter?.orderAfterReceive(6, mOrderNumber)
                                    1 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                            "order_number" to mOrderNumber, "type" to 1)
                                    2 -> {
                                        mBean.run {
                                            startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                    "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                    "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"))
                                        }
                                    }
                                    3 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                            "order_number" to mOrderNumber, "source" to "shop")
                                    4 -> mPresenter?.orderAfterReceive(7, mOrderNumber)
                                }
                            }
                            else -> {
                            }
                        }
                    }
                })
            }

            if (state == 6) {
                mPresenter?.afterSaleDetail(mOrderNumber)
            }
        }
    }

    override fun afterSaleDetailSuccess(bean: AfterSaleDetailBean) {
        mAfterSaleBean = bean

        mBean?.run {
            flow_layout.run {
                val list = when (state) {
                    6 -> {
                        when {
                            bean.after_state == 1 -> when {
                                bean.after_type == 1 -> listOf("确认退货退款", "拒绝退货退款", "售后记录", "查看物流")
                                bean.after_type == 2 -> listOf("同意退款", "拒绝退款", "售后记录", "查看物流")
                                else -> listOf()
                            }
                            bean.after_state == 4 -> {
                                when {
                                    bean.after_type == 1 -> listOf("签收", "售后记录", "查看退货物流", "查看物流", "拒绝签收")
                                    bean.after_type == 2 -> listOf("确认", "售后记录", "查看物流")
                                    else -> listOf()
                                }
                            }
                            else -> listOf()
                        }
                    }
                    else -> listOf()
                }
                setAdapter(object : TagAdapter(list) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = getTextView(state, data.toString())
                })
                setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
                    override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                        when (state) {
                            6 -> {
                                if (bean.after_state == 1) {
                                    if (bean.after_type == 1) {
                                        when (position) {
                                            0 -> mPresenter?.orderAfterAudit(2, mOrderNumber, "")
                                            1 -> {
                                                val dialog = RefuseDialog.newInstance()
                                                dialog.show(supportFragmentManager, "refuse")
                                            }
                                            2 -> {
                                                mBean.run {
                                                    startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                            "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "bean" to mAfterSaleBean)
                                                }
                                            }
                                            3 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo, "order_number" to mOrderNumber)
                                        }
                                    } else if (bean.after_type == 2) {
                                        when (position) {
                                            0 -> mPresenter?.merchantAgreeFund(mOrderNumber)
                                            1 -> {
                                                val dialog = RefuseDialog.newInstance()
                                                dialog.show(supportFragmentManager, "refuse")
                                            }
                                            2 -> {
                                                mBean.run {
                                                    startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                            "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "bean" to mAfterSaleBean)
                                                }
                                            }
                                            3 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo, "order_number" to mOrderNumber)
                                        }
                                    }
                                } else if (bean.after_state == 4) {
                                    if (bean.after_type == 1) {
                                        when (position) {
                                            0 -> mPresenter?.orderAfterReceive(6, mOrderNumber)
                                            1 -> {
                                                mBean.run {
                                                    startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                            "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "bean" to mAfterSaleBean)
                                                }
                                            }
                                            2 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                                    "order_number" to mOrderNumber, "type" to 1)
                                            3 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo, "order_number" to mOrderNumber)
                                            4 -> mPresenter?.orderAfterReceive(7, mOrderNumber)
                                        }
                                    } else if (bean.after_type == 2) {
                                        when (position) {
                                            0 -> mPresenter?.merchantAgreeFund(mOrderNumber)
                                            1 -> {
                                                mBean.run {
                                                    startActivity<AfterSaleDetailActivity>("order_number" to mOrderNumber, "name" to consignee, "phone" to phone_no,
                                                            "order_time" to TimeUtils.millis2String(order_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "pay_time" to TimeUtils.millis2String(pay_time!!.toLong(), "yyyy-MM-dd HH:mm"),
                                                            "bean" to mAfterSaleBean)
                                                }
                                            }
                                            2 -> startActivity<LogisticsActivity>("address" to address, "user_info" to mUserInfo,
                                                    "order_number" to mOrderNumber, "type" to 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    override fun orderAfterAuditSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun orderAfterReceiveSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun merchantAgreeFundSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getTextView(status: Int, title: String) = TextView(this).apply {
        layoutParams = FrameLayout.LayoutParams(context.getDimensionPixelSize(R.dimen.dp_75), context.getDimensionPixelSize(R.dimen.dp_28)).apply {
            gravity = Gravity.CENTER_VERTICAL
            rightMargin = context.getDimensionPixelSize(R.dimen.dp_10)
            bottomMargin = context.getDimensionPixelSize(R.dimen.dp_10)
        }

        gravity = Gravity.CENTER
        text = title
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_11))

        background = if (status != 2) {
            setTextColor(0xFFFF4444.toInt())
            DrawableCreator.Builder()
                    .setCornersRadius(context.getDimension(R.dimen.dp_28))
                    .setStrokeWidth(context.getDimension(R.dimen.dp_1))
                    .setStrokeColor(0xFFFF363A.toInt())
                    .build()
        } else {
            setTextColor(Color.WHITE)
            DrawableCreator.Builder()
                    .setCornersRadius(context.getDimension(R.dimen.dp_28))
                    .setSolidColor(0xFFFF4444.toInt())
                    .build()
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            mPresenter?.orderAfterAudit(3, mOrderNumber, obj)
        }
    }

}
