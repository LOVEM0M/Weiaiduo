package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.greendao.AuctionGoodsDraftEntityDao
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerAuctionContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManagerAuctionPresenter : BasePresenter<ManagerAuctionContract.IView>(), ManagerAuctionContract.IPresenter {

    override fun getAuctionMerchantGoodsList(auctionState: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("auction_state", "current_page", "page_size")
        val valueArray = arrayOf<Any>(auctionState, currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantGoodsAuctionList(requestBody), object : BaseSingleObserver<MerchantGoodsBean>() {
            override fun doOnSuccess(data: MerchantGoodsBean) {
                getView()?.showNormal()
                getView()?.getAuctionMerchantGoodsListSuccess(data)
            }

            override fun doOnFailure(data: MerchantGoodsBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.tip)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showEmpty()
                }
            }
        })
    }

    override fun getDraftAuctionGoodsList(currentPage: Int, pageSize: Int) {
        val disposable = Flowable.create(FlowableOnSubscribe<List<AuctionGoodsDraftEntity>> {
            val dao = App.daoSession.auctionGoodsDraftEntityDao
            val list = dao.queryBuilder()
                    .where(AuctionGoodsDraftEntityDao.Properties.UserId.eq(SPUtils.getInt("user_id")))
                    .offset((currentPage - 1) * pageSize)
                    .limit(pageSize)
                    .list()
            it.onNext(list)
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getView()?.getDraftAuctionGoodsListSuccess()
                }, {
                    if (currentPage == 1) {
                        getView()?.showError()
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun updateMerchantGoodsState(goodsId: Int, state: Int) {
        val keyArray = arrayOf("goods_id", "state")
        val valueArray = arrayOf<Any>(goodsId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.updateMerchantGoodsState(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateMerchantGoodsStateSuccess(state)
            }
        })
    }

}
