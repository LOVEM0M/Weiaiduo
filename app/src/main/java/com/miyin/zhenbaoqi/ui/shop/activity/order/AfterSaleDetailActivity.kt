package com.miyin.zhenbaoqi.ui.shop.activity.order

import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.AfterSaleDetailBean
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.MaxImageView
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_after_sale_detail.*

class AfterSaleDetailActivity : BaseActivity() {

    private var mOrderNumber: String? = null
    private var mOrderTime: String? = null
    private var mPayTime: String? = null
    private var mName: String? = null
    private var mPhone: String? = null
    private var mBean: AfterSaleDetailBean? = null

    override fun getContentView(): Int {
        with(intent) {
            mOrderNumber = getStringExtra("order_number")
            mOrderTime = getStringExtra("order_time")
            mPayTime = getStringExtra("pay_time")
            mName = getStringExtra("name")
            mPhone = getStringExtra("phone")
            mBean = getSerializableExtra("bean") as AfterSaleDetailBean
        }
        return R.layout.activity_after_sale_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("售后记录")
        immersionBar { statusBarDarkFont(true) }

        SpanUtils.with(tv_order_num)
                .append("$mOrderNumber\u3000")
                .append("复制编号")
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        copyMsg(mOrderNumber ?: "")
                        ToastUtils.showToast("复制成功")
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.run {
                            isUnderlineText = false
                            color = 0xFF2B92E2.toInt()
                        }
                    }
                })
                .create()
        tv_order_time.text = mOrderTime
        tv_pay_time.text = mPayTime

        tv_contact.text = mName
        tv_phone.text = mPhone

        mBean?.run {
            tv_type.text = if (after_type == 1) {
                "退货退款"
            } else {
                "仅退款"
            }
            tv_refund_reason.text = after_why
            tv_progress.text = when (after_state) {
                1 -> "等待商家审核"
                2 -> "商家同意"
                3 -> "卖家第一次拒绝"
                4 -> "等待卖家签收"
                5 -> "卖家第二次拒绝(售后失败,订单完成)"
                6 -> "已完成"
                7 -> "拒绝签收(售后失败)"
                8 -> "已取消"
                else -> ""
            }
            tv_date_time.text = add_time

            if (!after_img.isNullOrEmpty()) {
                var list = mutableListOf<String>()
                if (after_img!!.contains(",")) {
                    list = after_img!!.split(",").toMutableList()
                    list.forEachIndexed { index, s ->
                        when (index) {
                            0 -> iv_left.loadImg(s)
                            1 -> iv_middle.loadImg(s)
                            2 -> iv_right.loadImg(s)
                        }
                    }
                } else {
                    list.add(after_img!!)
                    iv_left.loadImg(after_img)
                }

                iv_left.setOnClickListener {
                    MaxImageView.maxImageView(this@AfterSaleDetailActivity, list, 0)
                }
                iv_middle.setOnClickListener {
                    MaxImageView.maxImageView(this@AfterSaleDetailActivity, list, 1)
                }
                iv_right.setOnClickListener {
                    MaxImageView.maxImageView(this@AfterSaleDetailActivity, list, 2)
                }

            }
        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        MaxImageView.clear()
        super.onDestroy()
    }

}
