package com.miyin.zhenbaoqi.base.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AliPayResultBean
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BasePayActivity<V : IBaseView, P : IBasePresenter<V>> : BaseMvpActivity<V, P>() {

    private var mIWXAPI: IWXAPI? = null

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        @Suppress("UNCHECKED_CAST")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = AliPayResultBean(msg.obj as Map<String, String>)

                    val resultStatus = payResult.resultStatus
                    // 判断 resultStatus 为 9000 则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast("支付成功")
                        onAliPaySuccess()
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast("支付结果确认中")
                        } else {
                            showToast("支付失败")
                        }
                    }
                }
            }
        }
    }

    override fun useEventBus() = true

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mIWXAPI = WXAPIFactory.createWXAPI(applicationContext, BuildConfig.WX_APP_ID, true)
    }

    protected fun onAliCallback(orderString: String) {
        object : Thread() {
            override fun run() {
                val aliPay = PayTask(this@BasePayActivity)
                // 调用支付接口，获取支付结果
                val result = aliPay.payV2(orderString, true)

                val msg = Message()
                msg.what = SDK_PAY_FLAG
                msg.obj = result
                mHandler.sendMessage(msg)
            }
        }.start()
    }

    protected fun onWXCallback(bean: PayResultBean) {
        WXOptionUtils.pay(mIWXAPI!!, BuildConfig.WX_APP_ID, bean)
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWXPayEvent(msg: String) {
        if (TextUtils.equals("wx_pay_success", msg)) {
            onWXPaySuccess()
        }
    }

    protected abstract fun onAliPaySuccess()

    protected abstract fun onWXPaySuccess()

    companion object {
        private const val SDK_PAY_FLAG = 1
    }

}
