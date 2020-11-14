package com.miyin.zhenbaoqi.utils

import android.graphics.Bitmap
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.io.ByteArrayOutputStream
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject

object WXOptionUtils {

    /**
     * 微信分享
     */
    fun share(iwxapi: IWXAPI, url: String, title: String, description: String, bitmap: Bitmap?, friendsCircle: Boolean) {
        val webPage = WXWebpageObject()
        val timestamp = System.currentTimeMillis().toString()
        webPage.webpageUrl = if (url.contains("?")) {
            "$url&timestamp=$timestamp"
        } else {
            "$url?timestamp=$timestamp"
        }
        val msg = WXMediaMessage(webPage)
        msg.title = title
        msg.description = description
        if (bitmap != null) {
            msg.thumbData = bitmap2Bytes(bitmap)
        }
        val req = SendMessageToWX.Req().apply {
            transaction = timestamp
            message = msg
            scene = if (friendsCircle) SendMessageToWX.Req.WXSceneTimeline else SendMessageToWX.Req.WXSceneSession
        }
        iwxapi.sendReq(req)
    }

    /**
     * 微信分享--图片
     */
    fun shareImage(iwxapi: IWXAPI, bitmap: Bitmap, friendsCircle: Boolean) {
        val imgObj = WXImageObject(bitmap)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        val req = SendMessageToWX.Req().apply {
            transaction = System.currentTimeMillis().toString()
            message = msg
            scene = if (friendsCircle) SendMessageToWX.Req.WXSceneTimeline else SendMessageToWX.Req.WXSceneSession
        }
        iwxapi.sendReq(req)
    }

    /**
     * 压缩图片
     */
    private fun bitmap2Bytes(bitmap: Bitmap): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        var options = 100
        while (output.toByteArray().size > 32 && options != 0) {
            output.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output)
            options -= 10
        }
        if (!bitmap.isRecycled)
            bitmap.recycle()
        return output.toByteArray()
    }

    /**
     * 微信支付
     */
    fun pay(iwxapi: IWXAPI, appId: String, bean: PayResultBean) {
        if (!iwxapi.isWXAppInstalled) {
            ToastUtils.showToast("请安装微信")
            return
        }
        val req = PayReq().apply {
            this.appId = appId
            nonceStr = bean.noncestr
            packageValue = bean.package_value
            sign = bean.sign
            partnerId = bean.partnerid
            prepayId = bean.prepayid
            timeStamp = bean.timestamp
        }

        iwxapi.registerApp(appId)
        iwxapi.sendReq(req)
    }

    /**
     * 微信登录
     */
    fun login(iwxapi: IWXAPI) {
        if (!iwxapi.isWXAppInstalled) {
            ToastUtils.showToast("请安装微信")
            return
        }
        val req = SendAuth.Req().apply {
            scope = "snsapi_userinfo"
            state = System.currentTimeMillis().toString()
        }
        iwxapi.sendReq(req)
    }

    /**
     * 拉起小程序
     */
    fun openProgram(iwxapi: IWXAPI, id: String, url: String, title: String, desc: String, bmp: Bitmap) {
        if (!iwxapi.isWXAppInstalled) {
            ToastUtils.showToast("请安装微信")
            return
        }

        val miniProgramObj = WXMiniProgramObject().apply {
            webpageUrl = "http://www.qq.com"
            miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
            userName = id
            path = url
        }
        val msg = WXMediaMessage(miniProgramObj).apply {
            this.title = title
            description = desc
//            val thumbBmp = Bitmap.createScaledBitmap(bmp, 90, 90, true)
//            bmp.recycle()
            thumbData = bitmap2Bytes(bmp)
        }
        val req = SendMessageToWX.Req().apply {
            transaction = "" + System.currentTimeMillis()
            message = msg
            scene = SendMessageToWX.Req.WXSceneSession
        }
        iwxapi.sendReq(req)
    }

}
