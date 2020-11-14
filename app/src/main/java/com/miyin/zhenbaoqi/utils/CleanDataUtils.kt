package com.miyin.zhenbaoqi.utils

import android.content.Context
import android.os.Environment

import java.io.File
import java.math.BigDecimal

object CleanDataUtils {

    /**
     * 获取缓存值
     */
    fun getTotalCacheSize(context: Context): String {

        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除所有缓存
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
            context.deleteDatabase("webview.db")
            context.deleteDatabase("webviewCache.db")
        }
    }

    /**
     * 删除某个文件
     */
    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            return if (children.map { deleteDir(File(dir, it)) }.none { it }) false else dir.delete()
        }
        return dir != null && dir.delete()
    }

    /**
     * 获取文件
     */
    private fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList = file.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (aFileList in fileList) {
                    // 如果下面还有文件
                    size = if (aFileList.isDirectory) {
                        size + getFolderSize(aFileList)
                    } else {
                        size + aFileList.length()
                    }
                }
            }
        }
        return size
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal.valueOf(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

}
