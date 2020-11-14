package com.miyin.zhenbaoqi.ui.shop.activity.security_deposit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BasePayActivity
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.contract.SecurityDepositContract
import com.miyin.zhenbaoqi.ui.shop.presenter.SecurityDepositPresenter
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.miyin.zhenbaoqi.widget.flow_layout.TagFlowLayout
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_security_deposit.*
import org.greenrobot.eventbus.EventBus

class SecurityDepositActivity : BasePayActivity<SecurityDepositContract.IView, SecurityDepositContract.IPresenter>(),
        SecurityDepositContract.IView {

    private var mPrice = "1000"
    private var mPayType = 3
    private var mHasMoney = false

    override fun getContentView(): Int {
        mHasMoney = intent.getBooleanExtra("has_money", false)
        return R.layout.activity_security_deposit
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("品质保证金提额")
        immersionBar { statusBarDarkFont(true) }

        iv_wx_pay.isSelected = true
        iv_ali_pay.isSelected = false

        flow_layout.run {
            val priceList = listOf("1000", "2000", "3000", "5000")
            val adapter = object : TagAdapter(priceList) {
                @SuppressLint("SetTextI18n")
                override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                    val params = FrameLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_70), resources.getDimensionPixelSize(R.dimen.dp_41)).apply {
                        rightMargin = resources.getDimensionPixelSize(R.dimen.dp_14)
                        topMargin = resources.getDimensionPixelSize(R.dimen.dp_14)
                        bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_14)
                    }
                    layoutParams = params
                    text = "${data}元"
                    gravity = Gravity.CENTER
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_14))
                    setTextColor(DrawableCreator.Builder()
                            .setCheckedTextColor(ContextCompat.getColor(context, R.color.white))
                            .setUnCheckedTextColor(ContextCompat.getColor(context, R.color.text_33))
                            .buildTextColor())
                    background = DrawableCreator.Builder()
                            .setCornersRadius(resources.getDimension(R.dimen.dp_2))
                            .setCheckedSolidColor(0xFFFF6855.toInt(), 0xFFF0F0F0.toInt())
                            .build()
                }
            }
            setAdapter(adapter)
            setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
                override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                    mPrice = priceList[position]
                    tv_add_price.text = SpanUtils()
                            .append("追加金额：")
                            .append("¥ $mPrice").setForegroundColor(0xFFFF6854.toInt())
                            .create()
                }
            })
        }

        tv_add_price.text = SpanUtils()
                .append("追加金额：")
                .append("¥ $mPrice").setForegroundColor(0xFFFF6854.toInt())
                .create()
        setOnClickListener(fl_wx_pay, iv_wx_pay, fl_ali_pay, fl_ali_pay, tv_pay)
    }

    override fun initData() {

    }

    override fun createPresenter() = SecurityDepositPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_wx_pay, R.id.iv_wx_pay -> {
                mPayType = 3
                iv_wx_pay.isSelected = true
                iv_ali_pay.isSelected = false
            }
            R.id.fl_ali_pay, R.id.iv_ali_pay -> {
                mPayType = 2
                iv_wx_pay.isSelected = false
                iv_ali_pay.isSelected = true
            }
            R.id.tv_pay -> {
                val price = (mPrice.toFloat() * 100).toLong()
                mPresenter?.addQualityMoney(price, mPayType)
            }
        }
    }

    override fun onAliPaySuccess() {
        if (mHasMoney) {
            EventBus.getDefault().post("refresh_money")
            finish()
        } else {
            mPresenter?.getTotalAmount()
        }
    }

    override fun onWXPaySuccess() {
        if (mHasMoney) {
            EventBus.getDefault().post("refresh_money")
            finish()
        } else {
            mPresenter?.getTotalAmount()
        }
    }

    override fun addQualityMoneySuccess(bean: PayResultBean, payType: Int) {
        if (payType == 1) {

        } else if (payType == 2) {
            onAliCallback(bean.alipayBody ?: "")
        } else {
            onWXCallback(bean)
        }
    }

    override fun getTotalAmountSuccess(bean: TotalAmountBean) {
        bean.data?.run {
            SPUtils.putInt("quality_shop", quality_balance)
            if (quality_balance != 0) {
                EventBus.getDefault().post("refresh_money")
                startActivity<QualificationActivity>()
            }
            finish()
        }
    }

}
