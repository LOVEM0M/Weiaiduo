package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BankBean
import com.miyin.zhenbaoqi.bean.RealNameBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.BindBandCardContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BindBandCardPresenter : BasePresenter<BindBandCardContract.IView>(), BindBandCardContract.IPresenter {

    override fun getBankList() {
        request(RetrofitUtils.mApiService.bankList(), object : BaseSingleObserver<BankBean>() {
            override fun doOnSuccess(data: BankBean) {
                val list = data.list
                if (null != list) {
                    getView()?.getBankListSuccess(list)
                } else {
                    getView()?.showToast("未知错误")
                }
            }
        })
    }

    override fun showRealName() {
        request(RetrofitUtils.mApiService.showRealName(), object : BaseSingleObserver<RealNameBean>() {
            override fun doOnSuccess(data: RealNameBean) {
                getView()?.showRealNameSuccess(data)
            }
        })
    }

    override fun addBankCard(bankCardNum: String?, bankBranch: String?, bankId: Int) {
        if (bankCardNum.isNullOrEmpty()) {
            getView()?.showToast("请填写银行卡号")
            return
        }
        if (bankBranch.isNullOrEmpty()) {
            getView()?.showToast("请填写支行")
            return
        }

        val keyArray = arrayOf("bank_branch", "bank_card_num", "bank_id")
        val valueArray = arrayOf<Any>(bankBranch, bankCardNum, bankId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.addBankCard(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.addBankCardSuccess()
            }
        })
    }

    override fun updateBankCard(bankCardNum: String?, bankBranch: String?, bankId: Int, userBankId: Int) {
        if (bankCardNum.isNullOrEmpty()) {
            getView()?.showToast("请填写银行卡号")
            return
        }
        if (bankBranch.isNullOrEmpty()) {
            getView()?.showToast("请填写支行")
            return
        }

        val keyArray = arrayOf("bank_branch", "bank_card_num", "bank_id", "user_bank_id")
        val valueArray = arrayOf<Any>(bankCardNum, bankId, userBankId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateBankCard(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateBankCardSuccess()
            }
        })
    }

}
