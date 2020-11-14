package com.miyin.zhenbaoqi.widget.weak_dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener

class WeakDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : Dialog(context, themeResId) {

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(Weak.proxy(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(Weak.proxy(listener))
    }

    override fun setOnShowListener(listener: OnShowListener?) {
        super.setOnShowListener(Weak.proxy(listener))
    }

}