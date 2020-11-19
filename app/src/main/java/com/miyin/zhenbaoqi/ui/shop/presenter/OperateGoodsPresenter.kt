package com.miyin.zhenbaoqi.ui.shop.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.OperateGoodsContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class OperateGoodsPresenter : BasePresenter<OperateGoodsContract.IView>(), OperateGoodsContract.IPresenter {

    override fun uploadImage(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadFeedbackImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadImageSuccess(data.photo_url ?: "")
            }

            override fun doOnFailure(data: ImageBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
            }
        })
    }

    override fun liveGoodsAuctionInsert(addAmount: Long, cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int,
                                        endTime: Long, ensureAmount: Int, goodsAmount: Int, goodsDescribe: String, goodsFreight: Int,
                                        goodsImg: String, goodsName: String, goodsOriginalAmount: Long, goodsVideo: String, isSeven: Int,
                                        istype: Int, roomId: Int, startAmount: Int, startTime: Long) {
        val keyArray = arrayOf("add_amount", "cate_id1", "cate_id2", "cate_id3", "commission_ratio", "end_time", "ensure_amount", "goods_amount",
                "goods_describe", "goods_freight", "goods_img", "goods_name", "goods_original_amount", "goods_video", "is_seven",
                "istype", "room_id", "start_amount", "start_time")
        val valueArray = arrayOf<Any>(addAmount, cateId1, cateId2, cateId3, commissionRatio, endTime, ensureAmount, goodsAmount, goodsDescribe,
                goodsFreight, goodsImg, goodsName, goodsOriginalAmount, goodsVideo, isSeven, istype, roomId, startAmount, startTime)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.addAuctionMerchantGoods(requestBody), object : BaseSingleObserver<GoodsDetailBean>() {
            override fun doOnSuccess(data: GoodsDetailBean) {
                getView()?.hideDialog()
                getView()?.liveGoodsAuctionInsertSuccess(data)
            }

            override fun doOnFailure(data: GoodsDetailBean) {
                getView()?.hideDialog()
                super.doOnFailure(data)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun liveGoodsSpikeInsert(goodsAmount: Int, goodsDescribe: String, goodsFreight: Int, goodsImg: String?, goodsName: String?,
                                      inventory: Int, isSeven: Int, roomId: Int, isRestriction: Int, goodsId: Int) {
        if (goodsImg.isNullOrEmpty()) {
            getView()?.showToast("请上传商品图片")
            return
        }
        if (goodsName.isNullOrEmpty()) {
            getView()?.showToast("请填写商品名称")
            return
        }

        val keyArray = arrayOf("goods_amount", "goods_describe", "goods_freight", "goods_img", "goods_name", "is_seven", "room_id", "inventory", "is_restriction", "goods_id")
        val valueArray = arrayOf<Any>(goodsAmount, goodsDescribe, goodsFreight, goodsImg, goodsName, isSeven, roomId, inventory, isRestriction, goodsId)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.liveGoodsSpikeInsert(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
                getView()?.liveGoodsSpikeInsertSuccess()
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun getParentList(type: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("type", type)
        }
        request(RetrofitUtils.mApiService.parentList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getParentListSuccess(data.data!!)
            }
        })
    }

    override fun getSonList(parentId: Int, type: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("parentId", parentId)
        }
        request(RetrofitUtils.mApiService.sonList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getSonListSuccess(data.data!!, type)
            }
        })
    }

}
