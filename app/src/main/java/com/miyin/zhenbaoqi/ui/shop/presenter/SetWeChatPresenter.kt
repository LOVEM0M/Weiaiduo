package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.SetWeChatContract

class SetWeChatPresenter : BasePresenter<SetWeChatContract.IView>(), SetWeChatContract.IPresenter {

    override fun uploadImage(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadFeedbackImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImageSuccess(data)
            }
        })
    }

    override fun inviteMerchantWeChat(weChatId: String, weChatImage: String) {
        request(RetrofitUtils.mApiService.inviteMerchantAddWeChat(weChatId, weChatImage), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.inviteMerchantWeChatSuccess()
            }
        })
    }

}
