package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.FeedbackContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class FeedbackPresenter : BasePresenter<FeedbackContract.IView>(), FeedbackContract.IPresenter {

    override fun uploadImage(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadFeedbackImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImgSuccess(data.photo_url ?: "")
            }

            override fun doOnFailure(data: ImageBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
            }
        })
    }

    override fun feedback(proposalContent: String?, repairPicture: String) {
        if (proposalContent.isNullOrEmpty()) {
            getView()?.showToast("请填写意见描述")
            return
        }

        val keyArray = arrayOf("proposal_content", "repair_picture")
        val valueArray = arrayOf<Any>(proposalContent, repairPicture)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.feedback(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.feedbackSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

}
