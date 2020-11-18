package com.miyin.zhenbaoqi.ui.home.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.bean.takeThreeVipBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.NewVipContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class NewVipPresenter :BasePresenter<NewVipContract.IView>(), NewVipContract.IPresenter {
    override fun getVipFirstFreegoodsList(page: Int, rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.vipFirstFreegoodsList(map), object : BaseSingleObserver<VipFirstFreegoodsBean>() {
            override fun doOnSuccess(list: VipFirstFreegoodsBean) {
                getView()?.showNormal()
                getView()?.getVipFirstFreegoodsListSuccess(list)
            }

            override fun doOnFailure(list: VipFirstFreegoodsBean) {
                if (page == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (page == 1) {
                    getView()?.showError()
                }
            }
        })
    }

}
