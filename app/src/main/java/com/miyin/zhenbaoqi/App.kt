package com.miyin.zhenbaoqi

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.StrictMode
import android.widget.ImageView
import androidx.multidex.MultiDex
import cn.jpush.android.api.JPushInterface
import com.billy.android.swipe.SmartSwipeBack
import com.luck.picture.lib.PictureSelectorActivity
import com.lzy.ninegrid.NineGridView
import com.miyin.greendao.DaoMaster
import com.miyin.greendao.DaoSession
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.sql.migration.MySQLiteOpenHelper
import com.miyin.zhenbaoqi.ui.MainActivity
import com.miyin.zhenbaoqi.ui.SplashActivity
import com.miyin.zhenbaoqi.ui.common.ExitActivity
import com.miyin.zhenbaoqi.ui.live.activity.PullLiveActivity
import com.miyin.zhenbaoqi.ui.live.activity.VerticalPullLiveActivity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.shop.activity.PushLiveActivity
import com.miyin.zhenbaoqi.ui.sort.activity.SubMainActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.noober.background.BLAutoInjectController
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.interfaces.BetaPatchListener
import com.tencent.imsdk.TIMSdkConfig
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig
import com.tencent.qcloud.tim.uikit.config.GeneralConfig
import com.tencent.rtmp.TXLiveBase
import com.tencent.tinker.entry.DefaultApplicationLike
import me.jessyan.autosize.AutoSizeConfig

/**
 * 接口文档：http://www.zbq888.cn:8761/swagger-ui.html#/%E5%9C%B0%E5%9D%80%E6%8E%A5%E5%8F%A3/deleteUsingPOST
 * adb connect 127.0.0.1:62001
 */
class App(application: Application, tinkerFlags: Int, tinkerLoadVerifyFlag: Boolean, applicationStartElapsedTime: Long, applicationStartMillisTime: Long, tinkerResultIntent: Intent) : DefaultApplicationLike(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent) {

    override fun onCreate() {
        super.onCreate()
        context = application
        // 适配 7.0
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        // 日志
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        // 极光推送
        JPushInterface.init(application)
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        // 侧滑
        SmartSwipeBack.activitySlidingBack(application, SmartSwipeBack.ActivitySwipeBackFilter { activity ->
            return@ActivitySwipeBackFilter when (activity) {
                is SplashActivity -> false
                is WXLoginActivity -> false
                is MainActivity -> false
                is ExitActivity -> false
                is PullLiveActivity -> false
                is PushLiveActivity -> false
                is VerticalPullLiveActivity -> false
                is SubMainActivity -> false

                is PictureSelectorActivity -> false
                else -> true
            }
        })
        // 禁止自动初始化 BackgroundLibrary
        BLAutoInjectController.setEnableAutoInject(false)
        // 初始化 NineGridView
        NineGridView.setImageLoader(GlideImageLoader())
        // 屏幕适配
        AutoSizeConfig.getInstance().isExcludeFontScale = true
        // Bugly
        Beta.canNotifyUserRestart = true
        Beta.betaPatchListener = object : BetaPatchListener {
            override fun onApplySuccess(p0: String?) {
                SPUtils.putBoolean("canMergePatch", true)
            }

            override fun onPatchReceived(p0: String?) {

            }

            override fun onApplyFailure(p0: String?) {

            }

            override fun onDownloadReceived(p0: Long, p1: Long) {

            }

            override fun onDownloadSuccess(p0: String?) {

            }

            override fun onDownloadFailure(p0: String?) {

            }

            override fun onPatchRollback() {

            }
        }
        Bugly.init(application, "7044d51f5f", BuildConfig.DEBUG)
        Bugly.setIsDevelopmentDevice(application, BuildConfig.DEBUG)
        // TIM
        val configs = TUIKit.getConfigs().apply {
            sdkConfig = TIMSdkConfig(1400324831)
            customFaceConfig = CustomFaceConfig()
            generalConfig = GeneralConfig()
        }
        TUIKit.init(application, Constant.TENCENT_APPID.toInt(), configs)
        // 直播
        val licenceURL = "https://license.vod2.myqcloud.com/license/v1/ee384d192d6baa031ce0e56ea9362a2d/TXLiveSDK.licence" // 获取到的 licence url
        val licenceKey = "d96a365c85be1296add97ba9d2e0dbe5" // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(application, licenceURL, licenceKey)
        // GreenDao 数据库œ
        val helper = MySQLiteOpenHelper(application, "zhenbaoqi.db", null)
        val daoMaster = DaoMaster(helper.writableDatabase)
        daoSession = daoMaster.newSession()
    }

    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        MultiDex.install(base)
        Beta.installTinker(this)
    }

    private inner class GlideImageLoader : NineGridView.ImageLoader {

        override fun onDisplayImage(context: Context, imageView: ImageView, url: String) {
            imageView.loadImg(url)
        }

        override fun getCacheImage(url: String): Bitmap? {
            return null
        }
    }

    fun registerActivityLifecycleCallback(callbacks: Application.ActivityLifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(callbacks)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
        lateinit var daoSession: DaoSession
            private set

        init {
            SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
                layout.run {
                    setEnableAutoLoadMore(false)
                    setDisableContentWhenRefresh(true)
                    setDisableContentWhenLoading(true)
                }
            }
            // 设置全局的 Header 构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)
                MaterialHeader(context)
            }
            // 设置全局的 Footer 构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                // 指定为经典 Footer
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }

    }

}
