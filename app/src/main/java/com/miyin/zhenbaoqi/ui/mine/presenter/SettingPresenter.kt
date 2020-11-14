package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.PushSwitchBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.SettingContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SettingPresenter : BasePresenter<SettingContract.IView>(), SettingContract.IPresenter {

    override fun pushSwitch() {
        request(RetrofitUtils.mApiService.pushSwitch(), object : BaseSingleObserver<PushSwitchBean>() {
            override fun doOnSuccess(data: PushSwitchBean) {
                getView()?.pushSwitchSuccess(data.push_switch)
            }

            override fun doOnFailure(data: PushSwitchBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

    override fun updatePushSwitch(pushSwitch: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("push_switch"), arrayOf(pushSwitch))
        request(RetrofitUtils.mApiService.updatePushSwitch(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                val toggle = if (pushSwitch == 0) 1 else 0
                getView()?.updatePushSwitchSuccess(toggle)
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

}
