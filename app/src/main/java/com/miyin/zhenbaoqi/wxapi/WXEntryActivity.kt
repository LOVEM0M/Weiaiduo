package com.miyin.zhenbaoqi.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : Activity(), IWXAPIEventHandler {

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
            var result = ""

            if (type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) { // 分享
                result = when (errCode) {
                    BaseResp.ErrCode.ERR_OK -> ""//"分享成功"
                    BaseResp.ErrCode.ERR_USER_CANCEL -> "用户取消"
                    BaseResp.ErrCode.ERR_AUTH_DENIED -> "分享失败"
                    else -> "未知错误"
                }
            } else if (type == ConstantsAPI.COMMAND_SENDAUTH) { // 登陆
                result = when (errCode) {
                    BaseResp.ErrCode.ERR_OK -> {
                        EventBus.getDefault().post("wx_login_success,${(baseResp as SendAuth.Resp).code}")
                        "登录成功"
                    }
                    BaseResp.ErrCode.ERR_USER_CANCEL -> "用户取消"
                    BaseResp.ErrCode.ERR_AUTH_DENIED -> "登录失败"
                    else -> "未知错误"
                }
            } else if (type == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) { // 小程序
                val launchMiniProResp = this as WXLaunchMiniProgram.Resp
                val extraData = launchMiniProResp.extMsg
                if (extraData.isNotEmpty()) {
                    EventBus.getDefault().post("program,$extraData")
                }
            }
            if (result.isNotEmpty()) {
                ToastUtils.showToast(result)
            }
            finish()
        }
    }

    override fun onReq(baseReq: BaseReq?) {

    }

}
