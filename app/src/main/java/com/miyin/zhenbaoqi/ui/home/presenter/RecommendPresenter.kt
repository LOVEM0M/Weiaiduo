package com.miyin.zhenbaoqi.ui.home.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.RecommendContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class RecommendPresenter : BasePresenter<RecommendContract.IView>(), RecommendContract.IPresenter {

    override fun canGetCoupon() {
        request(RetrofitUtils.mApiService.welfareCouponCanGet(), object : BaseSingleObserver<CouponGetBean>() {
            override fun doOnSuccess(data: CouponGetBean) {
                getView()?.canGetCouponSuccess(data)
            }

            override fun doOnFailure(data: CouponGetBean) {

            }
        })
    }

    override fun homeBanner() {
        request(RetrofitUtils.mApiService.homeBanner(), object : BaseSingleObserver<HomeBannerBean>() {
            override fun doOnSuccess(data: HomeBannerBean) {
                getView()?.getHomeBannerSuccess(data)
            }

            override fun doOnFailure(data: HomeBannerBean) {

            }
        })
    }

    override fun getHotList() {
        request(RetrofitUtils.mApiService.obtainHotList(1, 6), object : BaseSingleObserver<LiveHotBean>() {
            override fun doOnSuccess(data: LiveHotBean) {
                getView()?.getHotListSuccess(data)
            }

            override fun doOnFailure(data: LiveHotBean) {
                getView()?.onFailure("hotLive", 1)
            }

            override fun onError(e: Throwable) {
                getView()?.onFailure("hotLive", 1)
            }
        })
    }

    override fun getHomeGoodsHotList(currentPage: Int, pageSize: Int) {

        request(RetrofitUtils.mApiService.homeGoodsHotList(), object : BaseSingleObserver<HomeGoodsHotBean>() {
            override fun doOnSuccess(data: HomeGoodsHotBean) {
                getView()?.showNormal()
                getView()?.getHomeGoodsHotListSuccess(data)
            }

            override fun doOnFailure(data: HomeGoodsHotBean) {
                if (currentPage == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(data)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }

    override fun liveRoomEntry(roomId: Int, position: Int) {
        request(RetrofitUtils.mApiService.liveRoomEntry(roomId), object : BaseSingleObserver<LiveEntryRoomBean>() {
            override fun doOnSuccess(data: LiveEntryRoomBean) {
                getView()?.liveRoomEntrySuccess(data, position)
            }
        })
    }

    override fun bannerClick(bannerId: Int, position: Int) {
        request((RetrofitUtils.mApiService.bannerClick(bannerId)), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.bannerClickSuccess(position)
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.onFailure("", 3)
            }

            override fun onError(e: Throwable) {
                getView()?.onFailure("", 3)
                super.onError(e)
            }
        })
    }

}
