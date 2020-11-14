package com.miyin.zhenbaoqi.ui.mine.activity.wallet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.activity.bank_card.BankCardActivity
import com.miyin.zhenbaoqi.ui.mine.contract.WithdrawContract
import com.miyin.zhenbaoqi.ui.mine.presenter.WithdrawPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_withdraw.*
import java.text.DecimalFormat

@SuppressLint("SetTextI18n")
class WithdrawActivity : BaseMvpActivity<WithdrawContract.IView, WithdrawContract.IPresenter>(), WithdrawContract.IView {

    private var mMoney: String? = null
    private var mUserBankId = 0
    private var mBalance = 0f

    override fun getContentView(): Int {
        mBalance = intent.getFloatExtra("balance", 0f)
        return R.layout.activity_withdraw
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("申请提现")
        immersionBar { statusBarDarkFont(true) }

        tv_balance.text = "可用余额 ${DecimalFormat("0.00").format(mBalance)}元"

        et_money.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mMoney = editable.toString().trim { it <= ' ' }
                btn_commit.isEnabled = !TextUtils.isEmpty(mMoney)
            }
        })

        setOnClickListener(fl_container, btn_commit)
    }

    override fun initData() {
        mPresenter?.getBankCardList()
    }

    override fun createPresenter() = WithdrawPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.run {
                (getSerializableExtra("bean") as BankCardBean.ListBean).run {
                    mUserBankId = user_bank_id

                    iv_cover.loadImg(bank_logo)
                    tv_name.text = bank_name
                    tv_card_num.text = bank_card_num
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_container -> startActivityForResult<BankCardActivity>(Constant.INTENT_REQUEST_CODE)
            R.id.btn_commit -> {
                if (!mMoney.isNullOrEmpty()) {
                    val price = mMoney!!.toFloat()
                    if (price > mBalance) {
                        showToast("请输入合理的金额")
                        return
                    }
                    mPresenter?.withdraw(mMoney, mUserBankId)
                }
            }
        }
    }

    override fun withdrawSuccess() {
        showToast("申请提现成功")
        finish()
    }

    override fun getBankCardListSuccess(list: List<BankCardBean.ListBean>) {
        list[0].run {
            mUserBankId = user_bank_id

            iv_cover.loadImg(bank_logo)
            tv_name.text = bank_name
            tv_card_num.text = bank_card_num
        }
    }

}
