package com.miyin.zhenbaoqi.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.miyin.zhenbaoqi.bean.UserSignBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConversationService : Service() {

    private val mDisposable = CompositeDisposable()
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            getSign()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        throw Exception("")
    }

    override fun onCreate() {
        super.onCreate()
//        getSign()
    }

    private fun getSign() {
        Logger.d("TIM 登陆 GET USER_SIGN")
//        val disposable = RetrofitUtils.mApiService.chatUserSign()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : BaseSingleObserver<UserSignBean>() {
//                    override fun doOnSuccess(data: UserSignBean) {
//                        val sign = data.usersig
//                        if (sign.isNullOrEmpty()) {
//                            mHandler.sendEmptyMessageDelayed(0, 5 * 1000)
//                        } else {
//                            SPUtils.putString("user_sig", sign)
//                            timLogin(sign)
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//                        super.onError(e)
//                        mHandler.sendEmptyMessageDelayed(0, 5 * 1000)
//                    }
//                })
//        mDisposable.add(disposable)
    }

    private fun timLogin(sign: String) {
        val userId = SPUtils.getInt("user_id")
        TIMManager.getInstance().login(userId.toString(), sign, object : TIMCallBack {
            override fun onSuccess() {
                Logger.d("TIM 登陆 Login Success")
                stopSelf()
            }

            override fun onError(p0: Int, p1: String?) {
                Logger.d("TIM 登陆 Login Failure")
                mHandler.sendEmptyMessageDelayed(0, 5 * 1000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("TIM 登陆 Service Destroy")
        mDisposable.clear()
        mHandler.removeCallbacksAndMessages(null)
    }

}
