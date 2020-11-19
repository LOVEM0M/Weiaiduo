package com.miyin.zhenbaoqi.ui.sort.presenter

import android.util.ArrayMap
import com.miyin.greendao.FootprintEntityDao
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.sql.FootprintEntity
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsDetailContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.orhanobut.logger.Logger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class GoodsDetailPresenter : BasePresenter<GoodsDetailContract.IView>(), GoodsDetailContract.IPresenter {

    override fun getGoodsDetail(goodsId: Int) {
        val map = androidx.collection.ArrayMap<String, Any>().apply {
            put("goodsId", goodsId)
        }
        request(RetrofitUtils.mApiService.goodsDetail(map), object : BaseSingleObserver<GoodsDetailBean>() {
            override fun doOnSuccess(data: GoodsDetailBean) {
                getView()?.getGoodsDetailSuccess(data)
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

    override fun insertFootprint(goodsId: Int, goodsImg: String, goodsAmount: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("goods_id", goodsId)
            put("goods_img", goodsImg)
            put("goods_amount", goodsAmount)
        }
        val json = JSONUtils.gson.toJson(map)
        val disposable = Flowable.create(FlowableOnSubscribe<Boolean> { it ->
            val dao = App.daoSession.footprintEntityDao
            val list = dao.queryBuilder().where(FootprintEntityDao.Properties.UserId.eq(SPUtils.getInt("user_id"))).list()
            if (list.size > 200) {
                dao.deleteInTx(list[0])
            }
            val filterList = list.filter { it.goodsId == goodsId }
            if (filterList.isNotEmpty()) {
                dao.deleteInTx(filterList)
            }

            val entity = FootprintEntity().apply {
                userId = SPUtils.getInt("user_id")
                data = json
                this.goodsId = goodsId
                browseDate = TimeUtils.date2String(Date(), "yyyy-MM-dd")
            }
            val id = dao.insert(entity)
            it.onNext(id != 0L)
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        Logger.d("size == 插入成功")
                    } else {
                        Logger.d("size == 插入失败")
                    }
                }, {
                    Logger.d("size == error ${it.message}")
                })
        getDisposable()?.add(disposable)
    }

}
