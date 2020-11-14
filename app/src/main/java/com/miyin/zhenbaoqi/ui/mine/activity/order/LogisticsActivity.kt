package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseStateActivity
import com.miyin.zhenbaoqi.bean.OrderLogisticsBean
import com.miyin.zhenbaoqi.bean.ThridDayBean
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.LogisticsAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.LogisticsContract
import com.miyin.zhenbaoqi.ui.mine.presenter.LogisticsPresenter
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_logistics.*

class LogisticsActivity : BaseStateActivity<LogisticsContract.IView, LogisticsContract.IPresenter>(), LogisticsContract.IView {

    private var mOrderNumber: String? = null
    private var mList = mutableListOf<ThridDayBean.DataBean>()
    private lateinit var mAdapter: LogisticsAdapter
    private var mType = 0
    private var mSource: String? = null
    private var mUserInfo: String? = null
    private var mAddress: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mOrderNumber = getStringExtra("order_number")
            mType = getIntExtra("type", 0)
            mSource = getStringExtra("source")
            mUserInfo = getStringExtra("user_info")
            mAddress = getStringExtra("address")
        }
        return R.layout.activity_logistics
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("物流跟踪")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        recycler_view.run {
            mAdapter = LogisticsAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        if (mType == 1) {
            tv_tip.text = "用户寄货至商家"
        } else {
            tv_tip.text = "商家寄货至用户"
        }
    }

    override fun initData() {
        if (mType == 1) {
            mPresenter?.getOrderAfterLogistics(mOrderNumber ?: "")
        } else {
            mPresenter?.getOrderLogistics(mOrderNumber ?: "")
        }
    }

    override fun createPresenter() = LogisticsPresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    override fun getOrderLogisticsSuccess(bean: OrderLogisticsBean) {
        with(bean) {
            tv_company.text = SpanUtils()
                    .append("物流公司")
                    .appendSpace(DensityUtils.dp2px(17f))
                    .append(courier_name ?: "").setForegroundColor(0xFF338F24.toInt())
                    .create()
            Logger.d("type == $mType")
            if (mType == 0 && mSource == "shop") {
                SpanUtils.with(tv_logistics_no)
                        .append("快递单号")
                        .appendSpace(DensityUtils.dp2px(17f))
                        .append(courier_no ?: "").setForegroundColor(Color.BLACK)
                        .appendSpace(DensityUtils.dp2px(26f))
                        .append("复制")
                        .setClickSpan(object : ClickableSpan() {
                            override fun onClick(v: View) {
                                showToast("复制成功")
                                copyMsg(courier_no ?: "")
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                ds.run {
                                    isUnderlineText = false
                                    color = Color.parseColor("#2B92E2")
                                }
                            }
                        })
                        .appendSpace(DensityUtils.dp2px(26f))
                        .append("修改").setForegroundColor(0xFF2B92E2.toInt())
                        .setClickSpan(object : ClickableSpan() {
                            override fun onClick(v: View) {
                                startActivity<ExpressDeliveryActivity>("order_number" to mOrderNumber,
                                        "user_info" to mUserInfo, "address" to mAddress, "source" to "merchantUpdate")
                                finish()
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                ds.run {
                                    isUnderlineText = false
                                    color = Color.parseColor("#2B92E2")
                                }
                            }
                        })
                        .create()
            } else {
                SpanUtils.with(tv_logistics_no)
                        .append("快递单号")
                        .appendSpace(DensityUtils.dp2px(17f))
                        .append(courier_no ?: "").setForegroundColor(Color.BLACK)
                        .appendSpace(DensityUtils.dp2px(26f))
                        .append("复制")
                        .setClickSpan(object : ClickableSpan() {
                            override fun onClick(v: View) {
                                showToast("复制成功")
                                copyMsg(courier_no ?: "")
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                ds.run {
                                    isUnderlineText = false
                                    color = Color.TRANSPARENT
                                }
                            }
                        })
                        .create()
            }
            tv_logistics_no.highlightColor = Color.WHITE

            third_day?.let {
                val thirdDayBean = JSONUtils.fromJSON<ThridDayBean>(it)
                if (thirdDayBean.message == "ok") {
                    val list = thirdDayBean.data!!
                    mAdapter.setNewData(list)
                }
            }

        }
    }

}
