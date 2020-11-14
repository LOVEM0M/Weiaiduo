package com.miyin.zhenbaoqi.ui.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.webkit.*
import com.miyin.zhenbaoqi.BuildConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.utils.WXOptionUtils
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_web.*
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.ArrayMap
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.http.OkHttpUtils
import com.miyin.zhenbaoqi.ui.mine.activity.BecomeMerchantActivity
import com.miyin.zhenbaoqi.ui.shop.activity.security_deposit.SecurityDepositActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

@Suppress("OverridingDeprecatedMember")
class WebActivity : BaseActivity(), OnDialogCallback {

    private lateinit var mWXAPI: IWXAPI
    private var mTitle: String? = null
    private var mUrl: String? = null
    private var mWebView: WebView? = null

    companion object {
        fun openActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, WebActivity::class.java).apply {
                putExtra("title", title)
                putExtra("url", url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getContentView(): Int {
        with(intent) {
            mTitle = getStringExtra("title")
            mUrl = getStringExtra("url")
        }
        return R.layout.activity_web
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        val rightTitle = when (mTitle) {
            "申请认证品质店铺" -> "质保金说明"
            else -> ""
        }
        if (rightTitle.isEmpty()) {
            initTitleBar(mTitle!!, rightImage = R.drawable.ic_refresh_gray)
        } else {
            initTitleBar(mTitle!!, rightTitle = rightTitle)
        }

        immersionBar { statusBarDarkFont(true) }
        mWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, true)

        mWebView = WebView(applicationContext).apply {
            scrollBarStyle = WebView.SCROLLBARS_INSIDE_OVERLAY
            overScrollMode = WebView.OVER_SCROLL_NEVER
            val settings = settings
            with(settings) {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_NO_CACHE
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.proceed()
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                    AlertDialog.Builder(this@WebActivity)
                            .setTitle("提示")
                            .setMessage(message)
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ -> dialog.dismiss() }
                            .setCancelable(true)
                            .show()
                    return true
                }
            }
            clearCache(true)
            loadUrl(mUrl)
            addJavascriptInterface(JavaScriptInterface(), "AndroidJs")
        }
        fl_container.addView(mWebView)
    }

    override fun initData() {

    }

    override fun onRightClick() {
        when (mTitle) {
            "申请认证品质店铺" -> openActivity(this, "质保金说明", Constant.WARRANTY_DESC)
            else -> mWebView?.loadUrl(mUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        mWebView!!.onResume()
        mWebView!!.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        mWebView!!.onPause()
        mWebView!!.pauseTimers()
    }

    override fun onDestroy() {
        if (mWebView != null) {
            mWebView!!.stopLoading()
            mWebView!!.clearHistory()
            fl_container.removeView(mWebView)
            mWebView!!.destroy()
            mWebView = null
        }
        super.onDestroy()
    }

    override fun onDialog(obj: Any, flag: Int) {
        val nickName = URLEncoder.encode(SPUtils.getString("nick_name"), "utf-8")
        val url = "${Constant.SHARE_INVITE}?user_name=$nickName&user_id=${SPUtils.getInt("user_id")}&avatar=${SPUtils.getString("head_img")}"
        when (flag) {
            1 -> {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
                WXOptionUtils.share(mWXAPI, url, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap, false)
            }
            2 -> {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
                WXOptionUtils.share(mWXAPI, url, "【唯爱多】向你推荐", "唯爱严选，匠心好物。", bitmap, true)
            }
            3 -> {
                copyMsg(url)
                ToastUtils.showToast("复制成功")
            }
        }
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun share() {
            runOnUiThread {
                requestPermission(getString(R.string.permission_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        val dialog = ShareDialog.newInstance()
                        dialog.show(supportFragmentManager, "share")
                    }

                    override fun onDenied() {
                        ToastUtils.showToast(getString(R.string.permission_storage))
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        @JavascriptInterface
        fun specialActivity() {
            runOnUiThread {
                requestPermission(getString(R.string.permission_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        val dialog = SpecialActivityDialog.newInstance()
                        dialog.show(supportFragmentManager, "specialActivity")
                    }

                    override fun onDenied() {
                        ToastUtils.showToast(getString(R.string.permission_storage))
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        @JavascriptInterface
        fun becomeMerchant() {
            startActivity<BecomeMerchantActivity>()
            finish()
        }

        @JavascriptInterface
        fun task(type: Int) {
            when (type) {
                1 -> {
                    val map = ArrayMap<String, Any>().apply {
                        put("type", "switch")
                        put("position", 1)
                    }
                    EventBus.getDefault().post(map)
                    finish()
                }
                2 -> {
                    runOnUiThread {
                        requestPermission(getString(R.string.permission_storage), object : OnPermissionCallback {
                            override fun onGranted() {
                                val dialog = ShareDialog.newInstance()
                                dialog.show(supportFragmentManager, "share")
                            }

                            override fun onDenied() {
                                ToastUtils.showToast(getString(R.string.permission_storage))
                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                3, 4 -> {
                    val map = ArrayMap<String, Any>().apply {
                        put("type", "switch")
                        put("position", 0)
                    }
                    EventBus.getDefault().post(map)
                    finish()
                }
            }
        }

        @JavascriptInterface
        fun applyAuthStore() {
            startActivity<SecurityDepositActivity>()
            finish()
        }

        @JavascriptInterface
        fun auctionGoods(goodsId: Int) {
            startActivity<GoodsDetailActivity>("goods_id" to goodsId)
            finish()
        }
    }

    private fun getAccessToken() {
        val params = arrayOf<Pair<String, Any>>("grant_type" to "client_credential",
                "appid" to "wx90b3ac2231df8c82", "secret" to "1433bf1d2c8c8ff965a4739d0634c7d6")
        OkHttpUtils.get("https://api.weixin.qq.com/cgi-bin/token", params, {
            val jsonObject = JSONObject(it)
            if (jsonObject.has("access_token")) {
                val accessToken = jsonObject.getString("access_token")
                getQRCode(accessToken)
            }
        }, {
            ToastUtils.showToast(it)
        })
    }

    private fun getQRCode(accessToken: String) {
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val json = "{\"width\":430,\"path\":\"pages/index/index\",\"scene\":\"pages\"}"
        val requestBody = RequestBody.create(mediaType, json)

        val request = Request.Builder()
                .url("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=$accessToken")
                .post(requestBody)
                .build()

        val call = OkHttpUtils.okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    ToastUtils.showToast(e.message ?: "未知错误")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()?.byteStream()
                val drawable = Drawable.createFromStream(inputStream, "wechatqrcode")
                runOnUiThread {
                    val dialog = ShareDialog.newInstance()
                    dialog.setDrawable(drawable)
                    dialog.show(supportFragmentManager, "share")
                }
            }
        })
    }

}
