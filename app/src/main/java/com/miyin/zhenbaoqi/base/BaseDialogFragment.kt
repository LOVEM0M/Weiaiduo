package com.miyin.zhenbaoqi.base

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

abstract class BaseDialogFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (activity == null) {
            super.onCreateDialog(savedInstanceState)
        } else Dialog(activity!!, setDialogStyle())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(getContentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(setAnimation())
            val params = attributes
            params.width = setWidth()
            params.height = setHeight()
            params.gravity = setGravity()
            attributes = params
        }
    }

    protected open fun setDialogStyle() = 0

    protected open fun setWidth() = WRAP_CONTENT

    protected open fun setHeight() = WRAP_CONTENT

    protected open fun setGravity() = Gravity.CENTER

    protected open fun setAnimation() = R.style.DialogAnimation

    protected fun setOnClickListener(vararg views: View?) {
        setOnClickListener(this, *views)
    }

    protected fun setOnClickListener(listener: View.OnClickListener, vararg views: View?) {
        views.filterNotNull().forEach {
            it.setOnClickListener(listener)
        }
    }

    protected fun savePicture(bitmap: Bitmap, fileName: String) {
        val folder = File("${Constant.CACHE_PATH}/share")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val file = File(folder, fileName)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            Logger.d("保存图片错误： ${e.message}")
        }

        context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.path)))

        ToastUtils.showToast("保存成功!")
    }

    override fun onClick(v: View?) {

    }

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun initView(view: View)

    companion object {
        const val MATCH_PARENT = WindowManager.LayoutParams.MATCH_PARENT
        const val WRAP_CONTENT = WindowManager.LayoutParams.WRAP_CONTENT
    }

}
