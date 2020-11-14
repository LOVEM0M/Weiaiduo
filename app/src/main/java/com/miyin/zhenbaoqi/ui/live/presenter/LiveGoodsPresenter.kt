package com.miyin.zhenbaoqi.ui.live.presenter

import android.util.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveGoodsContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class LiveGoodsPresenter : BasePresenter<LiveGoodsContract.IView>(), LiveGoodsContract.IPresenter {

    override fun obtainLiveGoodsList(currentPage: Int, pageSize: Int, mTitle: String) {
        val map = ArrayMap<String, Any>().apply {
            put("current_page", currentPage)
            put("page_size", pageSize)
        }
        when (mTitle) {
            "全部宝贝" -> {

            }
            "编辑中的宝贝" -> {
                map["state"] = 1
            }
        }
        request(RetrofitUtils.mApiService.obtainLiveGoodsList(map), object : BaseSingleObserver<LiveGoodsBean>() {
            override fun doOnSuccess(data: LiveGoodsBean) {
                getView()?.showNormal()
                getView()?.obtainLiveGoodsDataSuccess(data)
            }

            override fun doOnFailure(data: LiveGoodsBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
                    getView()?.showEmptyView()
                } else {
                    getView()?.showToast(data.code.toString())
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(e.message)
                }
            }
        })
    }

    override fun updateShelfGoods(goods_id: Int, state: Int) {
        val keyArray = arrayOf("goods_id", "state")
        val valueArray = arrayOf<Any>(goods_id, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateMerchantGoodsState(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateShelfGoodsSuccess(state)
            }
        })
    }

    override fun obtainLiveRoomGoodsList(currentPage: Int, pageSize: Int, mTitle: String, room_id: Int) {

        var keyArray: Array<String> = emptyArray()
        var valueArray: Array<Any> = emptyArray()

        when (mTitle) {
            "秒杀中" -> {
                keyArray = arrayOf("current_page", "page_size", "state", "room_id")
                valueArray = arrayOf<Any>(currentPage, pageSize, 0, room_id)
            }
            "拍卖中" -> {
                keyArray = arrayOf("current_page", "page_size", "state", "room_id")
                valueArray = arrayOf<Any>(currentPage, pageSize, 1, room_id)
            }
        }
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.obtainLiveRoomGoodsList(requestBody), object : BaseSingleObserver<LiveGoodsBean>() {
            override fun doOnSuccess(data: LiveGoodsBean) {
                getView()?.showNormal()
                getView()?.obtainLiveGoodsDataSuccess(data)
            }

            override fun doOnFailure(data: LiveGoodsBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
                    getView()?.showEmptyView()
                } else {
                    getView()?.showToast(data.code.toString())
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(e.message)
                }
            }
        })
    }

}
