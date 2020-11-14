package com.miyin.zhenbaoqi.widget.weak_dialog

import android.content.DialogInterface
import java.lang.ref.WeakReference

class WeakOnShowListener(listener: DialogInterface.OnShowListener?) : DialogInterface.OnShowListener {

    private var mRef: WeakReference<DialogInterface.OnShowListener>? = null

    init {
        listener?.let {
            mRef = WeakReference(it)
        }
    }

    override fun onShow(dialog: DialogInterface) {
        val listener = mRef!!.get()
        listener?.onShow(dialog)
    }

}
