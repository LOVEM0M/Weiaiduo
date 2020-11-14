package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.contract.AfterSaleStateContract
import com.miyin.zhenbaoqi.ui.mine.presenter.AfterSaleStatePresenter
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import kotlinx.android.synthetic.main.activity_after_sale_state.*

class AfterSaleStateActivity : BaseMvpActivity<AfterSaleStateContract.IView, AfterSaleStateContract.IPresenter>(), AfterSaleStateContract.IView {

    private var mOrderNumber: String? = null
    private var mState = 0
    private var mBean: AfterSaleDetailBean? = null

    override fun getContentView(): Int {
        mOrderNumber = intent.getStringExtra("order_number")
        return R.layout.activity_after_sale_state
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("申请售后")
        immersionBar { statusBarDarkFont(true) }

        btn_commit.setOnClickListener {
            if (mState == 2) {
                mBean?.run {
                    startActivity<ExpressDeliveryActivity>("order_number" to mOrderNumber,
                            "user_info" to "$consignee\u3000$phone_no", "address" to address, "source" to "afterSale")
                    finish()
                }
            } else if (mState == 3) {
                mBean?.run {
                    mPresenter?.afterSaleLaunch(after_img ?: "", after_type, after_why, mOrderNumber
                            ?: "")
                }
            }
        }
        tv_cancel.setOnClickListener {
            mPresenter?.orderAfterCancel(mOrderNumber)
        }
    }

    override fun initData() {
        mPresenter?.getAfterSaleDetail(mOrderNumber!!)
    }

    override fun createPresenter() = AfterSaleStatePresenter()

    @SuppressLint("SetTextI18n")
    override fun getAfterSaleDetailSuccess(bean: AfterSaleDetailBean) {
        mBean = bean
        with(bean) {
            mState = after_state

            tv_type.text = if (after_type == 1) {
                "退货退款"
            } else {
                "仅退款"
            }
            tv_after_sale_reason.text = after_why

            val afterType = if (after_type == 1) "退货退款" else "仅退款"
            tv_return_info.text = SpanUtils()
                    .appendLine("退款信息").setFontSize(14, true)
                    .setForegroundColor(ContextCompat.getColor(this@AfterSaleStateActivity, R.color.text_54))
                    .appendLine("退款类型：$afterType")
                    .appendLine("退款原因：$after_why")
                    .appendLine("退款方式：退回余额")
                    .append("原始订单号：$mOrderNumber")
                    .create()

            tv_name.text = consignee
            tv_phone.text = phone_no

            if (after_img.isNullOrEmpty()) {
                gone(flow_layout)
            } else {
                visible(flow_layout)
                val list = mutableListOf<String>()
                if (after_img!!.contains(",")) {
                    val array = after_img!!.split(",").toList()
                    list.addAll(array)
                } else {
                    list.add(after_img!!)
                }

                flow_layout.setAdapter(object : TagAdapter(list) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = ImageView(applicationContext).apply {
                        layoutParams = FrameLayout.LayoutParams(getDimensionPixelSize(R.dimen.dp_76), getDimensionPixelSize(R.dimen.dp_76)).apply {
                            leftMargin = getDimensionPixelSize(R.dimen.dp_15)
                        }
                        loadImg(data.toString())
                    }
                })
            }
            tv_status/*tv_feedback*/.text = when (after_state) {
                1 -> {
                    gone(tv_reason, fl_info, fl_result, btn_commit, btn_recommit)
                    visible(tv_cancel, fl_commit_apply)

                    SpanUtils().appendLine("等待卖家处理")
                            .append("您已成功发起退款申请，请耐心等待商家处理~").setFontSize(12, true)
                            .create()
//                    "等待卖家审核"
                }
                2 -> {
                    visible(btn_commit, tv_cancel, fl_commit_apply, btn_recommit)

                    SpanUtils().appendLine("退货地址：")
                            .append("您已成功发起退款申请，请耐心等待商家处理~").setFontSize(12, true)
                            .create()
//                    "卖家同意"
                }
                3 -> {
                    visible(tv_reason, btn_commit, btn_recommit)
                    gone(fl_info, fl_result)

                    tv_reason.text = refuse_reason
                    btn_commit.text = "重新申请售后"

                    SpanUtils().appendLine("卖家拒绝退款")
                            .append("请和卖家协商之后重新申请退款哦~").setFontSize(12, true)
                            .create()
//                    "卖家拒绝"
                }
                4 -> {
                    SpanUtils().appendLine("等待卖家收货")
                            .append("快递还在运输中请耐心等待一下哦~").setFontSize(12, true)
                            .create()
//                    "等待卖家签收"
                }
                5 -> {
                    tv_reason.text = refuse_reason

                    "售后已被卖家拒绝"
                }
                6 -> {
                    visible(fl_info)
                    gone(view_space, btn_commit)
                    tv_info.text = "$courier_name $courier_no"

                    SpanUtils().appendLine("退款成功")
                            .append("退款时间：2020-05-11 14:30").setFontSize(12, true) // TODO 时间没有
                            .create()
//                    "已完成"
                }
                7 -> "拒绝签收(售后失败)"
                8 -> "已取消"
                else -> ""
            }
        }
    }

    override fun orderAfterCancelSuccess() {
        finish()
    }

    override fun confirmShipSuccess() {
        finish()
    }

    override fun afterSaleLaunchSuccess() {
        finish()
    }

}
