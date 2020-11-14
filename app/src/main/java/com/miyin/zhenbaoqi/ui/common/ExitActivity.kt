package com.miyin.zhenbaoqi.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.login.activity.PhoneLoginActivity
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_exit.*

class ExitActivity : BaseActivity() {

    companion object {
        fun openActivity(context: Context) {
            val intent = Intent(context, ExitActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getContentView() = R.layout.activity_exit

    override fun initView(savedInstanceState: Bundle?) {
        tv_confirm.setOnClickListener {
            val intent = Intent(applicationContext, PhoneLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
//            overridePendingTransition(0, 0)

            ActivityManager.finishAllActivity()
            SPUtils.clear()

            finish()
        }
    }

    override fun initData() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

}
