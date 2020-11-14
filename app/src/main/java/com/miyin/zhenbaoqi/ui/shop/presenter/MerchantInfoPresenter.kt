package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.MerchantBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantInfoContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class MerchantInfoPresenter : BasePresenter<MerchantInfoContract.IView>(), MerchantInfoContract.IPresenter {

    override fun getMerchantInfo() {
        request(RetrofitUtils.mApiService.merchantInfo(), object : BaseSingleObserver<MerchantBean>() {
            override fun doOnSuccess(data: MerchantBean) {
                getView()?.getMerchantInfoSuccess(data)
            }
        })
    }

    override fun uploadMerchantHead(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadMerchantHeadImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadMerchantHeadSuccess(data.photo_url!!)
            }
        })
    }

    override fun uploadMerchantBackground(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadMerchantBackgroundImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadMerchantBackgroundSuccess(data.photo_url!!)
            }
        })
    }

    override fun updateMerchantInfo(address: String?, cityId: Int, consignee: String?, countyId: Int, headImg: String?,
                                    merchantsBack: String?, merchantsName: String?, merchantsSubtitle: String?,
                                    phoneNo: String?, provinceId: Int, wechatId: String?) {
        if (headImg.isNullOrEmpty()) {
            getView()?.showToast("请选择店铺头像")
            return
        }
        if (merchantsName.isNullOrEmpty()) {
            getView()?.showToast("请填写店铺名")
            return
        }
        if (merchantsSubtitle.isNullOrEmpty()) {
            getView()?.showToast("请填写店铺介绍")
            return
        }
        if (wechatId.isNullOrEmpty()) {
            getView()?.showToast("请填写微信号")
            return
        }
        if (phoneNo.isNullOrEmpty()) {
            getView()?.showToast("请填写手机号")
            return
        }
        if (address.isNullOrEmpty()) {
            getView()?.showToast("请选择退货地址")
            return
        }
        if (merchantsBack.isNullOrEmpty()) {
            getView()?.showToast("请选择店铺背景图")
            return
        }

        val keyArray = arrayOf("address", "city_id", "consignee", "county_id", "head_img", "merchants_back",
                "merchants_name", "merchants_subtitle", "phone_no", "province_id", "wechat_id")
        val valueArray = arrayOf<Any>(address!!, cityId, consignee!!, countyId, headImg!!, merchantsBack!!,
                merchantsName!!, merchantsSubtitle!!, phoneNo!!, provinceId, wechatId!!)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateMerchantInfo(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateMerchantInfoSuccess()
            }
        })
    }

    override fun getProvinceList() {
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf("province"))
        request(RetrofitUtils.mApiService.parentList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                val list = data.dicts
                if (null == list || list.isEmpty()) {
                    getView()?.showToast(data.msg)
                } else {
                    getView()?.getProvinceListSuccess(list)
                }
            }
        })
    }

    override fun getAreaList(position: Int, state: Int, parentId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("parent_id"), arrayOf(parentId))
        request(RetrofitUtils.mApiService.sonList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                val list = data.dicts
                if (null == list || list.isEmpty()) {
                    getView()?.showToast(data.msg)
                } else {
                    getView()?.getAreaListSuccess(list, position, state)
                }
            }
        })
    }

}
