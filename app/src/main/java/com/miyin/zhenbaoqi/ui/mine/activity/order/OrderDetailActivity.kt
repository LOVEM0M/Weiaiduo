package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BasePayActivity
import com.miyin.zhenbaoqi.bean.OrderDetailBean
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.contract.OrderDetailContract
import com.miyin.zhenbaoqi.ui.mine.dialog.OrderPayDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.OrderDetailPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_order_detail.*

@SuppressLint("SetTextI18n")
class OrderDetailActivity : BasePayActivity<OrderDetailContract.IView, OrderDetailContract.IPresenter>(), OrderDetailContract.IView, OnDialogCallback {

    private var mState = 1
    private var mOrderNumber: String? = null
    private var mBean: OrderDetailBean? = null
    private var mGoodsId = 2
    private var mPhone: String? = null
    private var mAddress: String? = null
    private var mConsignee: String? = null
    private var mRemark: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mState = getIntExtra("state", 1)
            mOrderNumber = getStringExtra("order_number")
            mRemark = getStringExtra("remark")
        }
        return R.layout.activity_order_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("订单详情")
        immersionBar { statusBarDarkFont(true) }

        tv_remark.text = mRemark
        when (mState) {
            1 -> {
                invisible(tv_left_title)

                tv_middle_title.text = "取消订单"
                tv_fix_right_title.text = "立即支付"
                tv_fix_right_title.isSelected = true
            }
            2 -> {
                invisible(tv_left_title, tv_middle_title)

                tv_fix_right_title.text = "提醒发货"
                tv_fix_right_title.isSelected = true
            }
            3 -> {
                invisible(tv_left_title)

                tv_middle_title.text = "查看物流"
                tv_fix_right_title.text = "确认收货"
            }
            4 -> {
                invisible(tv_left_title)

                tv_middle_title.text = "查看物流"
                tv_fix_right_title.text = "申请售后"
            }
            6 -> {
                invisible(tv_left_title, tv_middle_title)

                tv_fix_right_title.text = "售后详情"
            }
            else -> {
                invisible(tv_left_title, tv_middle_title, tv_fix_right_title, ll_container)
            }
        }

        setOnClickListener(cl_container, tv_copy_order_num, tv_left_title, tv_middle_title, tv_fix_right_title, iv_customer)
    }

    override fun initData() {
        mPresenter?.getOrderDetail(mOrderNumber ?: "")
    }

    override fun createPresenter() = OrderDetailPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_container -> startActivity<MerchantMessageActivity>("merchant_id" to mBean?.merchants_id)
            R.id.tv_copy_order_num -> {
                copyMsg(mOrderNumber ?: "")
                showToast("复制成功")
            }
            R.id.tv_left_title -> {

            }
            R.id.tv_middle_title -> {
                when (mState) {
                    1 -> {
                        mPresenter?.orderCancel(mOrderNumber!!)
                    }
                    3, 4 -> {
                        startActivity<LogisticsActivity>("order_number" to mBean?.order_number)
                    }
                }
            }
            R.id.tv_fix_right_title -> {
                when (mState) {
                    1 -> {
                        val dialog = OrderPayDialog.newInstance()
                        dialog.show(supportFragmentManager, "orderPay")
                    }
                    2 -> {
                        AlertDialog.Builder(this)
                                .setTitle("温馨提示")
                                .setMessage("提供商家发货已通知")
                                .setPositiveButton(getString(R.string.confirm), null)
                                .show()
                    }
                    3 -> mPresenter?.confirmReceive(mBean?.order_number ?: "")
                    4 -> {
                        mBean?.run {
                            startActivity<SelectAfterSaleTypeActivity>("name" to consignee, "phone" to phone_no, "order_number" to order_number)
                        }
                    }
                    6 -> startActivity<AfterSaleStateActivity>("order_number" to mBean?.order_number)
                }
            }
            R.id.iv_customer -> {
//                val intent = Intent(Intent.ACTION_DIAL)
//                val data = Uri.parse("tel:" + phoneNum)
//                intent.data = data
//                startActivity(intent)
            }
        }
    }

    override fun onAliPaySuccess() {

    }

    override fun onWXPaySuccess() {

    }

    override fun getOrderDetailSuccess(bean: OrderDetailBean) {
        mBean = bean
        with(bean) {
            mGoodsId = goods_id
            mConsignee = consignee
            mPhone = phone_no
            mAddress = address

            tv_status.text = when (mState) {
                1 -> "待支付"
                2 -> "待发货"
                3 -> "已发货"
                4 -> "已签收"
                5 -> "已评价"
                6 -> "退款中"
                7 -> "已退款"
                8 -> "退款失败"
                9 -> "已关闭"
                else -> "已关闭"
            }
            tv_user_info.text = consignee
            tv_user_phone.text = phone_no
            tv_address.text = "$address"

            tv_shop_name.text = merchants_name
            iv_cover.loadImg(if (goods_img!!.contains(",")) goods_img!!.split(",")[0] else goods_img)
            tv_goods_name.text = goods_name
            tv_price.text = "¥${FormatUtils.formatNumber(order_amount / 100f)}"
            tv_count.text = "x$pay_number"

            tv_label_second.text = if (courier_amount == 0) "包邮" else "邮费5元"

            tv_goods_price.text = "¥${FormatUtils.formatNumber(order_amount * pay_number / 100f)}"
            tv_freight_price.text = "¥${FormatUtils.formatNumber(courier_amount / 100f)}"
            tv_coupon_price.text = "¥${FormatUtils.formatNumber(coupons_amount / 100f)}"
            tv_pay_price.text = "¥${FormatUtils.formatNumber(pay_amount / 100f)}"

            tv_order_num.text = order_number
            tv_order_time.text = TimeUtils.millis2String(order_time!!.toLong())
            tv_pay_time.text = TimeUtils.millis2String(order_time!!.toLong())
        }
    }

    override fun orderCancelSuccess() {
        showToast("订单取消成功")

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun confirmReceiveSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun orderPaySuccess(bean: PayResultBean, payType: Int) {
        if (payType == 1) {

        } else if (payType == 2) {
            val orderString = bean.alipayBody
            if (orderString.isNullOrEmpty()) {
                showToast("支付宝错误")
                return
            }
            onAliCallback(orderString)
        } else if (payType == 3) {
//            onWXCallback(bean)
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        mPresenter?.orderPay(mOrderNumber!!, flag)
    }

}
