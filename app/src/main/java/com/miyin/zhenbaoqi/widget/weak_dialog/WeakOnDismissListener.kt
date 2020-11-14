package com.miyin.zhenbaoqi.widget.weak_dialog

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import java.lang.ref.WeakReference

class WeakOnDismissListener(listener: DialogInterface.OnDismissListener?) : DialogInterface.OnDismissListener {

    private var mRef: WeakReference<DialogInterface.OnDismissListener>? = null

    init {
        listener?.let {
            mRef = WeakReference(it)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        val listener = mRef!!.get()
        listener?.onDismiss(dialog)
    }

}
