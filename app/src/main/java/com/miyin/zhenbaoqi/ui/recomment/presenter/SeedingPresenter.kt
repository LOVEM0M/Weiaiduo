package com.miyin.zhenbaoqi.ui.recomment.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.bean.SeedingBean
import com.miyin.zhenbaoqi.ui.recomment.contract.SeedingContract
import com.miyin.zhenbaoqi.utils.JSONUtils


class SeedingPresenter : BasePresenter<SeedingContract.IView>(), SeedingContract.IPresenter {
    override fun getSeedingGoodsList(currentPage: Int,goodsName : String , pageSize: Int) {
        val keyArray = arrayOf("current_page", "goodsName","page_size")
        val valueArray = arrayOf<Any>(currentPage,goodsName, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.seedingGoodsList(requestBody), object : BaseSingleObserver<SeedingBean>() {
            override fun doOnSuccess(list: SeedingBean) {
                getView()?.getSeedingGoodsListSuccess(list)
            }

            override fun doOnFailure(list: SeedingBean) {
                if (currentPage == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
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
