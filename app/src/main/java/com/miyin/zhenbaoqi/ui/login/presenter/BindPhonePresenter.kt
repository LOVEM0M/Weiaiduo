package com.miyin.zhenbaoqi.ui.login.presenter

import android.text.TextUtils
import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LoginBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.UserSignBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.contract.BindPhoneContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class BindPhonePresenter : BasePresenter<BindPhoneContract.IView>(), BindPhoneContract.IPresenter {

    private val mCount = 60L

    override fun sendVerificationCode(phone: String?) {
        if (TextUtils.isEmpty(phone)) {
            getView()?.showToast("请输入手机号")
            return
        }

        val map = ArrayMap<String, Any>().apply {
            put("phoneNo", phone)
        }
        request(RetrofitUtils.mApiService.sendMessage(map), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                val disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                        .take(mCount + 1)
                        .map { mCount - it }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { getView()?.setCodeBtnEnabled(false) }
                        .doOnNext { getView()?.setCodeBtnText("${it}s") }
                        .doOnError {
                            getView()?.setCodeBtnText("获取验证码")
                            getView()?.setCodeBtnEnabled(true)
                        }
                        .doOnComplete {
                            getView()?.setCodeBtnText("获取验证码")
                            getView()?.setCodeBtnEnabled(true)
                        }
                        .subscribe()
                getDisposable()?.add(disposable)
            }

            override fun doOnFailure(data: ResponseBean) {
                getView()?.showToast(data.msg)
            }
        })
    }

    override fun login(code: String?, phone: String?) {
        if (code.isNullOrEmpty()) {
            getView()?.showToast("请输入验证码")
            return
        }
        if (phone.isNullOrEmpty()) {
            getView()?.showToast("请输入手机号")
            return
        }

        getView()?.showDialog("正在登录...", false)

        val map = ArrayMap<String, Any>().apply {
            put("code", code)
            put("phoneNo", phone)
        }
        request(RetrofitUtils.mApiService.login(map), object : BaseSingleObserver<LoginBean>() {
            override fun doOnSuccess(list : LoginBean) {
                if (list.code == 200) {
                    with(list.data) {
                        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
                        val hasMatch = pattern.matcher(this?.nickName ?: "").matches()
                        val nickName = if (hasMatch) {
                            this?.nickName!!.substring(0, 3) + "*" + this?.nickName!!.substring(7, nickName!!.length)
                        } else {
                            this?.nickName ?: ""
                        }
                        SPUtils.putInt("userId", this?.userId!!)
                        SPUtils.putString("phoneNo", phoneNo ?: "")
                        SPUtils.putString("nickName", nickName)
                        SPUtils.putString("headImg", headImg ?: "")
                        SPUtils.putString("registerDate", registerDate ?: "")
                        SPUtils.putFloat("balance", this?.balance.toFloat())
                        SPUtils.putString("token", token ?: "")
                        SPUtils.putInt("vipType", vipType)
                        SPUtils.putString("vipTime", vipTime ?: "")
                    }
                }
            }

            override fun doOnFailure(data: LoginBean) {
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    private fun timLogin(userId: String, userSign: String) {
        TIMManager.getInstance().login(userId, userSign, object : TIMCallBack {
            override fun onSuccess() {
                getView()?.hideDialog()
                SPUtils.putBoolean("is_login", true)
                getView()?.onLogin()
            }

            override fun onError(code: Int, desc: String?) {
                getView()?.hideDialog()
                SPUtils.putBoolean("is_login", true)
                getView()?.onLogin()
            }
        })
    }

}
