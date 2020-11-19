package com.miyin.zhenbaoqi.ui.mine.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AddressContact
import com.miyin.zhenbaoqi.utils.JSONUtils

class AddressPresenter : BasePresenter<AddressContact.IView>(), AddressContact.IPresenter {

    override fun getAddressList(page: Int, rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.addressList(map), object : BaseSingleObserver<AddressBean>() {
            override fun doOnSuccess(data: AddressBean) {
                getView()?.showNormal()
                getView()?.getAddressListSuccess(data)
            }

            override fun doOnFailure(data: AddressBean) {
                    getView()?.showEmpty()
                    getView()?.onFailure(data.msg!!, 1)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }

    override fun deleteAddress(adsId: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("adsId", adsId)
        }
        request(RetrofitUtils.mApiService.deleteAddress(map), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.deleteAddressSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure(data.msg!!, 0)
            }
        })
    }

    override fun setDefaultAddress(address: String, addressId: Int, cityId: Int, consignee: String,
                                   countyId: Int, isDefault: Int, phoneNo: String, provinceId: Int) {
        if (isDefault == 0) {
            return
        }

        val keyArray = arrayOf("address", "ads_id", "city_id", "consignee", "county_id", "is_default", "phone_no", "province_id")
        val valueArray = arrayOf(address, addressId, cityId, consignee, countyId, 0, phoneNo, provinceId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateAddress(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.setDefaultAddressSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure(data.msg!!, 0)
            }
        })
    }

}
