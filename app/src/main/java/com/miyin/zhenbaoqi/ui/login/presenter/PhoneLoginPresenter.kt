package com.miyin.zhenbaoqi.ui.login.presenter

import android.text.TextUtils
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.UserSignBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.contract.PhoneLoginContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class PhoneLoginPresenter : BasePresenter<PhoneLoginContract.IView>(), PhoneLoginContract.IPresenter {

    private val mCount = 60L

    override fun sendVerificationCode(phone: String?) {
        if (TextUtils.isEmpty(phone)) {
            getView()?.showToast("请输入手机号")
            return
        }

        getView()?.showDialog("正在发送验证码...")
        val requestBody = JSONUtils.createJSON(arrayOf("phone_no"), arrayOf(phone!!))
        request(RetrofitUtils.mApiService.sendMessage(requestBody), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                val disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                        .take(mCount + 1)
                        .map { mCount - it }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { getView()?.setCodeBtnEnabled(false) }
                        .doOnNext {
                            getView()?.hideDialog()
                            getView()?.setCodeBtnText("${it}s")
                        }
                        .doOnError {
                            getView()?.hideDialog()
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
                getView()?.hideDialog()
                getView()?.showToast(data.msg)
            }

            override fun onError(e: Throwable) {
                getView()?.hideDialog()
                super.onError(e)
            }
        })
    }

    override fun login(code: String?, phone: String?, pushCode: String?, inviteCode: String?) {
        if (code.isNullOrEmpty()) {
            getView()?.showToast("请输入验证码")
            return
        }
        if (phone.isNullOrEmpty()) {
            getView()?.showToast("请输入手机号")
            return
        }

        getView()?.showDialog("正在登录...", false)

        val keyArray = arrayOf("code", "phone_no", "push_code", "invitation_code")
        val valueArray = arrayOf<Any>(code, phone, pushCode ?: "11111111111111", inviteCode ?: "")
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        val disposable = RetrofitUtils.mApiService.login(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    if (it.code == 0) {
                        with(it) {
                            val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
                            val hasMatch = pattern.matcher(nick_name ?: "").matches()
                            val nickName = if (hasMatch) {
                                nick_name!!.substring(0, 3) + "*" + nick_name!!.substring(7, nick_name!!.length)
                            } else {
                                nick_name ?: ""
                            }

                            SPUtils.putString("token", token ?: "")
                            SPUtils.putString("head_img", head_img ?: "")
                            SPUtils.putString("nick_name", nickName)
                            SPUtils.putString("birthday", birthday ?: "")
                            SPUtils.putInt("gender", gender)
                            SPUtils.putInt("state", state)
                            SPUtils.putString("phone", phone_no ?: "")
                            SPUtils.putInt("user_id", user_id)
                            SPUtils.putInt("merchant_id", merchants_id)
                            SPUtils.putInt("quality_shop", quality_balance)
                        }
                        return@flatMap RetrofitUtils.mApiService.chatUserSign()
                    } else {
                        getView()?.hideDialog()
                        throw Exception(it.msg)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<UserSignBean>() {
                    override fun doOnSuccess(data: UserSignBean) {
                        val sign = data.usersig
                        if (sign.isNullOrEmpty()) {
                            getView()?.hideDialog()
                            return
                        }
                        SPUtils.putString("user_sig", sign)
                        timLogin(SPUtils.getInt("user_id").toString(), sign!!)
                    }

                    override fun doOnFailure(data: UserSignBean) {
                        getView()?.showToast(data.msg)
                        getView()?.hideDialog()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        getView()?.hideDialog()
                    }
                })
        getDisposable()?.add(disposable)
    }

    private fun timLogin(userId: String, userSign: String) {
        TIMManager.getInstance().login(userId, userSign, object : TIMCallBack {
            override fun onSuccess() {
                Logger.d("TIM 登录成功")
                getView()?.hideDialog()
                SPUtils.putBoolean("is_login", true)
                getView()?.onLogin()
            }

            override fun onError(code: Int, desc: String?) {
                Logger.d("TIM 登录失败 code == $code, desc == $desc")
                getView()?.hideDialog()
                SPUtils.putBoolean("is_login", true)
                getView()?.onLogin()
            }
        })
    }

}