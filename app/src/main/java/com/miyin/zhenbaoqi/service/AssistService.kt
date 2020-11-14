package com.miyin.zhenbaoqi.service

import android.app.Service
import android.content.Intent
import android.os.Binder

class AssistService : Service() {

    override fun onBind(intent: Intent) = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: AssistService
            get() = this@AssistService
    }

}
