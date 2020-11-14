package com.miyin.zhenbaoqi.ui.sort.presenter

import com.google.gson.JsonParseException
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.sort.contract.MerchantMessageContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

class MerchantMessagePresenter : BasePresenter<MerchantMessageContract.IView>(), MerchantMessageContract.IPresenter {

    override fun getMerchantInfo(merchantId: Int) {
        val seeMerchantInfoObservable = RetrofitUtils.mApiService.seeMerchantInfo(merchantId)
        val seeMerchantEvaluateObservable = RetrofitUtils.mApiService.seeMerchantEvaluate(merchantId)
        val disposable = Single.merge(seeMerchantInfoObservable, seeMerchantEvaluateObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.mark) {
                        "0" -> getView()?.getMerchantInfoSuccess(it)
                        "500" -> App.context.clearTask<WXLoginActivity>()
                        else -> getView()?.showToast(it.tip)
                    }
                }, {
                    when (it) {
                        is ConnectException -> getView()?.showToast("网络连接失败")
                        is JsonParseException -> getView()?.showToast("JSON 数据解析异常")
                        else -> getView()?.showToast(it.message)
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun updateMerchantState(focusState: Int, merchantId: Int) {
        val state = if (focusState == 0) 1 else 0
        val keyArray = arrayOf("merchants_id", "focus_state")
        val valueArray = arrayOf<Any>(merchantId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAttention(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.updateMerchantStateSuccess(state)
            }
        })
    }

    override fun liveRoomEntry(roomId: Int) {
        request(RetrofitUtils.mApiService.liveRoomEntry(roomId), object : BaseSingleObserver<LiveEntryRoomBean>() {
            override fun doOnSuccess(data: LiveEntryRoomBean) {
                getView()?.liveRoomEntrySuccess(data)
            }
        })
    }

}