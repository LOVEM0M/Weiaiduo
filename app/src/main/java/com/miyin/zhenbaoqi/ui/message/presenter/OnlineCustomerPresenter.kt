package com.miyin.zhenbaoqi.ui.message.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CheckBlackListBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.message.contract.OnlineCustomerContract
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OnlineCustomerPresenter : BasePresenter<OnlineCustomerContract.IView>(), OnlineCustomerContract.IPresenter {

    override fun getMerchantId(userId: Int) {
        var bean: UserTypeBean? = null
        val disposable = RetrofitUtils.mApiService.searchMerchantsId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    when (it.mark) {
                        "0" -> {
                            bean = it
                            val userId1 = SPUtils.getInt("user_id")
                            return@flatMap RetrofitUtils.mApiService.merchantCheckBlackList(userId1, it.merchants_id)
                        }
                        else -> {
                            getView()?.hideDialog()
                            throw Exception(it.tip)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<CheckBlackListBean>() {
                    override fun doOnSuccess(data: CheckBlackListBean) {
                        bean?.let {
                            it.check_status = data.data?.check_status!!
                            getView()?.getMerchantIdSuccess(it)
                        }
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun getSubMerchantId(accountName: String) {
        var bean: UserTypeBean? = null
        val disposable = RetrofitUtils.mApiService.searchMerchantsSub(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    when (it.mark) {
                        "0" -> {
                            bean = it
                            val userId1 = SPUtils.getInt("user_id")
                            return@flatMap RetrofitUtils.mApiService.merchantCheckBlackList(userId1, it.merchants_id)
                        }
                        else -> {
                            getView()?.hideDialog()
                            throw Exception(it.tip)
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<CheckBlackListBean>() {
                    override fun doOnSuccess(data: CheckBlackListBean) {
                        bean?.let {
                            it.check_status = data.data?.check_status!!
                            getView()?.getMerchantIdSuccess(it)
                        }
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun addBlackList(userId: Int) {
        request(RetrofitUtils.mApiService.merchantAddBlackList(userId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.addBlackListSuccess()
            }
        })
    }

}