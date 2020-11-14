package com.miyin.zhenbaoqi.service

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import com.miyin.zhenbaoqi.App.Companion.context
import com.miyin.zhenbaoqi.R

class ForegroundService : Service() {

    private val PID = android.os.Process.myPid()
    private var mConnection: AssistServiceConnection? = null

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        setForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY_COMPATIBILITY
    }

    private fun setForeground() {
        if (null == mConnection) {
            mConnection = AssistServiceConnection()
        }
        bindService(Intent(this, AssistService::class.java), mConnection!!, Service.BIND_AUTO_CREATE)
    }

    private inner class AssistServiceConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {

        }

        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val assistService = (binder as AssistService.LocalBinder).service
            startForeground(PID, getNotification())
            assistService.startForeground(PID, getNotification())
            assistService.stopForeground(true)

            unbindService(mConnection!!)
            mConnection = null
        }
    }

    private fun getNotification(): Notification {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // 8.0系统之上
            val channel = NotificationChannel(PID.toString(), "chanel_name", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            Notification.Builder(context, PID.toString())
        } else {
            Notification.Builder(context)
        }

        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("${getString(R.string.app_name)}正在后台运行...")

        manager.notify(PID, builder.build())
        return builder.build()
    }

}
