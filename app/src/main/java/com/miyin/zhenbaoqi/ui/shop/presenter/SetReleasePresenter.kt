package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.SetReleaseContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class SetReleasePresenter : BasePresenter<SetReleaseContract.IView>(), SetReleaseContract.IPresenter {

    override fun addMerchantGoods(cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int, endTime: Long,
                                  goodsAmount: String?, goodsDescribe: String?, goodsFreight: Int, goodsImg: String?,
                                  goodsName: String?, goodsOriginalAmount: String?, goodsType: Int, goodsVideo: String?,
                                  inventory: Int, isRestriction: Int, isSeven: Int, goodsId: Int, measure: String,
                                  texture: String, place: String, weight: String) {
        getView()?.showDialog("正在发布...", false)
        val keyArray = arrayOf("cate_id1", "cate_id2", "cate_id3", "commission_ratio", "end_time", "goods_amount",
                "goods_describe", "goods_freight", "goods_img", "goods_name", "goods_original_amount", "goods_type",
                "goods_video", "inventory", "is_restriction", "is_seven", "goods_id", "measure", "place", "texture", "weight")
        val img = goodsImg ?: ""
        val video = goodsVideo ?: ""
        val originalAmount = if (goodsOriginalAmount.isNullOrEmpty()) {
            0L
        } else {
            (goodsOriginalAmount.toDouble() * 100L).toLong()
        }
        val goodsPrice = if (goodsAmount.isNullOrEmpty()) {
            0L
        } else {
            (goodsAmount.toDouble() * 100L).toLong()
        }
        val valueArray = arrayOf<Any>(cateId1, cateId2, cateId3, commissionRatio, endTime, goodsPrice, goodsDescribe!!,
                goodsFreight, img, goodsName!!, originalAmount, goodsType, video, inventory, isRestriction, isSeven, goodsId, measure,
                place, texture, weight)

        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.addMerchantGoods(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.hideDialog()
                getView()?.addMerchantGoodsSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                super.doOnFailure(data)
                getView()?.hideDialog()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.hideDialog()
            }
        })
    }

}
