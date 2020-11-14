package com.miyin.zhenbaoqi.ui.mine.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.ext.getVersion
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.mine.contract.SettingContract
import com.miyin.zhenbaoqi.ui.mine.presenter.SettingPresenter
import com.miyin.zhenbaoqi.utils.CleanDataUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_setting.*

@SuppressLint("SetTextI18n")
class SettingActivity : BaseMvpActivity<SettingContract.IView, SettingContract.IPresenter>(), SettingContract.IView {

    override fun getContentView() = R.layout.activity_setting

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("设置")
        immersionBar { statusBarDarkFont(true) }

        val isPush = SPUtils.getBoolean("is_push")
        switch_button.isChecked = isPush

        tv_cache.text = CleanDataUtils.getTotalCacheSize(applicationContext)
        tv_version_name.text = getVersion()

        switch_button.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                mPresenter?.updatePushSwitch(if (isChecked) 1 else 0)
            }
        })
        setOnClickListener(fl_cache, tv_user_agreement, tv_privacy_protocol, tv_log_out)
    }

    override fun initData() {
        mPresenter?.pushSwitch()
    }

    override fun createPresenter() = SettingPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_cache -> {
                CleanDataUtils.clearAllCache(applicationContext)
                showToast("清理成功")
                tv_cache.text = "0.00MB"
            }
            R.id.tv_user_agreement -> WebActivity.openActivity(applicationContext, "用户协议", Constant.USER_AGREEMENT)
            R.id.tv_privacy_protocol -> WebActivity.openActivity(applicationContext, "隐私政策", Constant.PRIVACY_PROTOCOL)
            R.id.tv_log_out -> {
                clearTask<WXLoginActivity>()
            }
        }
    }

    override fun pushSwitchSuccess(pushSwitch: Int) {
        switch_button.isChecked = pushSwitch == 0
        SPUtils.putBoolean("is_push", pushSwitch == 0)
    }

    override fun updatePushSwitchSuccess(pushSwitch: Int) {
        switch_button.isChecked = pushSwitch == 0
        SPUtils.putBoolean("is_push", pushSwitch == 0)
    }

}
