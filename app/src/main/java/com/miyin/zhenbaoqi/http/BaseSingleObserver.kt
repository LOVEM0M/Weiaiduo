package com.miyin.zhenbaoqi.http

import com.google.gson.JsonParseException
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.utils.ToastUtils
import io.reactivex.observers.DisposableSingleObserver
import java.net.ConnectException
import java.net.UnknownHostException
import javax.security.auth.login.LoginException

abstract class BaseSingleObserver<T : ResponseBean> : DisposableSingleObserver<T>() {

    override fun onSuccess(t: T) {
        when (t.code) {
            0 -> doOnSuccess(t)
            500 -> {
                if (t.msg != "服务器内部错误!") {
                    App.context.clearTask<WXLoginActivity>()
                } else {
                    if (t.msg.isNullOrEmpty()) {
                        t.msg = "未知错误"
                    }
                    doOnFailure(t)
                }
            }
            else -> {
                if (t.msg.isNullOrEmpty()) {
                    t.msg = "未知错误"
                }
                doOnFailure(t)
            }
        }
    }

    override fun onError(e: Throwable) {
        when (e) {
            is LoginException -> App.context.clearTask<WXLoginActivity>()
            is ConnectException -> ToastUtils.showToast("网络连接失败")
            is UnknownHostException -> ToastUtils.showToast("网络连接失败")
            is JsonParseException -> ToastUtils.showToast("JSON 数据解析异常")
            else -> ToastUtils.showToast(e.message ?: "未知错误")
        }
    }

    abstract fun doOnSuccess(data: T)

    protected open fun doOnFailure(data: T) {
        ToastUtils.showToast(data.msg ?: "未知错误")
    }

}
