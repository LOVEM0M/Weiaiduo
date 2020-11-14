package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MaterialBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.MaterialContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class MaterialPresenter : BasePresenter<MaterialContract.IView>(), MaterialContract.IPresenter {

    override fun getMerchantMaterialList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantMaterialList(requestBody), object : BaseSingleObserver<MaterialBean>() {
            override fun doOnSuccess(data: MaterialBean) {
                getView()?.showNormal()
                getView()?.getMerchantMaterialListSuccess(data)
            }

            override fun doOnFailure(data: MaterialBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    super.doOnFailure(data)
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

}
