package com.miyin.zhenbaoqi.ext

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import cn.jpush.android.api.JPushInterface
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.Internals
import com.miyin.zhenbaoqi.utils.SPUtils
import java.io.IOException

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Internals.createIntent(this, T::class.java, params)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Internals.createIntent(context!!, T::class.java, params)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) {
    val intent = Internals.createIntent(this, T::class.java, params)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) {
    val intent = Internals.createIntent(context!!, T::class.java, params)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Context.clearTask() {
    val intent = Intent(this, T::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)

    ActivityManager.finishAllActivity()
    SPUtils.putBoolean("is_login", false)
    SPUtils.remove("token")
    SPUtils.remove("merchant_id")
    SPUtils.remove("account_name")

    val userId = SPUtils.getInt("user_id")
    JPushInterface.deleteAlias(this, userId)
}

fun Context.copyMsg(text: String, label: String = "label") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager.setPrimaryClip(clipData)
}

fun Context.getDimension(@DimenRes dimension: Int) = resources.getDimension(dimension)

fun Context.getDimensionPixelSize(@DimenRes dimension: Int) = resources.getDimensionPixelSize(dimension)

fun Context.getVersion(nameOfPackage: String = packageName) = try {
    val info = packageManager.getPackageInfo(nameOfPackage, 0)
    "v${info.versionName}"
} catch (e: Exception) {
    ""
}

fun Context.getVersionCode(packageName: String) = try {
    val manager = packageManager
    val info = manager.getPackageInfo(packageName, 0)
    info.versionCode
} catch (e: Exception) {
    0
}

fun Context.getJSON(fileName: String) = try {
    val assetManager = resources.assets
    val ins = assetManager.open(fileName)
    val buffer = ByteArray(ins.available())
    ins.read(buffer)
    ins.close()
    String(buffer)
} catch (e: IOException) {
    null
}
