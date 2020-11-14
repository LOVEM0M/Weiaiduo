package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.RealNameBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.VerifiedContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class VerifiedPresenter : BasePresenter<VerifiedContract.IView>(), VerifiedContract.IPresenter {

    override fun showRealName() {
        request(RetrofitUtils.mApiService.showRealName(), object : BaseSingleObserver<RealNameBean>() {
            override fun doOnSuccess(data: RealNameBean) {
                getView()?.showRealNameSuccess(data)
            }
        })
    }

    override fun realName(idNo: String?, realName: String?) {
        if (idNo.isNullOrEmpty()) {
            getView()?.showToast("请填写身份证号")
            return
        }
        if (realName.isNullOrEmpty()) {
            getView()?.showToast("请填写姓名")
            return
        }

        val keyArray = arrayOf("id_no", "real_name")
        val valueArray = arrayOf<Any>(idNo!!, realName!!)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.realName(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.realNameSuccess()
            }
        })
    }

}
