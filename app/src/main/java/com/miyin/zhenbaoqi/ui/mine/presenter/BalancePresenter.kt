package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.BalanceBean
import com.miyin.zhenbaoqi.bean.MerchantHasAuthBean
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.BalanceContract

class BalancePresenter : BasePresenter<BalanceContract.IView>(), BalanceContract.IPresenter {

    override fun getBalance() {
        request(RetrofitUtils.mApiService.balance(), object : BaseSingleObserver<BalanceBean>() {
            override fun doOnSuccess(data: BalanceBean) {
                getView()?.getBalanceSuccess(data.balance / 100f)
            }
        })
    }

    override fun getTotalAmount() {
        request(RetrofitUtils.mApiService.walletMerchantTotalAmount(), object : BaseSingleObserver<TotalAmountBean>() {
            override fun doOnSuccess(data: TotalAmountBean) {
                getView()?.getTotalAmountSuccess(data)
            }
        })
    }

    override fun merchantHasAuth() {
        request(RetrofitUtils.mApiService.merchantIsAuth(), object : BaseSingleObserver<MerchantHasAuthBean>() {
            override fun doOnSuccess(data: MerchantHasAuthBean) {
                getView()?.merchantHasAuthSuccess(data.data?.status!!)
            }
        })
    }

}