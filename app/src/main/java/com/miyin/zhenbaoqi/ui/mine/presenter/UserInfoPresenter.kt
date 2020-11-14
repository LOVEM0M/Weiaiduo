package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ReferrerBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.UserInfoContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class UserInfoPresenter : BasePresenter<UserInfoContract.IView>(), UserInfoContract.IPresenter {

    override fun updateHeadImg(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.updateHeadImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                data.photo_url?.run {
                    getView()?.updateHeadImgSuccess(this)
                }
            }
        })
    }

    override fun updateUserInfo(birthday: String?, gender: Int, nickName: String?) {
        if (nickName.isNullOrEmpty()) {
            getView()?.showToast("请填写昵称")
            return
        }
        if (birthday.isNullOrEmpty()) {
            getView()?.showToast("请选择出生日期")
            return
        }
        val keyArray = arrayOf("birthday", "gender", "nick_name")
        val valueArray = arrayOf(birthday, gender, nickName)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateUserInfo(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateUserInfoSuccess()
            }
        })
    }

    override fun referrerInfo() {
        request(RetrofitUtils.mApiService.referrerInfo(), object : BaseSingleObserver<ReferrerBean>() {
            override fun doOnSuccess(data: ReferrerBean) {
                getView()?.referrerInfoSuccess(data)
            }
        })
    }

}
