package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AfterSaleContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AfterSalePresenter : BasePresenter<AfterSaleContract.IView>(), AfterSaleContract.IPresenter {

    override fun uploadImage(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadAfterSaleImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImgSuccess(data.photo_url!!)
            }

            override fun doOnFailure(data: ImageBean) {
                getView()?.onFailure(data.msg!!, 0)
            }
        })
    }

    override fun afterSaleLaunch(afterImg: String, afterType: Int, afterWhy: String?, orderNumber: String) {
        if (afterWhy.isNullOrEmpty()) {
            getView()?.onFailure("请选择售后原因", 1)
            return
        }

        val keyArray = arrayOf("after_img", "after_type", "after_why", "order_number")
        val valueArray = arrayOf(afterImg, afterType, afterWhy!!, orderNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.afterSaleLaunch(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.afterSaleLaunchSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure(data.msg!!, 1)
            }
        })
    }

}
