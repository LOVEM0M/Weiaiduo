package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.BankCardContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class BankCardPresenter : BasePresenter<BankCardContract.IView>(), BankCardContract.IPresenter {

    override fun getBankCardList() {
        request(RetrofitUtils.mApiService.bankCardList(), object : BaseSingleObserver<BankCardBean>() {
            override fun doOnSuccess(data: BankCardBean) {
                val list = data.list
                if (null != list) {
                    getView()?.getBankCardListSuccess(list)
                } else {
                    getView()?.onFailure("未知错误", 0)
                }
            }

            override fun doOnFailure(data: BankCardBean) {
                if (data.code == 1) {
                    getView()?.onFailure(data.msg!!, 1)
                } else if (data.code == 502 && data.msg!!.contains("实名")) {
                    getView()?.onFailure(data.msg!!, 5)
                } else {
                    getView()?.onFailure(data.msg!!, 0)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.onFailure("", 2)
            }
        })
    }

    override fun deleteBankCard(userBankId: Int, position: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("user_bank_id"), arrayOf(userBankId))
        request(RetrofitUtils.mApiService.deleteBankCard(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.deleteBankCardSuccess(position)
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure(data.msg!!, 0)
            }
        })
    }

}
