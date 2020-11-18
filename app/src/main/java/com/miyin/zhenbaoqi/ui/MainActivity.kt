package com.miyin.zhenbaoqi.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.ArrayMap
import androidx.fragment.app.Fragment
import androidx.core.app.NotificationManagerCompat
import android.view.KeyEvent
import android.view.View
import com.miyin.weiaiduo.ui.vip.fragment.VipFragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.UpdateAppBean
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.ext.getVersionCode
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.service.ConversationService
import com.miyin.zhenbaoqi.ui.home.fragment.HomeFragment
import com.miyin.zhenbaoqi.ui.mine.fragment.MineFragment
import com.miyin.zhenbaoqi.ui.recomment.fragment.SeedingFragment
import com.miyin.zhenbaoqi.ui.shopcart.fragment.ShopCartFragment
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.DownloadUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity(), OnTabSelectCallback {

    private var mCurrentFragment: Fragment? = null
    private var mLastClickTime = 0L
    private var mClickPosition = 0

    private var mHomeFragment: Fragment? = null
    private var mSortFragment: Fragment? = null
    private var mLiveFragment: Fragment? = null
    private var mMessageFragment: Fragment? = null
    private var mMineFragment: Fragment? = null
    private var mScreenStatusReceiver: ScreenStatusReceiver? = null

    override fun useEventBus() = true

    override fun getContentView() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        if (null == mHomeFragment) {
            mHomeFragment = HomeFragment.newInstance()
        }
        switchFragment(mHomeFragment!!)
        onTabSelect(mClickPosition)

        registerScreenStatusReceiver()

//        val loginUser = TIMManager.getInstance().loginUser
//        Logger.d("TIM 登陆 Login User == $loginUser")
//        if (loginUser.isNullOrEmpty()) {
//            val intent = Intent(this, ConversationService::class.java)
//            startService(intent)
//        } else {
//            val userId = SPUtils.getInt("userId").toString()
//            TIMManager.getInstance().autoLogin(userId, object : TIMCallBack {
//                override fun onSuccess() {
//                    Logger.d("自动登录成功 ===============")
//                }
//
//                override fun onError(p0: Int, p1: String?) {
//                    TIMManager.getInstance().initStorage(userId, object : TIMCallBack {
//                        override fun onSuccess() {
//                            Logger.d("自动初始化成功 ===============")
//                        }
//
//                        override fun onError(p0: Int, p1: String?) {
//
//                        }
//                    })
//                }
//            })
//        }

//        ConversationManagerKit.getInstance().addUnreadWatcher { count ->
//            if (count > 0) {
//                visible(tv_not_read_message)
//            } else {
//                gone(tv_not_read_message)
//            }
//            var unreadStr = "$count"
//            if (count > 100) {
//                unreadStr = "99+"
//            }
//            tv_not_read_message.text = unreadStr
//        }


        setOnClickListener(tv_home, tv_sort, tv_live, tv_message, tv_mine)
    }

    override fun initData() {
//        requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
//            override fun onGranted() {
//                val disposable = RetrofitUtils.mApiService.updateApk()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(object : BaseSingleObserver<UpdateAppBean>() {
//                            override fun doOnSuccess(data: UpdateAppBean) {
//                                data.data?.run {
//                                    val versionCode = getVersionCode("com.miyin.zhenbaoqi")
//                                    if (versionCode != code) {
//                                        updateApp(url!!, remark!!)
//                                    } else {
//                                        checkNotifySetting()
//                                    }
//                                }
//                            }
//                        })
//            }
//
//            override fun onDenied() {
//                checkNotifySetting()
//            }
//        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onDestroy() {
        unRegisterScreenStatusReceiver()
//        ConversationManagerKit.getInstance().destroyConversation()
        super.onDestroy()
    }

    override fun onTabClick(position: Int) {
        when (position) {
            0 -> {
                mClickPosition = 0

                if (null == mHomeFragment) {
                    mHomeFragment = HomeFragment.newInstance()
                }
                switchFragment(mHomeFragment!!)
            }
            1 -> {
                mClickPosition = 1

                if (null == mSortFragment) {
                    mSortFragment = SeedingFragment.newInstance()
                }
                switchFragment(mSortFragment!!)
            }
            2 -> {
                mClickPosition = 2

                if (null == mLiveFragment) {
                    mLiveFragment = VipFragment.newInstance()
                }
                switchFragment(mLiveFragment!!)
            }
            3 -> {
                mClickPosition = 3

                if (null == mMessageFragment) {
                    mMessageFragment = ShopCartFragment.newInstance()
                }
                switchFragment(mMessageFragment!!)
            }
        }
        onTabSelect(mClickPosition)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_home -> {
                if (mClickPosition == 0) return
                mClickPosition = 0

                if (null == mHomeFragment) {
                    mHomeFragment = HomeFragment.newInstance()
                }
                switchFragment(mHomeFragment!!)
            }
            R.id.tv_sort -> {
                if (mClickPosition == 1) return
                mClickPosition = 1

                if (null == mSortFragment) {
                    mSortFragment = SeedingFragment.newInstance()
                }
                switchFragment(mSortFragment!!)
            }
            R.id.tv_live -> {
                if (mClickPosition == 2) return
                mClickPosition = 2

                if (null == mLiveFragment) {
                    mLiveFragment = VipFragment.newInstance()
                }
                switchFragment(mLiveFragment!!)
            }
            R.id.tv_message -> {
                if (mClickPosition == 3) return
                mClickPosition = 3

                if (null == mMessageFragment) {
                    mMessageFragment = ShopCartFragment.newInstance()
                }
                switchFragment(mMessageFragment!!)
            }
            R.id.tv_mine -> {
                if (mClickPosition == 4) return
                mClickPosition = 4

                if (null == mMineFragment) {
                    mMineFragment = MineFragment.newInstance()
                }
                switchFragment(mMineFragment!!)
            }
        }
        onTabSelect(mClickPosition)
    }

    private fun switchFragment(targetFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (mCurrentFragment == null) {
            transaction.add(R.id.fl_container, targetFragment)
        } else {
            if (targetFragment.isAdded) {
                transaction.hide(mCurrentFragment!!).show(targetFragment)
            } else {
                transaction.hide(mCurrentFragment!!).add(R.id.fl_container, targetFragment)
            }
        }
        transaction.commitAllowingStateLoss()
        mCurrentFragment = targetFragment
    }

    private fun onTabSelect(position: Int) {
        tv_home.isSelected = position == 0
        tv_sort.isSelected = position == 1
        tv_live.isSelected = position == 2
        tv_message.isSelected = position == 3
        tv_mine.isSelected = position == 4
    }

    private fun registerScreenStatusReceiver() {
        mScreenStatusReceiver = ScreenStatusReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(mScreenStatusReceiver, intentFilter)
    }

    private fun unRegisterScreenStatusReceiver() {
        if (null != mScreenStatusReceiver) {
            unregisterReceiver(mScreenStatusReceiver)
            mScreenStatusReceiver = null
        }
    }

    private fun updateApp(url: String, remark: String) {
        AlertDialog.Builder(this)
                .setTitle("升级提示")
                .setMessage(remark)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                    val file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    file?.run {
                        if (exists()) {
                            listFiles()?.filter { it.exists() }!!
                                    .forEach { it.delete() }
                        }
                    }
                    DownloadUtils(applicationContext, url, "${System.currentTimeMillis()}.apk")
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkNotifySetting() {
        val manager = NotificationManagerCompat.from(this)
        val isOpened = manager.areNotificationsEnabled()

        if (!isOpened) {
            AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton("去设置") { dialog, _ ->
                        dialog.dismiss()

                        val intent = Intent()
                        when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
                            }
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {  // 5.0
                                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                intent.putExtra("app_package", packageName)
                                intent.putExtra("app_uid", applicationInfo.uid)
                            }
                            Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT -> {  // 4.4
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.data = Uri.parse("package:$packageName")
                            }
                            Build.VERSION.SDK_INT >= 15 -> {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                                intent.data = Uri.fromParts("package", packageName, null)

                            }
                        }
                        startActivity(intent)
                    }
                    .show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentClickTime = System.currentTimeMillis()
            if (currentClickTime - mLastClickTime > 2 * 1000) {
                mLastClickTime = currentClickTime
                ToastUtils.showToast("再按一次退出应用")
                return true
            } else {
                if (SPUtils.contains("canMergePatch")) {
                    SPUtils.remove("canMergePatch")
                    ActivityManager.exit()
                } else {
                    finish()
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainEvent(map: ArrayMap<String, Any>) {
        val type = map["type"].toString()
        if (type == "switch") {
            val position = map["position"].toString().toDouble().toInt()
            onTabClick(position)
        }
    }

    private inner class ScreenStatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_SCREEN_OFF == intent.action) {
                if (SPUtils.contains("canMergePatch")) {
                    SPUtils.remove("canMergePatch")
                    ActivityManager.exit()
                }
            }
        }
    }

}
