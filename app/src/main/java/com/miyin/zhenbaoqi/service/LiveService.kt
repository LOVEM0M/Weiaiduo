package com.miyin.zhenbaoqi.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LiveService : Service() {

    private val mDisposable = CompositeDisposable()

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val disposable = RetrofitUtils.mApiService.liveRoomClose(SPUtils.getInt("room_id",127))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<ResponseBean>() {
                    override fun doOnSuccess(data: ResponseBean) {
                        stopSelf()
                    }

                    override fun doOnFailure(data: ResponseBean) {
                        stopSelf()
                    }

                    override fun onError(e: Throwable) {
                        stopSelf()
                    }
                })
        mDisposable.add(disposable)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mDisposable.clear()
        super.onDestroy()
    }

}
