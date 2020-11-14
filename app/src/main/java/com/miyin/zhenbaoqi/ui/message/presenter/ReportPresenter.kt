package com.miyin.zhenbaoqi.ui.message.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.message.contract.ReportContract

class ReportPresenter : BasePresenter<ReportContract.IView>(), ReportContract.IPresenter {

    override fun uploadImage(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadFeedbackImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImageSuccess(data.photo_url ?: "")
            }
        })
    }

    override fun report(merchantId: Int, content: String?, images: String, cate: String?) {
        if (cate.isNullOrEmpty()) {
            getView()?.showToast("请选择投诉类型")
            return
        }
        if (content.isNullOrEmpty()) {
            getView()?.showToast("请填写投诉内容")
            return
        }

        val map = ArrayMap<String, Any>().apply {
            put("merchants_id", merchantId)
            put("content", content)
            put("images", images)
            put("cate_string", cate)
        }
        request(RetrofitUtils.mApiService.report(map), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.reportSuccess()
            }
        })
    }

}
