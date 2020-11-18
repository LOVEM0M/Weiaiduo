package com.miyin.zhenbaoqi.ui.recomment.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.bean.SeedingBean
import com.miyin.zhenbaoqi.ui.recomment.contract.SeedingContract
import com.miyin.zhenbaoqi.utils.JSONUtils


class SeedingPresenter : BasePresenter<SeedingContract.IView>(), SeedingContract.IPresenter {
    override fun getSeedingGoodsList( goodsName : String ,page: Int,rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.seedingGoodsList(map), object : BaseSingleObserver<SeedingBean>() {
            override fun doOnSuccess(list: SeedingBean) {
                getView()?.getSeedingGoodsListSuccess(list)
            }

            override fun doOnFailure(list: SeedingBean) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                    super.doOnFailure(list)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }

}
