package com.miyin.zhenbaoqi.ui.mine.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.ExpressDeliveryContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class ExpressDeliveryPresenter : BasePresenter<ExpressDeliveryContract.IView>(), ExpressDeliveryContract.IPresenter {

    override fun getCourierCompanyList(type: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("type", type)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getCourierCompanyListSuccess(data.data!!)
            }
        })
    }

    override fun confirmShip(courierNo: String?, dictId: Int, orderNumber: String) {
        if (dictId == 0) {
            getView()?.showToast("请选择快递公司")
            return
        }
        if (courierNo.isNullOrEmpty()) {
            getView()?.showToast("请输入或识别快递单号")
            return
        }

        val keyArray = arrayOf("courier_no", "dict_id", "order_number")
        val valueArray = arrayOf<Any>(courierNo, dictId, orderNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.afterSaleShip(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.confirmShipSuccess()
            }
        })
    }

    override fun merchantShip(courierCom: String?, courierName: String, courierNo: String?, orderNumber: String) {
        if (courierCom.isNullOrEmpty()) {
            getView()?.showToast("请选择快递公司")
            return
        }
        if (courierNo.isNullOrEmpty()) {
            getView()?.showToast("请输入或识别快递单号")
            return
        }

        val keyArray = arrayOf("courier_no", "courier_com", "order_number", "courier_name")
        val valueArray = arrayOf<Any>(courierNo, courierCom, orderNumber, courierName)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantOrderSend(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.merchantShipSuccess()
            }
        })
    }

    override fun updateCourierNo(courierCom: String?, courierName: String, courierNo: String?, orderNumber: String) {
        if (courierCom.isNullOrEmpty()) {
            getView()?.showToast("请选择快递公司")
            return
        }
        if (courierNo.isNullOrEmpty()) {
            getView()?.showToast("请输入或识别快递单号")
            return
        }

        val keyArray = arrayOf("courier_no", "courier_com", "order_number", "courier_name")
        val valueArray = arrayOf<Any>(courierNo, courierCom, orderNumber, courierName)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateCourierNo(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.merchantShipSuccess()
            }
        })
    }

}