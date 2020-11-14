package com.miyin.zhenbaoqi.receiver

import android.content.Context
import android.content.Intent
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.miyin.zhenbaoqi.ui.shop.activity.purse.PurseActivity
import com.orhanobut.logger.Logger
import org.json.JSONObject

class MyPushReceiver : JPushMessageReceiver() {

    override fun onNotifyMessageOpened(context: Context?, message: NotificationMessage?) {
        super.onNotifyMessageOpened(context, message)
        Logger.d("extra == ${message?.toString()}")
        val extra = message?.notificationExtras
        if (extra.isNullOrEmpty()) {
            return
        }
        val jsonObject = JSONObject(extra)
        if (jsonObject.has("extMessage")) {
            val extMessage = jsonObject.get("extMessage").toString()
            if (extMessage.isEmpty()) {
                return
            }
            val innerJSONObject = JSONObject(extMessage)
            if (innerJSONObject.has("type")) {
                val type = innerJSONObject.get("type").toString()
                if (type == "0") {
                    val intent = Intent(context, PurseActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(intent)
                }
            }
        }
    }

}
