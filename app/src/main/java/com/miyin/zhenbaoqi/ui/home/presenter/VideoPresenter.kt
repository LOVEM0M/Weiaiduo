package com.miyin.zhenbaoqi.ui.home.presenter

import com.google.gson.JsonParseException
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.VideoContract
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

class VideoPresenter : BasePresenter<VideoContract.IView>(), VideoContract.IPresenter {

    override fun getVideoDetail(videoId: Int, merchantId: Int) {
        val videoDetail = RetrofitUtils.mApiService.merchantVideoDetail(videoId)
        val checkBlackList = RetrofitUtils.mApiService.merchantCheckBlackList(SPUtils.getInt("user_id"), merchantId)
        val disposable = Single.merge(videoDetail, checkBlackList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.mark) {
                        "0" -> {
                            when (it) {
                                is VideoDetailBean -> {
                                    getView()?.getVideoDetailSuccess(it)
                                }
                                is CheckBlackListBean -> {
                                    getView()?.checkBlackListSuccess(it)
                                }
                            }
                        }
                        "500" -> App.context.clearTask<WXLoginActivity>()
                        else -> getView()?.showToast(it.tip)
                    }
                }, {
                    when (it) {
                        is ConnectException -> getView()?.showToast("网络连接失败")
                        is JsonParseException -> getView()?.showToast("JSON 数据解析异常")
                        else -> getView()?.showToast(it.message)
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun getReplyList(videoId: Int, currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.videoReplyList(videoId, currentPage, pageSize), object : BaseSingleObserver<VideoReplyBean>() {
            override fun doOnSuccess(data: VideoReplyBean) {
                getView()?.hideDialog()
                getView()?.getReplyListSuccess(data)
            }

            override fun doOnFailure(data: VideoReplyBean) {
                super.doOnFailure(data)
                getView()?.hideDialog()
                if (currentPage == 1) {
                    getView()?.getReplyListSuccess(data)
                }
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun addReply(replyId: Int, replyContent: String) {
        request(RetrofitUtils.mApiService.addVideoReply(replyId, replyContent), object : BaseSingleObserver<SingleCommentBean>() {
            override fun doOnSuccess(data: SingleCommentBean) {
                getView()?.addReplySuccess(data)
            }
        })
    }

    override fun getChildReplyList(replyId: Int, currentPage: Int, pageSize: Int) {
        request(RetrofitUtils.mApiService.videoChildReplyList(replyId, currentPage, pageSize), object : BaseSingleObserver<VideoReplyBean>() {
            override fun doOnSuccess(data: VideoReplyBean) {
                getView()?.getChildReplyListSuccess(data)
            }
        })
    }

    override fun addChildReply(replyId: Int, replyContent: String) {
        request(RetrofitUtils.mApiService.addVideoChildReply(replyId, replyContent), object : BaseSingleObserver<SingleCommentBean>() {
            override fun doOnSuccess(data: SingleCommentBean) {
                getView()?.addChildReplySuccess(data)
            }
        })
    }

    override fun addLikes(likeId: Int) {
        request(RetrofitUtils.mApiService.addVideoLikes(likeId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateLikesSuccess(0)
            }
        })
    }

    override fun deleteLike(likeId: Int) {
        request(RetrofitUtils.mApiService.deleteVideoLikes(likeId), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.updateLikesSuccess(1)
            }
        })
    }

    override fun updateMerchantState(merchantId: Int, focusState: Int) {
        val state = if (focusState == 0) 1 else 0
        val keyArray = arrayOf("merchants_id", "focus_state")
        val valueArray = arrayOf<Any>(merchantId, state)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.merchantAttention(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.showToast(data.tip)
                getView()?.updateMerchantStateSuccess(state)
            }
        })
    }

}
