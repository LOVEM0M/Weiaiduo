package com.miyin.zhenbaoqi.ui.mine.activity.wallet

import android.annotation.SuppressLint
import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.contract.BalanceContract
import com.miyin.zhenbaoqi.ui.mine.presenter.BalancePresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_wallet.*

class WalletActivity : BaseMvpActivity<BalanceContract.IView, BalanceContract.IPresenter>(), BalanceContract.IView {

    private var mBalance = 0f

    override fun getContentView() = R.layout.activity_wallet

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("我的钱包")
        immersionBar { statusBarDarkFont(true) }

        tv_withdraw.setOnClickListener {
            startActivityForResult<WithdrawActivity>(Constant.INTENT_REQUEST_CODE)
        }
        tv_detail.setOnClickListener {
            startActivity<BalanceActivity>("balance" to mBalance)
        }
    }

    override fun initData() {
        mPresenter?.getBalance()
    }

    override fun createPresenter() = BalancePresenter()

    @SuppressLint("SetTextI18n")
    override fun getBalanceSuccess(balance: Float) {
        mBalance = balance
        tv_balance.text = "¥${FormatUtils.formatNumber(balance)}"
    }

    override fun getTotalAmountSuccess(bean: TotalAmountBean) {

    }

    override fun merchantHasAuthSuccess(state: Int) {

    }

}
