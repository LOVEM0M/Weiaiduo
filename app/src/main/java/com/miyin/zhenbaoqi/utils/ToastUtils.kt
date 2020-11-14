package com.miyin.zhenbaoqi.utils

import android.annotation.SuppressLint
import android.widget.Toast
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ext.getDimension
import com.noober.background.drawable.DrawableCreator
import com.orhanobut.logger.Logger
import java.lang.reflect.Proxy

object ToastUtils {

    private var sToast: Toast? = null

    @SuppressLint("ShowToast", "DiscouragedPrivateApi", "PrivateApi")
    fun showToast(msg: CharSequence) {
        val context = App.context
        if (null == sToast) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            /*try {
                val getServiceMethod = Toast::class.java.getDeclaredMethod("getService")
                getServiceMethod.isAccessible = true
                val iNotificationManager = getServiceMethod.invoke(null)
                val iNotificationManagerCls = Class.forName("android.app.INotificationManager")
                val classLoader = sToast?.javaClass?.classLoader
                val iNotificationManagerProxy = Proxy.newProxyInstance(classLoader, arrayOf(iNotificationManagerCls)) { _, method, args ->
                    // 强制使用系统 Toast
                    // 华为p20 pro上为 enqueueToastEx
                    if ("enqueueToast" == method.name || "enqueueToastEx" == method.name) {
                        args[0] = "android"
                    }
                    method.invoke(iNotificationManager, *args.orEmpty())
                }
                val sServiceFiled = Toast::class.java.getDeclaredField("sService")
                sServiceFiled.isAccessible = true
                sServiceFiled.set(null, iNotificationManagerProxy)
            } catch (e: Exception) {
                Logger.d(e.message)
            }*/
        } else {
            sToast?.setText(msg)
            sToast?.duration = Toast.LENGTH_SHORT
        }
        sToast?.show()
    }

}
