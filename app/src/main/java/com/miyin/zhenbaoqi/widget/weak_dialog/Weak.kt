package com.miyin.zhenbaoqi.widget.weak_dialog

import android.content.DialogInterface

object Weak {

    fun proxy(listener: DialogInterface.OnShowListener?): WeakOnShowListener {
        return WeakOnShowListener(listener)
    }

    fun proxy(listener: DialogInterface.OnCancelListener?): WeakOnCancelListener {
        return WeakOnCancelListener(listener)
    }

    fun proxy(listener: DialogInterface.OnDismissListener?): WeakOnDismissListener {
        return WeakOnDismissListener(listener)
    }

}
