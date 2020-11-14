package com.miyin.zhenbaoqi.ui.login.activity

import android.os.Bundle
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.LoginBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.MainActivity
import com.miyin.zhenbaoqi.ui.login.ProtocolDialog
import com.miyin.zhenbaoqi.ui.login.contract.WXLoginContract
import com.miyin.zhenbaoqi.ui.login.presenter.WXLoginPresenter
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_wx_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject

class WXLoginActivity : BaseMvpActivity<WXLoginContract.IView, WXLoginContract.IPresenter>(), WXLoginContract.IView {

    private lateinit var mIWXAPI: IWXAPI

    override fun useEventBus() = true

    override fun getContentView() = R.layout.activity_wx_login

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).init()

        mIWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)
        mIWXAPI.registerApp(BuildConfig.WX_APP_ID)

        tv_gift.text = SpanUtils()
                .append("新人注册成功，赠送")
                .append("376").setForegroundColor(0xFFCE9447.toInt())
                .setFontSize(17, true).setBold()
                .append("元优惠券")
                .create()

//        setOnClickListener(iv_wx_login, iv_phone_login, iv_sub_account_login)
        setOnClickListener(iv_phone_login)
    }

    override fun initData() {
        val isShowProtocol = SPUtils.getBoolean("is_show_protocol")
        if (!isShowProtocol) {
            val dialog = ProtocolDialog.newInstance()
            dialog.show(supportFragmentManager, "protocol")
        }
    }

    override fun createPresenter() = WXLoginPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.iv_wx_login -> WXOptionUtils.login(mIWXAPI)
            R.id.iv_phone_login -> startActivity<PhoneLoginActivity>()
//            R.id.iv_sub_account_login -> startActivity<SubAccountLoginActivity>()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWXLoginEvent(msg: String) {
        if (msg.contains(",")) {
            val list = msg.split(",")
            if ("wx_login_success" == list[0]) {
                mPresenter?.wxLogin(list[1])
            }
        }
    }

    override fun onWXLoginSuccess(json: String) {
        val jsonObject = JSONObject(json)
        val city = jsonObject.getString("city")
        val country = jsonObject.getString("country")
        val gender = jsonObject.getInt("sex")
        val headerImg = jsonObject.getString("headimgurl")
        val nickName = jsonObject.getString("nickname")
        val openId = jsonObject.getString("openid")
        val province = jsonObject.getString("province")
        val unionId = jsonObject.getString("unionid")
        mPresenter?.appLogin(city, country, gender.toString(), headerImg, nickName, openId, province, unionId)
    }

    override fun appLoginSuccess(bean: LoginBean) {
        with(bean) {
            if (state == 1) {
                startActivity<BindPhoneActivity>()
            } else {
                mPresenter?.getUserSign()
            }
        }
    }

    override fun getUserSignSuccess() {
        SPUtils.putBoolean("is_login", true)
        val userId = SPUtils.getInt("user_id")
        JPushInterface.setAlias(this, userId, "zbq_${userId}")

        startActivity<MainActivity>()
        finish()
    }

}
