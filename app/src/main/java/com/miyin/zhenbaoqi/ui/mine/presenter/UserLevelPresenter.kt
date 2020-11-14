package com.miyin.zhenbaoqi.ui.mine.presenter

import com.google.gson.JsonParseException
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.mine.contract.UserLevelContract
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import javax.security.auth.login.LoginException

class UserLevelPresenter : BasePresenter<UserLevelContract.IView>(), UserLevelContract.IPresenter {

    override fun getUserLevel() {
        val userGrade = RetrofitUtils.mApiService.userGrade()
        val userLevelList = RetrofitUtils.mApiService.pointRuleList()
        val disposable = Single.merge(userGrade, userLevelList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        0 -> getView()?.getUserLevelSuccess(it)
                        500 -> App.context.clearTask<WXLoginActivity>()
                        else -> getView()?.showToast(it.msg)
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

}
