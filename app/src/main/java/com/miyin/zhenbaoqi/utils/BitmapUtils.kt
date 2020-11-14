package com.miyin.zhenbaoqi.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object BitmapUtils {

    @Suppress("DEPRECATION")
    fun createBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    fun createQRCode(content: String, widthPix: Int, heightPix: Int, logoBm: Bitmap?): Bitmap? {
        try {
            if (content.isEmpty()) {
                return null
            }
            // 配置参数
            val hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            // 空白边距设置
            hints[EncodeHintType.MARGIN] = 0
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xFF000000.toInt()
                    } else {
                        pixels[y * widthPix + x] = 0xFFFFFFFF.toInt()
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm)
            }
            // 必须使用 compress 方法将 Bitmap 保存到文件中再进行读取。直接返回的 Bitmap 是没有任何压缩的，内存消耗巨大！
            return bitmap
        } catch (e: WriterException) {
            return null
        }
    }

    private fun addLogo(src: Bitmap, logo: Bitmap): Bitmap? {
        // 获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }
        // logo 大小为二维码整体大小的 1/5
        val scaleFactor = srcWidth * 1.0f / 5 / logoWidth
        var bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f)
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
        }
        return bitmap
    }

}
