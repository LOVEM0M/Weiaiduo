package com.miyin.zhenbaoqi.ui.shop.activity.purse

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.activity.bank_card.BankCardActivity
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantWithdrawContact
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantWithdrawPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_merchant_withdraw.*

@SuppressLint("SetTextI18n")
class MerchantWithdrawActivity : BaseMvpActivity<MerchantWithdrawContact.IView, MerchantWithdrawContact.IPresenter>(), MerchantWithdrawContact.IView {

    private var mWithdrawPrice = 0L
    private var mTitle: String? = null
    private var mPrice: String? = null
    private var mBankId = 0

    override fun getContentView(): Int {
        with(intent) {
            mWithdrawPrice = getLongExtra("price", 0L)
            mTitle = getStringExtra("title")
        }
        return R.layout.activity_merchant_withdraw
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar(mTitle ?: "")
        immersionBar { statusBarDarkFont(true, 0.2f) }

        tv_price.text = "¥${FormatUtils.formatNumber(mWithdrawPrice  )}"

        et_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPrice = editable.toString().trim { it <= ' ' }
                btn_commit.isEnabled = !mPrice.isNullOrEmpty()
            }
        })

        when (mTitle) {
            "收益提现" -> tv_remark.text = "佣金提现说明:\n1.每月1-10日、16-25日支持提现，分别于当月15日发放及每月月末发放，如遇周末及法定节假日顺延至下个工作日;\n2.单人每月累计总金额，按照相关法规，每笔扣除税费比例6%;\n3.佣金提现每次不得低于10元，不足10元暂不支持提现(最小单位为元)，单月多笔提现，打款时会合并成一笔打款至提现账户;\n4.因佣金打款为人工第三方处理，实际到账周期会因银行及地域原因有所延迟，若有疑问请于每月1日及15日提供身份证号向客服索取打款回执单;\n5.若因银行卡号、身份证及预留手机号错误造成打款失败，提现申请自动拒绝，需顺延至下个提现周期重新申请;"
            "货款提现" -> tv_remark.text = "货款提现规则\n1. 单笔不得低于10元，最高不超过20万元(最小单位为元)；\n2. 提现到账时间为次日，如遇节假日则顺延"
        }

        setOnClickListener(fl_bank_card, btn_commit)
    }

    override fun initData() {
        mPresenter?.getBankCardList()
    }

    override fun createPresenter() = MerchantWithdrawPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.run {
                (getSerializableExtra("bean") as BankCardBean.ListBean).run {
                    mBankId = user_bank_id

                    tv_card_num.text = bank_card_num
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_bank_card -> startActivityForResult<BankCardActivity>(Constant.INTENT_REQUEST_CODE)
            R.id.btn_commit -> {
                if (mPrice.isNullOrEmpty()) {
                    showToast("请输入提现金额")
                    return
                }
                val price = (mPrice!!.toFloat() * 100).toLong()
                if (price > mWithdrawPrice) {
                    showToast("请输入合理提现金额")
                    return
                }

                val type = if (mTitle == "收益提现") 0 else 1
                mPresenter?.walletMerchantWithdraw(price, type, mBankId)
            }
        }
    }

    override fun getBankCardListSuccess(bean: List<BankCardBean.ListBean>) {
        if (bean.isNotEmpty()) {
            mBankId = bean[0].user_bank_id

            tv_card_num.text = bean[0].bank_card_num
        }
    }

    override fun walletMerchantWithdrawSuccess() {
        finish()
    }

}
