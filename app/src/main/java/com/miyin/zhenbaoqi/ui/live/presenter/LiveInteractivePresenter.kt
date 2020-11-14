package com.miyin.zhenbaoqi.ui.live.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.live.contract.LiveInteractiveContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LiveInteractivePresenter : BasePresenter<LiveInteractiveContract.IView>(), LiveInteractiveContract.IPresenter {

    override fun getAuctionGoods(roomId: Int) {
        request(RetrofitUtils.mApiService.liveRoomAuctionGoods(roomId), object : BaseSingleObserver<GoodsDetailBean>() {
            override fun doOnSuccess(data: GoodsDetailBean) {
                getView()?.getAuctionGoodsSuccess(data)
            }
        })
    }

    override fun getLiveRoomPopularity(roomId: Int) {
        request(RetrofitUtils.mApiService.liveRoomPopularity(roomId), object : BaseSingleObserver<LiveRoomPopularityBean>() {
            override fun doOnSuccess(data: LiveRoomPopularityBean) {
                getView()?.getLiveRoomPopularitySuccess(data.popularity)
            }
        })
    }

    override fun closeLiveRoom(roomId: Int) {
        val disposable = RetrofitUtils.mApiService.liveRoomClose(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<ResponseBean>() {
                    override fun doOnSuccess(data: ResponseBean) {
                        getView()?.closeLiveRoomSuccess()
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun getMerchantList(merchantId: Int, state: Int) {
        val keyArray = arrayOf("merchants_id", "focus_state")
        val valueArray = arrayOf<Any>(merchantId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAttention(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showNormal()
                getView()?.getMerchantListSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
            }
        })
    }

    override fun obtainPeopleLevel() {
        request(RetrofitUtils.mApiService.userGrade(), object : BaseSingleObserver<UserGradeBean>() {
            override fun doOnSuccess(data: UserGradeBean) {
                getView()?.obtainPeopleLevelSuccess(data)
            }
        })
    }


    override fun obtainSharedList() {
        val currentPage = 1
        val pageSize = 3
        request(RetrofitUtils.mApiService.obtainShareList(currentPage, pageSize), object : BaseSingleObserver<LiveShareBean>() {
            override fun doOnSuccess(data: LiveShareBean) {
                getView()?.showNormal()
                if (data.list == null) {
                    return
                }
                getView()?.obtainShareListSuccess(data)
            }

            override fun doOnFailure(data: LiveShareBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
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

    override fun checkBlackList(userId: Int, merchantId: Int) {
        request(RetrofitUtils.mApiService.merchantCheckBlackList(userId, merchantId), object : BaseSingleObserver<CheckBlackListBean>() {
            override fun doOnSuccess(data: CheckBlackListBean) {
                getView()?.checkBlackListSuccess(data)
            }
        })
    }

    override fun openRoom(roomId: Int, face_url: String, room_name: String) {
        request(RetrofitUtils.mApiService.liveRoomOpen(roomId, face_url, room_name), object : BaseSingleObserver<LiveRoomOpenBean>() {
            override fun doOnSuccess(data: LiveRoomOpenBean) {
                getView()?.openRoomSuccess()
            }
        })
    }

    override fun obtainLiveInfoData(merchantId: Int) {
        request(RetrofitUtils.mApiService.seeMerchantInfo(merchantId), object : BaseSingleObserver<MerchantInfoBean>() {
            override fun doOnSuccess(data: MerchantInfoBean) {
                getView()?.obtainLiveInfoSuccess(data)
            }

        })
    }

    override fun auctionBindGoods(goodsId: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("goods_id"), arrayOf(goodsId))
        request(RetrofitUtils.mApiService.auctionBindOffer(requestBody), object : BaseSingleObserver<BindOfferBean>() {
            override fun doOnSuccess(data: BindOfferBean) {
                getView()?.auctionBindGoodsSuccess(data)
            }
        })
    }

    override fun shareLiveRoom(room_id: Int, isFriendCircle: Boolean) {
        request(RetrofitUtils.mApiService.shareLiveRoom(room_id), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.shareLiveRoomSuccess(isFriendCircle)
            }
        })
    }

    override fun getAuctionList(goodsId: Int, goodsImg: String, goodsName: String, goodsFreight: Int) {
        val requestBody = JSONUtils.createJSON(arrayOf("goods_id"), arrayOf(goodsId))
        request(RetrofitUtils.mApiService.auctionGoodsList(requestBody), object : BaseSingleObserver<AuctionGoodsRecordBean>() {
            override fun doOnSuccess(data: AuctionGoodsRecordBean) {
                getView()?.getAuctionListSuccess(data, goodsId, goodsImg, goodsName, goodsFreight)
            }

            override fun doOnFailure(data: AuctionGoodsRecordBean) {
                getView()?.getAuctionListSuccess(data, goodsId, goodsImg, goodsName, goodsFreight)
            }
        })
    }

}
