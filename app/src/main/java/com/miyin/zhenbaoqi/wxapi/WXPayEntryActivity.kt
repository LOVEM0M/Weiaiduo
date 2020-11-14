package com.miyin.zhenbaoqi.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var mWXAPI: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)
        mWXAPI.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        mWXAPI.handleIntent(intent, this)
    }

    override fun onResp(baseResp: BaseResp?) {
        baseResp?.run {
            if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                when (errCode) {
                    0 -> {
                        Logger.d("微信支付成功")
                        EventBus.getDefault().post("wx_pay_success")
                    }
                    -2 -> ToastUtils.showToast("取消支付")
                    else -> ToastUtils.showToast("支付失败")
                }
                finish()
            }
        }
    }

    override fun onReq(baseReq: BaseReq?) {

    }

}
