package com.miyin.zhenbaoqi.widget.weak_dialog

import android.content.DialogInterface
import java.lang.ref.WeakReference

class WeakOnCancelListener(listener: DialogInterface.OnCancelListener?) : DialogInterface.OnCancelListener {

    private var mRef: WeakReference<DialogInterface.OnCancelListener>? = null

    init {
        listener?.let {
            mRef = WeakReference(it)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        val listener = mRef!!.get()
        listener?.onCancel(dialog)
    }

}
