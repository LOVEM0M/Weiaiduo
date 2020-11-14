package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.PushLiveContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PushLivePresenter : BasePresenter<PushLiveContract.IView>(), PushLiveContract.IPresenter {


    override fun uploadMerchantHead(path: String) {
        val list = uploadImg(path)
        request(RetrofitUtils.mApiService.uploadMerchantGoodsImg(list), object : BaseSingleObserver<ImageBean>() {
            override fun doOnSuccess(data: ImageBean) {
                getView()?.uploadMerchantHeadSuccess(data.photo_url!!)
            }
        })
    }

    override fun liveRoomCreate() {
        request(RetrofitUtils.mApiService.liveRoomCreate(), object : BaseSingleObserver<LiveRoomCreateBean>() {
            override fun doOnSuccess(data: LiveRoomCreateBean) {
                getView()?.liveRoomCreateSuccess(data)
            }
        })
    }

    override fun liveRoomOpen(roomId: Int, face_url: String?, room_name: String?) {
        request(RetrofitUtils.mApiService.liveRoomOpen(roomId, face_url, room_name), object : BaseSingleObserver<LiveRoomOpenBean>() {
            override fun doOnSuccess(data: LiveRoomOpenBean) {
                getView()?.liveRoomOpenSuccess(data)
            }
        })
    }

    override fun uploadLiveRoomCover(roomId: Int, file: File) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        builder.addFormDataPart("photo_file", file.name, requestBody)
        builder.addFormDataPart("room_id", roomId.toString())
        val list = builder.build().parts()
        request(RetrofitUtils.mApiService.liveRoomCover(list), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {

            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.showToast(data.msg)
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

                    override fun doOnFailure(data: ResponseBean) {
                        getView()?.showToast(data.msg)
                    }
                })
        getDisposable()?.add(disposable)
    }


    override fun obtainSharedList(currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.obtainShareList(currentPage, pageSize), object : BaseSingleObserver<LiveShareBean>() {
            override fun doOnSuccess(data: LiveShareBean) {
                getView()?.showNormal()
                getView()?.obtainShareListSuccess(data)
            }

            override fun doOnFailure(data: LiveShareBean) {
                if (currentPage == 1 && null == data.list) {
                    getView()?.showNormal()
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

    override fun obtainPeopleLevel() {
        request(RetrofitUtils.mApiService.userGrade(), object : BaseSingleObserver<UserGradeBean>() {
            override fun doOnSuccess(data: UserGradeBean) {
                getView()?.obtainPeopleLevelSuccess(data)
            }
        })
    }

    override fun shareLiveRoom(roomId: Int, isFriendCircle: Boolean) {
        request(RetrofitUtils.mApiService.shareLiveRoom(roomId), object : BaseSingleObserver<ResponseBean>() {
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
