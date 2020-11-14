package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.greendao.AuctionGoodsDraftEntityDao
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity
import com.miyin.zhenbaoqi.ui.shop.contract.DraftGoodsContract
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DraftGoodsPresenter : BasePresenter<DraftGoodsContract.IView>(), DraftGoodsContract.IPresenter {

    override fun getDraftGoodsList(currentPage: Int, pageSize: Int) {
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
                    getView()?.getDraftGoodsListSuccess(it)
                }, {
                    if (currentPage == 1) {
                        getView()?.showError()
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun deleteDraftGoods(id: Long) {
        val disposable = Flowable.create(FlowableOnSubscribe<Boolean> {
            val dao = App.daoSession.auctionGoodsDraftEntityDao
            dao.deleteByKey(id)
            it.onNext(true)
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getView()?.deleteDraftGoodsSuccess()
                }, {
                    getView()?.showToast(it.message)
                })
        getDisposable()?.add(disposable)
    }

}