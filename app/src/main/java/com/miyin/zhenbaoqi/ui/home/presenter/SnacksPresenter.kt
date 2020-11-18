package com.miyin.zhenbaoqi.ui.home.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.FirstCategorySecondBean
import com.miyin.zhenbaoqi.bean.SecondCategoryGoodsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.SnacksContract

class SnacksPresenter : BasePresenter<SnacksContract.IView>(), SnacksContract.IPresenter {
    override fun getFirstCategorySecondList(cateId1: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("cateId1", cateId1)
        }
        request(RetrofitUtils.mApiService.firstCategorySecond(map), object : BaseSingleObserver<FirstCategorySecondBean>() {
            override fun doOnSuccess(list: FirstCategorySecondBean) {
                getView()?.showNormal()
                getView()?.getFirstCategorySecondListSuccess(list)
            }

            override fun doOnFailure(list: FirstCategorySecondBean) {
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

    override fun getSecondCategoryGoodsList(cateId2: Int ,page: Int ,rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("cateId2", cateId2)
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.secondCategoryGoods(map), object : BaseSingleObserver<SecondCategoryGoodsBean>() {
            override fun doOnSuccess(list: SecondCategoryGoodsBean) {
                getView()?.showNormal()
                getView()?.getSecondCategoryGoodsListSuccess(list)
            }

            override fun doOnFailure(list: SecondCategoryGoodsBean) {
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
