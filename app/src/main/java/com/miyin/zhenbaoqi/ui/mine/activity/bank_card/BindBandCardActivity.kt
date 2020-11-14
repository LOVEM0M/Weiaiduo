package com.miyin.zhenbaoqi.ui.mine.activity.bank_card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.BankBean
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.bean.RealNameBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.common.SingleDataDialog
import com.miyin.zhenbaoqi.ui.mine.contract.BindBandCardContract
import com.miyin.zhenbaoqi.ui.mine.presenter.BindBandCardPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_bind_band_card.*

class BindBandCardActivity : BaseMvpActivity<BindBandCardContract.IView, BindBandCardContract.IPresenter>(), BindBandCardContract.IView, OnDialogCallback {

    private var mName: String? = null
    private var mBankCardNo: String? = null
    private var mBranchBank: String? = null
    private var mBankId = 0
    private var mUserBankId = 0
    private var mBankCardBean: BankCardBean.ListBean? = null

    override fun getContentView(): Int {
        mBankCardBean = intent.getSerializableExtra("bean") as BankCardBean.ListBean?
        return R.layout.activity_bind_band_card
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("绑定银行卡")
        immersionBar { statusBarDarkFont(true) }

        if (null != mBankCardBean) {
            mBankCardBean!!.run {
                mName = bank_name
                mBranchBank = bank_branch
                mBankCardNo = bank_card_num
                mBankId = bank_id
                mUserBankId = user_bank_id

                et_name.setText(mName)
                et_bank_card_no.setText(mBankCardNo)
                et_bank_branch.setText(mBranchBank)
                tv_bank.text = bank_name
            }
        } else {
            mPresenter?.showRealName()
        }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
            }
        })
        et_bank_card_no.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mBankCardNo = editable.toString().trim { it <= ' ' }
            }
        })
        et_bank_branch.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mBranchBank = editable.toString().trim { it <= ' ' }
            }
        })

        setOnClickListener(tv_bank, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = BindBandCardPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mName = data?.getStringExtra("real_name")
            et_name.setText(mName)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_bank -> mPresenter?.getBankList()
            R.id.btn_commit -> {
                if (mUserBankId == 0) {
                    mPresenter?.addBankCard(et_bank_card_no.textWithoutSpace, mBranchBank, mBankId)
                } else {
                    mPresenter?.updateBankCard(et_bank_card_no.textWithoutSpace, mBranchBank, mBankId, mUserBankId)
                }
            }
        }
    }

    override fun getBankListSuccess(list: List<BankBean.BankListBean>) {
        if (list.isEmpty()) return

        val dialog = SingleDataDialog.newInstance(list)
        dialog.show(supportFragmentManager, "bank")
    }

    override fun showRealNameSuccess(bean: RealNameBean) {
        mName = bean.real_name
        et_name.setText(mName)
    }

    override fun addBankCardSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun updateBankCardSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        when (obj) {
            is BankBean.BankListBean -> {
                mBankId = obj.bank_id
                tv_bank.text = obj.bank_name
            }
        }
    }

}
