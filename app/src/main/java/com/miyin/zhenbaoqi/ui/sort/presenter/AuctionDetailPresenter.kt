package com.miyin.zhenbaoqi.ui.sort.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.AuctionDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class AuctionDetailPresenter : BasePresenter<AuctionDetailContract.IView>(), AuctionDetailContract.IPresenter {

    override fun getAuctionGoodsDetail(goodsId: Int) {
        val map = androidx.collection.ArrayMap<String, Any>().apply {
            put("goodsId", goodsId)
        }
        request(RetrofitUtils.mApiService.goodsDetail(map), object : BaseSingleObserver<GoodsDetailBean>() {
            override fun doOnSuccess(data: GoodsDetailBean) {
                getView()?.getAuctionGoodsDetailSuccess(data)
            }
        })
    }

    override fun getAuctionGoodsRecord(goodsId: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("goods_id", "current_page", "page_size")
        val valueArray = arrayOf<Any>(goodsId, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.auctionGoodsList(requestBody), object : BaseSingleObserver<AuctionGoodsRecordBean>() {
            override fun doOnSuccess(data: AuctionGoodsRecordBean) {
                getView()?.getAuctionGoodsRecordSuccess(data)
            }

            override fun doOnFailure(data: AuctionGoodsRecordBean) {
                getView()?.onFailure("auctionGoodsRecord", 0)
            }
        })
    }

    override fun getAuctionGoodsList(goodsId: Int) {
        request(RetrofitUtils.mApiService.auctionGoodsRecommendList(goodsId), object : BaseSingleObserver<AuctionGoodsBean>() {
            override fun doOnSuccess(data: AuctionGoodsBean) {
                getView()?.getAuctionGoodsListSuccess(data)
            }
        })
    }

    override fun auctionSecurityDeposit(goodsId: Int, payType: Int) {
        val keyArray = arrayOf("goods_id", "pay_type")
        val valueArray = arrayOf<Any>(goodsId, payType)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.auctionBind(requestBody), object : BaseSingleObserver<PayResultBean>() {
            override fun doOnSuccess(data: PayResultBean) {
                getView()?.auctionSecurityDepositSuccess(data, payType)
            }
        })
    }

    override fun auctionBindGoods(goodsId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("goods_id"), arrayOf(goodsId))
        request(RetrofitUtils.mApiService.auctionBindOffer(requestBody), object : BaseSingleObserver<BindOfferBean>() {
            override fun doOnSuccess(data: BindOfferBean) {
                getView()?.showToast(data.msg)
                getView()?.auctionBindGoodsSuccess()
            }
        })
    }

    override fun callHandleGoods() {
        request(RetrofitUtils.mApiService.callHandleGoods(), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.callHandleGoodsSuccess()
            }
        })
    }

    override fun updateMerchantState(focusState: Int, merchantId: Int) {
        val state = if (focusState == 0) 1 else 0
        val keyArray = arrayOf("merchants_id", "focus_state")
        val valueArray = arrayOf<Any>(merchantId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAttention(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.updateMerchantStateSuccess(state)
            }
        })
    }

    override fun updateCollectState(goodsId: Int, collectState: Int) {
        val state = if (collectState == 0) 1 else 0
        val keyArray = arrayOf("goods_id", "collection_state")
        val valueArray = arrayOf<Any>(goodsId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.goodsCollect(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.msg)
                getView()?.updateCollectStateSuccess(state)
            }
        })
    }

}
