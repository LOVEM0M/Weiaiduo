package com.miyin.zhenbaoqi.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.miyin.zhenbaoqi.R
import java.io.File
import java.io.IOException


class DownloadUtils(private val mContext: Context, url: String, name: String) {

    // 下载器
    private var downloadManager: DownloadManager? = null
    // 下载的 ID
    private var downloadId: Long = 0
    private var pathstr: String? = null

    // 广播监听下载的各个状态
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkStatus()
        }
    }

    init {
        downloadAPK(url, name)
    }

    /**
     * 下载 apk
     */
    private fun downloadAPK(url: String, name: String) {
        // 创建下载任务
        val request = DownloadManager.Request(Uri.parse(url))
        // 移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false)
        // 在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle("${mContext.getString(R.string.app_name)}更新")
        request.setDescription("新版${mContext.getString(R.string.app_name)}下载中...")
        request.setVisibleInDownloadsUi(true)

        // 设置下载的路径
        val file = File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), name)
        request.setDestinationUri(Uri.fromFile(file))
        pathstr = file.absolutePath
        // 获取 DownloadManager
        if (downloadManager == null)
            downloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager!!.enqueue(request)
        }

        // 注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    // 检查下载状态
    private fun checkStatus() {
        val query = DownloadManager.Query()
        // 通过下载的id查找
        query.setFilterById(downloadId)
        val cursor = downloadManager!!.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_PAUSED -> { // 下载暂停
                }
                DownloadManager.STATUS_PENDING -> { // 下载延迟
                }
                DownloadManager.STATUS_RUNNING -> { // 正在下载
                }
                DownloadManager.STATUS_SUCCESSFUL -> { // 下载完成
                    // 下载完成安装 APK
                    installAPK()
                    cursor.close()
                }
                DownloadManager.STATUS_FAILED -> { // 下载失败
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    mContext.unregisterReceiver(receiver)
                }
            }
        }
    }

    private fun installAPK() {
        setPermission(pathstr)
//        val intent = Intent(Intent.ACTION_VIEW)
//        // 由于没有在 Activity 环境下启动 Activity,设置下面的标签
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        val file = File(pathstr!!)
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
//        mContext.startActivity(intent)
        val file = File(pathstr!!)
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        val data = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val authority = "com.miyin.zhenbaoqi.app.provider"
            FileProvider.getUriForFile(mContext, authority, file)
        }
        intent.setDataAndType(data, type)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(intent)
    }

    /**
     * 修改文件权限
     */
    private fun setPermission(absolutePath: String?) {
        val command = "chmod 777 $absolutePath"
        val runtime = Runtime.getRuntime()
        try {
            runtime.exec(command)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
