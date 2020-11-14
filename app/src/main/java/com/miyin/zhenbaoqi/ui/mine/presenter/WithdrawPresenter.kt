package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.WithdrawContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class WithdrawPresenter : BasePresenter<WithdrawContract.IView>(), WithdrawContract.IPresenter {

    override fun withdraw(amount: String?, userBankId: Int) {
        if (amount.isNullOrEmpty()) {
            getView()?.showToast("请输入提现金额")
            return
        }
        if (userBankId == 0) {
            getView()?.showToast("请选择银行卡")
            return
        }

        val keyArray = arrayOf("amount", "user_bank_id")
        val valueArray = arrayOf<Any>((amount.toFloat() * 100).toInt(), userBankId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.withdraw(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.withdrawSuccess()
            }
        })
    }

    override fun getBankCardList() {
        request(RetrofitUtils.mApiService.bankCardList(), object : BaseSingleObserver<BankCardBean>() {
            override fun doOnSuccess(data: BankCardBean) {
                val list = data.list
                if (null != list) {
                    getView()?.getBankCardListSuccess(list)
                } else {
                    getView()?.showToast(data.tip)
                }
            }

            override fun doOnFailure(data: BankCardBean) {

            }
        })
    }

}
