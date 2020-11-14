package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.ReleaseVideoContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ReleaseVideoPresenter : BasePresenter<ReleaseVideoContract.IView>(), ReleaseVideoContract.IPresenter {

    override fun getMainCategory() {
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf("goods_category"))
        request(RetrofitUtils.mApiService.parentList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getMainCategorySuccess(data)
            }
        })
    }

    override fun uploadVideo(path: String?, desc: String?, type: Int) {
        if (path.isNullOrEmpty()) {
            getView()?.showToast("请选择要上传的视频")
            return
        }
        if (desc.isNullOrEmpty()) {
            getView()?.showToast("请填写描述")
            return
        }
        if (type == 0) {
            getView()?.showToast("请选择分类")
            return
        }
        getView()?.showDialog("正在上传...", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        val file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("video/*"), file)
        builder.addFormDataPart("video_file", file.name, requestBody)
                .addFormDataPart("video_describe", desc)
                .addFormDataPart("type", type.toString())

        val list = builder.build().parts()
        request(RetrofitUtils.mApiService.merchantVideoUpload(list), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
                getView()?.uploadVideoSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.hideDialog()
                super.doOnFailure(data)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun editVideo(videoId: Int, desc: String?, type: Int, path: String?) {
        if (path.isNullOrEmpty()) {
            getView()?.showToast("请选择要上传的视频")
            return
        }
        if (desc.isNullOrEmpty()) {
            getView()?.showToast("请填写描述")
            return
        }
        if (type == 0) {
            getView()?.showToast("请选择分类")
            return
        }

        getView()?.showDialog("正在上传...", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        val file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("video/*"), file)
        builder.addFormDataPart("video_file", file.name, requestBody)
                .addFormDataPart("video_describe", desc)
                .addFormDataPart("type", type.toString())
                .addFormDataPart("id", videoId.toString())

        val list = builder.build().parts()
        request(RetrofitUtils.mApiService.merchantVideoEdit(list), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
                getView()?.uploadVideoSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.hideDialog()
                super.doOnFailure(data)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

}
