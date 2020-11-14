package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.AddressContact
import com.miyin.zhenbaoqi.utils.JSONUtils

class AddressPresenter : BasePresenter<AddressContact.IView>(), AddressContact.IPresenter {

    override fun getAddressList(currentPage: Int, pageSize: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("current_page", "page_size"), arrayOf(currentPage, pageSize))
        request(RetrofitUtils.mApiService.addressList(requestBody), object : BaseSingleObserver<AddressBean>() {
            override fun doOnSuccess(data: AddressBean) {
                getView()?.showNormal()
                getView()?.getAddressListSuccess(data)
            }

            override fun doOnFailure(data: AddressBean) {
                if (data.mark == "1") {
                    getView()?.showEmpty()
                    getView()?.onFailure(data.tip!!, 1)
                } else {
                    getView()?.onFailure(data.tip!!, 0)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }

    override fun deleteAddress(addressId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("ads_id"), arrayOf(addressId))
        request(RetrofitUtils.mApiService.deleteAddress(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.deleteAddressSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure(data.tip!!, 0)
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
                getView()?.onFailure(data.tip!!, 0)
            }
        })
    }

}
