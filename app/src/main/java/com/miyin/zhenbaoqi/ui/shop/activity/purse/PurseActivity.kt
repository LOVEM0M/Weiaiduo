package com.miyin.zhenbaoqi.ui.shop.activity.purse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.mine.activity.wallet.BalanceActivity
import com.miyin.zhenbaoqi.ui.mine.activity.wallet.WithdrawActivity
import com.miyin.zhenbaoqi.ui.mine.contract.BalanceContract
import com.miyin.zhenbaoqi.ui.mine.presenter.BalancePresenter
import com.miyin.zhenbaoqi.ui.shop.activity.security_deposit.QualificationActivity
import com.miyin.zhenbaoqi.ui.shop.activity.security_deposit.SecurityDepositActivity
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_purse.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat

class PurseActivity : BaseMvpActivity<BalanceContract.IView, BalanceContract.IPresenter>(), BalanceContract.IView {

    private var mBalance = 0f
    private var mQualityBalance = 0

    override fun getContentView(): Int {
        return R.layout.activity_purse
    }

    override fun useEventBus() = true

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("我的钱包")
        immersionBar { statusBarDarkFont(true) }

        setOnClickListener(tv_detail, tv_withdraw, tv_income_detail, tv_loan_income_detail, tv_add_quota)
    }

    override fun initData() {
        mPresenter?.getBalance()
        mPresenter?.getTotalAmount()
    }

    override fun createPresenter() = BalancePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_detail -> startActivity<BalanceActivity>("balance" to mBalance)
            R.id.tv_withdraw -> startActivity<WithdrawActivity>("balance" to mBalance)
            R.id.tv_income_detail -> startActivity<IncomeDetailActivity>("title" to "收益总额")
            R.id.tv_loan_income_detail -> startActivity<IncomeDetailActivity>("title" to "货款收入")
            R.id.tv_add_quota -> {
                if (mQualityBalance == 0) {
                    WebActivity.openActivity(this, "申请认证品质店铺", Constant.AUTH_STORE)
                } else {
                    mPresenter?.merchantHasAuth()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun getBalanceSuccess(balance: Float) {
        mBalance = balance
        tv_balance.text = SpanUtils()
                .append("¥").setFontSize(16, true)
                .append(DecimalFormat("0.00").format(balance))
                .create()
    }

    override fun getTotalAmountSuccess(bean: TotalAmountBean) {
        bean.data?.run {
            mQualityBalance = quality_balance

            tv_income.text = FormatUtils.formatNumber(earn_balance  )
            tv_loan_income.text = FormatUtils.formatNumber(payment_balance  )
            tv_security_deposit.text = FormatUtils.formatNumber(quality_balance  )
        }
    }

    override fun merchantHasAuthSuccess(state: Int) {
        when (state) {
            0 -> {
                startActivity<QualificationActivity>()
            }
            1 -> {
                showToast("待高级商家核实")
            }
            2 -> {
                showToast("待总部核实")
            }
            else -> startActivity<SecurityDepositActivity>("has_money" to true)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "refresh_money") {
            mPresenter?.getTotalAmount()
        }
    }

}
