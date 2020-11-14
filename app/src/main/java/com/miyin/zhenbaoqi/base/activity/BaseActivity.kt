package com.miyin.zhenbaoqi.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.noober.background.BackgroundLibrary

import org.greenrobot.eventbus.EventBus

import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks {

    protected lateinit var mTitleBar: FrameLayout
    private var mOnPermissionCallback: OnPermissionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        ActivityManager.addActivity(this)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initView(savedInstanceState)
        initData()
    }

    override fun finish() {
        super.finish()
        ActivityManager.removeActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            mOnPermissionCallback?.onGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        mOnPermissionCallback?.onGranted()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("如果没有授权，应用将无法正常工作。我们将引导您到设置页面修改应用权限。")
                    .setPositiveButton(getString(R.string.confirm))
                    .setNegativeButton(getString(R.string.cancel))
                    .build()
                    .show()
        } else {
            mOnPermissionCallback?.onDenied()
        }
    }

    protected fun requestPermission(rationale: String, callback: OnPermissionCallback, vararg permission: String) {
        mOnPermissionCallback = callback
        if (!EasyPermissions.hasPermissions(applicationContext, *permission)) {
            EasyPermissions.requestPermissions(this, rationale, Constant.PERMISSION_REQUEST_CODE, *permission)
        } else {
            callback.onGranted()
        }
    }

    protected open fun useEventBus() = false

    protected fun initTitleBar(title: String, leftImage: Int = R.drawable.ic_back_gray, titleColor: Int = R.color.text_33,
                               rightTitle: String = "", rightImage: Int = 0, bgColor: Int = R.color.white) {
        mTitleBar = findViewById(R.id.fl_title_bar)
        if (bgColor != 0) {
            mTitleBar.setBackgroundColor(ContextCompat.getColor(applicationContext, bgColor))
        }

        if (leftImage != 0) {
            val leftImageView = findViewById<ImageView>(R.id.iv_back)
            visible(leftImageView)
            leftImageView.setImageResource(leftImage)
            leftImageView.setOnClickListener { onLeftClick() }
        }

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        tvTitle.text = title
        tvTitle.setTextColor(ContextCompat.getColor(applicationContext, titleColor))

        if (!TextUtils.isEmpty(rightTitle)) {
            val tvRightTitle = findViewById<TextView>(R.id.tv_right_title)
            visible(tvRightTitle)
            tvRightTitle.text = rightTitle
            tvRightTitle.setTextColor(ContextCompat.getColor(applicationContext, titleColor))
            tvRightTitle.setOnClickListener { onRightClick() }
        }

        if (rightImage != 0) {
            val rightImageView = findViewById<ImageView>(R.id.iv_right_image)
            visible(rightImageView)
            rightImageView.setImageResource(rightImage)
            rightImageView.setOnClickListener { onRightClick() }
        }
    }

    protected open fun onLeftClick() {
        finish()
    }

    protected open fun onRightClick() {

    }

    protected fun immersionBar(block: ImmersionBar.() -> Unit = {}) {
        ImmersionBar.with(this).titleBar(mTitleBar).apply(block).init()
    }

    override fun onClick(v: View?) {

    }

    protected fun setOnClickListener(vararg views: View?) {
        setOnClickListener(this, *views)
    }

    protected fun setOnClickListener(listener: View.OnClickListener, vararg views: View?) {
        views.filterNotNull().forEach {
            it.setOnClickListener(listener)
        }
    }

    protected fun visible(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.VISIBLE) {
                it.visibility = View.VISIBLE
            }
        }
    }

    protected fun invisible(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.INVISIBLE) {
                it.visibility = View.INVISIBLE
            }
        }
    }

    protected fun gone(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.GONE) {
                it.visibility = View.GONE
            }
        }
    }

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

}
