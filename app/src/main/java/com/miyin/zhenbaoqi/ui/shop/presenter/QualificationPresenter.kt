package com.miyin.zhenbaoqi.ui.shop.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.MerchantBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.QualificationContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class QualificationPresenter : BasePresenter<QualificationContract.IView>(), QualificationContract.IPresenter {

    override fun getMerchantInfo() {
        request(RetrofitUtils.mApiService.merchantInfo(), object : BaseSingleObserver<MerchantBean>() {
            override fun doOnSuccess(data: MerchantBean) {
                getView()?.getMerchantInfoSuccess(data)
            }
        })
    }

    override fun uploadImage(path: String?) {
        if (path.isNullOrEmpty()) {
            getView()?.showToast("请选择要上传的图片")
            return
        }

        getView()?.showDialog("图片正在上传...", false)
        val list = uploadImg(path!!)
        request(RetrofitUtils.mApiService.uploadMerchantAuthImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImageSuccess(data.photo_url ?: "")
                getView()?.hideDialog()
            }

            override fun doOnFailure(data: ImageBean) {
                super.doOnFailure(data)
                getView()?.hideDialog()
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun getProvinceList() {
        val map = ArrayMap<String, Any>().apply {
            put("type", 0)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                val list = data.data
                if (null == list || list.isEmpty()) {
                    getView()?.showToast(data.msg)
                } else {
                    getView()?.getProvinceListSuccess(list)
                }
            }
        })
    }

    override fun getAreaList(position: Int, state: Int, parentId: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("parentId", parentId)
        }
        request(RetrofitUtils.mApiService.sonList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                val list = data.data
                if (null == list || list.isEmpty()) {
                    getView()?.showToast(data.msg)
                } else {
                    getView()?.getAreaListSuccess(list, position, state)
                }
            }
        })
    }

    override fun getCategoryList(type: Int) {
         val map = ArrayMap<String, Any>().apply {
            put("type", type)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getCategoryListSuccess(data)
            }
        })
    }

    override fun merchantAuth(address: String?, cityId: Int, consignee: String?, countyId: Int, headImg: String?,
                              identityImages: String, identityType: Int, licenceImage: String, mainCate: Int,
                              merchantsBack: String?, merchantsName: String?, merchantsSubtitle: String?,
                              phoneNo: String?, provinceId: Int, wechatId: String?) {
        if (headImg.isNullOrEmpty()) {
            getView()?.showToast("请上传店铺头像")
            return
        }
        if (merchantsName.isNullOrEmpty()) {
            getView()?.showToast("请填写店铺名称")
            return
        }
        if (merchantsSubtitle.isNullOrEmpty()) {
            getView()?.showToast("请填写店铺介绍")
            return
        }
        if (wechatId.isNullOrEmpty()) {
            getView()?.showToast("请填写店铺微信号")
            return
        }
        if (phoneNo.isNullOrEmpty()) {
            getView()?.showToast("请填写手机号")
            return
        }
        if (provinceId == 0 || cityId == 0 || countyId == 0) {
            getView()?.showToast("请选择发货城市")
            return
        }
        if (address.isNullOrEmpty()) {
            getView()?.showToast("请填写发货地址")
            return
        }
        if (merchantsBack.isNullOrEmpty()) {
            getView()?.showToast("请上传店铺背景图")
            return
        }

        val keyArray = arrayOf("address", "city_id", "consignee", "county_id", "head_img", "identity_images",
                "identity_type", "licence_image", "main_cate", "merchants_back", "merchants_name", "merchants_subtitle",
                "phone_no", "province_id", "wechat_id")
        val valueArray = arrayOf<Any>(address!!, cityId, consignee!!, countyId, headImg!!, identityImages,
                identityType, licenceImage, mainCate, merchantsBack!!, merchantsName!!, merchantsSubtitle!!,
                phoneNo!!, provinceId, wechatId!!)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAuth(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.merchantAuthSuccess()
            }
        })
    }

}
