package com.miyin.zhenbaoqi.ui.mine.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AddAddressContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AddAddressPresenter : BasePresenter<AddAddressContract.IView>(), AddAddressContract.IPresenter {

    override fun addAddress(address: String?, cityId: Int, consignee: String?, countyId: Int, isDefault: Int, phoneNo: String?, provinceId: Int) {
        if (phoneNo.isNullOrEmpty()) {
            getView()?.showToast("请填写手机号")
            return
        }
        if (provinceId == 0 || cityId == 0 || countyId == 0) {
            getView()?.showToast("请选择城市")
            return
        }
        if (address.isNullOrEmpty()) {
            getView()?.showToast("请填写收货地址")
            return
        }
        if (consignee.isNullOrEmpty()) {
            getView()?.showToast("请填写收货人")
            return
        }

        val keyArray = arrayOf("address", "cityId", "consignee", "countyId", "isDefault", "phoneNo", "provinceId")
        val valueArray = arrayOf<Any>(address!!, cityId, consignee!!, countyId, isDefault, phoneNo!!, provinceId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.addAddress(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.addAddressSuccess()
            }
        })
    }

    override fun updateAddress(address: String?, adsId: Int, cityId: Int, consignee: String?, countyId: Int, isDefault: Int, phoneNo: String?, provinceId: Int) {
        if (phoneNo.isNullOrEmpty()) {
            getView()?.showToast("请填写手机号")
            return
        }
        if (provinceId == 0 || cityId == 0 || countyId == 0) {
            getView()?.showToast("请选择城市")
            return
        }
        if (address.isNullOrEmpty()) {
            getView()?.showToast("请填写收货地址")
            return
        }
        if (consignee.isNullOrEmpty()) {
            getView()?.showToast("请填写收货人")
            return
        }

        val keyArray = arrayOf("address", "adsId", "cityId", "consignee", "countyId", "isDefault", "phoneNo", "provinceId")
        val valueArray = arrayOf<Any>(address!!, adsId, cityId, consignee!!, countyId, isDefault, phoneNo!!, provinceId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateAddress(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateAddressSuccess()
            }
        })
    }

    override fun getProvinceList(type : Int) {
        val map = ArrayMap<String, Any>().apply {
            put("type", type)
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

}
