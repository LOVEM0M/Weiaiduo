package com.miyin.zhenbaoqi.ui.login.presenter

import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.LoginBean
import com.miyin.zhenbaoqi.bean.UserSignBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.OkHttpUtils
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.login.contract.WXLoginContract
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

@Suppress("NAME_SHADOWING")
class WXLoginPresenter : BasePresenter<WXLoginContract.IView>(), WXLoginContract.IPresenter {

//    override fun wxLogin(code: String?) {
//        var code = code
//        if (code.isNullOrEmpty()) {
//            code = ""
//        }
//        val array = arrayOf<Pair<String, Any>>("appid" to BuildConfig.WX_APP_ID, "code" to code,
//                "secret" to Constant.SECRET, "grant_type" to "authorization_code")
//        OkHttpUtils.get("https://api.weixin.qq.com/sns/oauth2/access_token", array, {
//            try {
//                val jsonObject = JSONObject(it)
//                val access = jsonObject.getString("access_token")
//                val openId = jsonObject.getString("openid")
//                getUserInfo(access, openId)
//            } catch (e: JSONException) {
//                getView()?.showToast(e.message)
//            }
//        }, {
//            getView()?.showToast(it)
//        })
//    }
//
//    override fun appLogin(city: String, country: String, gender: String, headImg: String, nickName: String, openId: String, province: String, unionId: String) {
//        val keyArray = arrayOf("city", "country", "gender", "headImg", "nickName", "openId", "province", "unionId")
//        val valueArray = arrayOf<Any>(city, country, gender, headImg, nickName, openId, province, unionId)
//        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
//        request(RetrofitUtils.mApiService.bindWX(requestBody), object : BaseSingleObserver<LoginBean>() {
//            override fun doOnSuccess(data: LoginBean) {
//              /*  if (data.code == 0) {
//                    with(data) {
//                        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
//                        val hasMatch = pattern.matcher(nickName ?: "").matches()
//                        val nickName = if (hasMatch) {
//                            nickName!!.substring(0, 3) + "*" + nickName!!.substring(7, nickName!!.length)
//                        } else {
//                            nickName ?: ""
//                        }
//                        SPUtils.putInt("userId", userId)
//                        SPUtils.putString("phoneNo", phoneNo ?: "")
//                        SPUtils.putString("nickName", nickName)
//                        SPUtils.putString("headImg", headImg ?: "")
//                        SPUtils.putString("registerDate", registerDate ?: "")
//                        SPUtils.putInt("balance", balance)
//                        SPUtils.putString("token", token ?: "")
//                        SPUtils.putInt("vipType", vipType)
//                        SPUtils.putString("vipTime", vipTime ?: "")
//                    }
//                }*/
//                getView()?.appLoginSuccess(data)
//            }
//        })
//    }
//
//   /* override fun getUserSign() {
//        getView()?.showDialog("正在登录...")
//
//       request(RetrofitUtils.mApiService.chatUserSign(), object : BaseSingleObserver<UserSignBean>() {
//            override fun doOnSuccess(data: UserSignBean) {
//                val sign = data.usersig
//                if (sign.isNullOrEmpty()) {
//                    getView()?.hideDialog()
//                    return
//                }
//                SPUtils.putString("user_sig", sign)
//                timLogin(SPUtils.getInt("user_id").toString(), sign)
//            }
//
//            override fun doOnFailure(data: UserSignBean) {
//                getView()?.hideDialog()
//            }
//
//            override fun onError(e: Throwable) {
//                super.onError(e)
//                getView()?.hideDialog()
//            }
//        })
//    }*/
//
//    private fun getUserInfo(access: String, openId: String) {
//        val array = arrayOf<Pair<String, Any>>("access_token" to access, "openid" to openId)
//        OkHttpUtils.get("https://api.weixin.qq.com/sns/userinfo", array, {
//            if (it.isEmpty()) {
//                getView()?.showToast("未知错误")
//            } else {
//                getView()?.onWXLoginSuccess(it)
//            }
//        }, {
//            getView()?.showToast(it)
//        })
//    }
//
//    /*private fun timLogin(userId: String, userSign: String) {
//        TIMManager.getInstance().login(userId, userSign, object : TIMCallBack {
//            override fun onSuccess() {
//                getView()?.hideDialog()
//                SPUtils.putBoolean("is_login", true)
//                getView()?.getUserSignSuccess()
//            }
//
//            override fun onError(code: Int, desc: String?) {
//                getView()?.hideDialog()
//                SPUtils.putBoolean("is_login", true)
//                getView()?.getUserSignSuccess()
//            }
//        })
//    }*/

}

