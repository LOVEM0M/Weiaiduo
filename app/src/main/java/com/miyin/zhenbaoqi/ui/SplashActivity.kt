package com.miyin.zhenbaoqi.ui

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.utils.SPUtils

class SplashActivity : BaseActivity() {

    private var mHandler = Handler()

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.hideStatusBar(window)
        mHandler.postDelayed({
            val isLogin = SPUtils.getBoolean("is_login")
            if (isLogin) {
                startActivity<MainActivity>()
            } else {
                val showBanner = SPUtils.getBoolean("show_guide")
                if (showBanner) {
                    startActivity<WXLoginActivity>()
                } else {
                    startActivity<GuideActivity>()
                }
            }
            finish()
        }, Constant.DELAY_TIME.toLong())
    }

    override fun initData() {

    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

}
