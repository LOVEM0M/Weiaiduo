package com.miyin.zhenbaoqi.ui.login.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.SubAccountLoginBean
import com.miyin.zhenbaoqi.bean.UserSignBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.contract.SubAccountLoginContract
import com.miyin.zhenbaoqi.utils.SPUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

class SubAccountLoginPresenter : BasePresenter<SubAccountLoginContract.IView>(), SubAccountLoginContract.IPresenter {

    private var mRoomBean: SubAccountLoginBean.DataBean.RoomBean? = null

    override fun login(account: String?, password: String?) {
        if (account.isNullOrEmpty()) {
            getView()?.showToast("请输入账号")
            return
        }
        if (password.isNullOrEmpty()) {
            getView()?.showToast("请输入密码")
            return
        }

        getView()?.showDialog("正在登录...", false)
        val disposable = RetrofitUtils.mApiService.merchantsSubLogin(account, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    if (it.code == 0) {
                        mRoomBean = it.data?.room
                        var accountName: String? = null

                        it.data?.merchantsSub?.run {
                            accountName = account_name

                            val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
                            val hasMatch = pattern.matcher(account_name ?: "").matches()
                            val nickName = if (hasMatch) {
                                account_name!!.substring(0, 3) + "*" + account_name!!.substring(7, account_name!!.length)
                            } else {
                                account_name ?: ""
                            }

                            SPUtils.putInt("type", type)
                            SPUtils.putString("token", token ?: "")
                            SPUtils.putString("head_img", head_img ?: "")
                            SPUtils.putString("nick_name", nickName)
                            SPUtils.putString("phone", phone_no ?: "")
                            SPUtils.putInt("user_id", user_id)
                            SPUtils.putInt("merchant_id", merchants_id)
                            SPUtils.putString("account_name", account_name ?: "")
                        }
                        return@flatMap RetrofitUtils.mApiService.chatSubUserSign(accountName ?: "")
                    } else {
                        getView()?.hideDialog()
                        throw Exception(it.msg)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<UserSignBean>() {
                    override fun doOnSuccess(data: UserSignBean) {
                        val sign = data.usersig
                        val accountName = SPUtils.getString("account_name")
                        if (sign.isNullOrEmpty() || accountName.isEmpty()) {
                            getView()?.hideDialog()
                            getView()?.showToast("请重新登录")
                            return
                        }
                        SPUtils.putString("user_sig", sign)
                        timLogin(accountName, sign)
                    }

                    override fun doOnFailure(data: UserSignBean) {
                        getView()?.showToast(data.msg)
                        getView()?.hideDialog()
                    }

                    override fun onError(e: Throwable) {
                        getView()?.hideDialog()
                        super.onError(e)
                    }
                })
        getDisposable()?.add(disposable)
    }

    private fun timLogin(userId: String, userSign: String) {
        TIMManager.getInstance().login(userId, userSign, object : TIMCallBack {
            override fun onSuccess() {
                Logger.d("TIM 登录成功")
                getView()?.hideDialog()
                getView()?.loginSuccess(mRoomBean)
            }

            override fun onError(code: Int, desc: String?) {
                Logger.d("TIM 登录失败 code == $code, desc == $desc")
                getView()?.hideDialog()
                getView()?.loginSuccess(mRoomBean)
            }
        })
    }

}
