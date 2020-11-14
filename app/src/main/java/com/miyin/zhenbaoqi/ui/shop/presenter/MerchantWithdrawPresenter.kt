package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantWithdrawContact
import com.miyin.zhenbaoqi.utils.JSONUtils

class MerchantWithdrawPresenter : BasePresenter<MerchantWithdrawContact.IView>(), MerchantWithdrawContact.IPresenter {

    override fun getBankCardList() {
        request(RetrofitUtils.mApiService.bankCardList(), object : BaseSingleObserver<BankCardBean>() {
            override fun doOnSuccess(data: BankCardBean) {
                val list = data.list
                if (null != list) {
                    getView()?.getBankCardListSuccess(list)
                }
            }
        })
    }

    override fun walletMerchantWithdraw(amount: Long, type: Int, bankId: Int) {
        val keyArray = arrayOf("amount", "type", "user_bank_id")
        val valueArray = arrayOf<Any>(amount, type, bankId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.walletMerchantWithdraw(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.walletMerchantWithdrawSuccess()
            }
        })
    }

}