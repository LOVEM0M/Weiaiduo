package com.miyin.zhenbaoqi.ui.mine.presenter

import com.google.gson.JsonParseException
import com.miyin.greendao.FootprintEntityDao
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.sql.FootprintEntity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.mine.contract.MineContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import javax.security.auth.login.LoginException

class MinePresenter : BasePresenter<MineContract.IView>(), MineContract.IPresenter {

    override fun getUserType() {
        val userInfoObservable = RetrofitUtils.mApiService.userInfo()
        val userTypeObservable = RetrofitUtils.mApiService.userType()
        val userLevelObservable = RetrofitUtils.mApiService.userGrade()
        val disposable = Single.merge(userInfoObservable, userTypeObservable, userLevelObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.mark) {
                        "0" -> {
                            when (it) {
                                is UserTypeBean -> {
                                    getView()?.getUserTypeSuccess(it.user_type)
                                }
                                is UserInfoBean -> {
                                    getView()?.getUserInfoSuccess(it)
                                }
                                is UserGradeBean -> {
                                    getView()?.getUserLevelSuccess(it)
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

    override fun getMerchantInfo() {
        val merchantBaseInfoObservable = RetrofitUtils.mApiService.merchantBaseInfo()
        val merchantInfoObservable = RetrofitUtils.mApiService.merchantInfo()
        val merchantShopDataObservable = RetrofitUtils.mApiService.merchantShopData()
        val disposable = Single.merge(merchantBaseInfoObservable, merchantInfoObservable, merchantShopDataObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.mark) {
                        "0" -> {
                            when (it) {
                                is MerchantBean -> {
                                    getView()?.getMerchantInfoSuccess(it)
                                }
                                is MerchantBaseInfoBean -> {
                                    getView()?.getMerchantBaseInfoSuccess(it)
                                }
                                is MerchantShopDataBean -> {
                                    getView()?.getMerchantShopDataSuccess(it)
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

    override fun getSystemCustomer() {
        var chatId = "0"
        val disposable = RetrofitUtils.mApiService.chatCuscomerService()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    when {
                        it.mark == "0" -> {
                            chatId = it.customerServiceId ?: ""
                            val requestBody = JSONUtils.createJSON(arrayOf("dialogueToken"), arrayOf(it.dialogueToken
                                    ?: ""))
                            return@flatMap RetrofitUtils.mApiService.chatCustomerServiceCheck(requestBody)
                        }
                        it.mark == "500" -> throw LoginException("")
                        else -> throw Exception("")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<ResponseBean>() {
                    override fun doOnSuccess(data: ResponseBean) {
                        getView()?.getSystemCustomerSuccess(chatId)
                    }
                })
        getDisposable()?.add(disposable)
    }

    override fun getFootprintSize() {
        val disposable = Flowable.create(FlowableOnSubscribe<List<FootprintEntity>> {
            val dao = App.daoSession.footprintEntityDao
            val list = dao.queryBuilder()
                    .where(FootprintEntityDao.Properties.UserId.eq(SPUtils.getInt("user_id")))
                    .orderDesc()
                    .list()
            it.onNext(list)
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getView()?.getFootprintSizeSuccess(it.size)
                }, {
                    getView()?.getFootprintSizeSuccess(0)
                })
        getDisposable()?.add(disposable)
    }

    override fun getHomeGoodsHotList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.homeGoodsHotList(requestBody), object : BaseSingleObserver<HomeGoodsHotBean>() {
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

}
