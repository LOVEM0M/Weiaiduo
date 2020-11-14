package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity
import com.miyin.zhenbaoqi.ui.shop.contract.SetAuctionContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SetAuctionPresenter : BasePresenter<SetAuctionContract.IView>(), SetAuctionContract.IPresenter {

    override fun addAuctionGoods(addAmount: Long, cateId1: Int, cateId2: Int, cateId3: Int, commissionRatio: Int, endTime: Long,
                                 ensureAmount: Int, goodsAmount: Long, goodsDescribe: String?, goodsFreight: Int, goodsImg: String?,
                                 goodsName: String?, goodsOriginalAmount: Long, goodsVideo: String?, isSeven: Int, startAmount: Long,
                                 startTime: Long, measure: String, texture: String, place: String, weight: String) {
        val keyArray = arrayOf("add_amount", "cate_id1", "cate_id2", "cate_id3", "commission_ratio", "end_time",
                "ensure_amount", "goods_amount", "goods_describe", "goods_freight", "goods_img", "goods_name",
                "goods_original_amount", "goods_video", "is_seven", "start_amount", "start_time", "measure", "place", "texture", "weight")
        val img = goodsImg ?: ""
        val video = goodsVideo ?: ""
        val desc = goodsDescribe ?: ""
        val valueArray = arrayOf<Any>(addAmount, cateId1, cateId2, cateId3, commissionRatio, endTime, ensureAmount,
                goodsAmount, desc, goodsFreight, img, goodsName ?: "", goodsOriginalAmount, video,
                isSeven, startAmount, startTime, measure, place, texture, weight)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.addAuctionMerchantGoods(requestBody), object : BaseSingleObserver<GoodsDetailBean>() {
            override fun doOnSuccess(data: GoodsDetailBean) {
                getView()?.hideDialog()
                getView()?.addAuctionGoodsSuccess()
            }

            override fun doOnFailure(data: GoodsDetailBean) {
                getView()?.hideDialog()
                if (data.code == 1) {
                    super.doOnFailure(data)
                } else {
                    getView()?.showToast("未知错误，已保存至草稿箱")
                    getView()?.onFailure("", 0)
                }
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                getView()?.onFailure("", 0)
                super.onError(e)
            }
        })
    }

    override fun saveAuctionGoods(json: String) {
        getView()?.showDialog("正在保存...", false)
        val disposable = Flowable.create(FlowableOnSubscribe<Boolean> {
            val dao = App.daoSession.auctionGoodsDraftEntityDao
            val entity = AuctionGoodsDraftEntity().apply {
                userId = SPUtils.getInt("user_id")
                data = json
            }
            val entityId = dao.insert(entity)
            if (entityId != 0L) {
                it.onNext(true)
            } else {
                it.onError(Throwable("保存失败"))
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getView()?.hideDialog()
                    getView()?.saveAuctionGoodsSuccess()
                }, {
                    getView()?.hideDialog()
                    getView()?.showToast(it.message)
                })
        getDisposable()?.add(disposable)
    }

}
