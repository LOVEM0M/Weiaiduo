package com.miyin.zhenbaoqi.ui.live

import android.content.Context
import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.miyin.zhenbaoqi.ui.live.IMMessageMgr
import com.miyin.zhenbaoqi.ui.live.callback.StandardMemberCallback
import com.tencent.imsdk.*
import com.tencent.imsdk.TIMGroupManager.CreateGroupParam
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo
import com.tencent.imsdk.ext.group.TIMGroupManagerExt
import com.tencent.liteav.basic.log.TXCLog
import java.util.*

class IMMessageMgr(context: Context) : TIMMessageListener {
    private var mContext: Context?
    private var mHandler: Handler?
    private var mLoginSuccess = false
    private var mSelfUserID: String? = null
    private var mSelfUserSig: String? = null
    private var mGroupID: String? = null
    private var mTIMSdkConfig: TIMSdkConfig? = null
    private var mIMConnListener: IMMessageConnCallback? = null
    private var mIMLoginListener: IMMessageLoginCallback? = null
    private val mMessageListener: IMMessageCallback?

    /**
     * 函数级公共Callback定义
     */
    interface Callback {
        fun onError(code: Int, errInfo: String?)
        fun onSuccess(vararg args: Any?)
    }

    /**
     * 模块回调Listener定义
     */
    interface IMMessageListener {
        /**
         * IM连接成功
         */
        fun onConnected()

        /**
         * IM断开连接
         */
        fun onDisconnected()

        /**
         * IM群组里推流者成员变化通知
         */
        fun onPusherChanged()

        /**
         * 收到群文本消息
         */
        fun onGroupTextMessage(groupID: String?, senderID: String?, userName: String?, headPic: String?, message: String?)

        /**
         * 收到自定义的群消息
         */
        fun onGroupCustomMessage(groupID: String?, senderID: String?, message: String?)

        /**
         * 收到自定义的C2C消息
         */
        fun onC2CCustomMessage(sendID: String?, cmd: String?, message: String?)

        /**
         * IM群组销毁回调
         */
        fun onGroupDestroyed(groupID: String?)

        /**
         * 日志回调
         */
        fun onDebugLog(log: String)

        /**
         * 用户进群通知
         * @param groupID 群标识
         * @param users 进群用户信息列表
         */
        fun onGroupMemberEnter(groupID: String?, users: ArrayList<TIMUserProfile>?)

        /**
         * 用户退群通知
         * @param groupID 群标识
         * @param users 退群用户信息列表
         */
        fun onGroupMemberExit(groupID: String?, users: ArrayList<TIMUserProfile>?)

        /**
         * 用户被强制下线通知
         *
         */
        fun onForceOffline()
    }

    /**
     * 初始化
     * @param userID    用户ID
     * @param userSig   签名
     * @param appID     appID
     * @param callback
     */
    fun initialize(userID: String?, userSig: String?, appID: Int, callback: Callback?) {
        if (userID == null || userSig == null) {
            mMessageListener!!.onDebugLog("参数错误，请检查 UserID， userSig 是否为空！")
            callback?.onError(-1, "参数错误")
            return
        }
        mSelfUserID = userID
        mSelfUserSig = userSig
        runOnHandlerThread(Runnable {
            val initializeStartTS = System.currentTimeMillis()
            mIMConnListener = IMMessageConnCallback(initializeStartTS, callback)
            mTIMSdkConfig = TIMSdkConfig(appID)
            val userConfig = TIMUserConfig()
            userConfig.connectionListener = mIMConnListener
            userConfig.userStatusListener = object : TIMUserStatusListener {
                override fun onForceOffline() {
                    val listener: IMMessageListener? = mMessageListener
                    listener?.onForceOffline()
                }

                override fun onUserSigExpired() {
                    val listener: IMMessageListener? = mMessageListener
                    listener?.onForceOffline()
                }
            }
            TIMManager.getInstance().addMessageListener(this@IMMessageMgr)
            if (TIMManager.getInstance().init(mContext, mTIMSdkConfig)) {
                login(object : Callback {
                    override fun onError(code: Int, errInfo: String?) {
                        printDebugLog("login failed: %s(%d)", errInfo!!, code)
                        mLoginSuccess = false
                        callback!!.onError(code, "IM登录失败")
                    }

                    override fun onSuccess(vararg args: Any?) {
                        printDebugLog("login success")
                        mLoginSuccess = true
                        mConnectSuccess = true
                        callback!!.onSuccess()
                    }
                })
                TIMManager.getInstance().userConfig = userConfig
            } else {
                printDebugLog("init failed")
                callback!!.onError(-1, "IM初始化失败")
            }
        })
    }

    fun runOnHandlerThread(runnable: Runnable?) {
        val handler = mHandler
        handler?.post(runnable) ?: Log.e(TAG, "runOnHandlerThread -> Handler == null")
    }

    /**
     * 反初始化
     */
    fun unInitialize() {
        TIMManager.getInstance().removeMessageListener(this@IMMessageMgr)
        mContext = null
        mHandler = null
        //
//        TIMUserConfig userConfig = new TIMUserConfig();
//        userConfig.setConnectionListener(null);
//        TIMManager.getInstance().setUserConfig(userConfig);
        if (mTIMSdkConfig != null) {
            mTIMSdkConfig = null
        }
        if (mIMConnListener != null) {
            mIMConnListener!!.clean()
            mIMConnListener = null
        }
        if (mIMLoginListener != null) {
            mIMLoginListener!!.clean()
            mIMLoginListener = null
        }
        mMessageListener?.setListener(null)
        logout(null)
    }

    /**
     * 加入IM群组
     * @param groupId  群ID
     * @param callback
     */
    fun jionGroup(groupId: String?, callback: Callback?) {
        runOnHandlerThread(Runnable {
            TIMGroupManager.getInstance().applyJoinGroup(groupId!!, "who care?", object : TIMCallBack {
                override fun onError(i: Int, s: String) {
                    var message = s
                    if (i == 10010) {
                        message = "房间已解散"
                    }
                    callback!!.onError(i, message)
                }

                override fun onSuccess() {
                    printDebugLog("加入群 {%s} 成功", groupId)
                    mGroupID = groupId
                    callback!!.onSuccess()
                }
            })
        })
    }

    /**
     * 退出IM群组
     * @param groupId  群ID
     * @param callback
     */
    fun quitGroup(groupId: String?, callback: Callback?) {
        runOnHandlerThread(Runnable {
            TIMGroupManager.getInstance().quitGroup(groupId!!, object : TIMCallBack {
                override fun onError(i: Int, s: String) {
                    if (i == 10010) {
                        printDebugLog("群 {%s} 已经解散了", groupId)
                        onSuccess()
                    } else {
                        printDebugLog("退出群 {%s} 失败： %s(%d)", groupId, s, i)
                        callback!!.onError(i, s)
                    }
                }

                override fun onSuccess() {
                    printDebugLog("退出群 {%s} 成功", groupId)
                    mGroupID = groupId
                    callback!!.onSuccess()
                }
            })
        })
    }

    fun createGroup(groupId: String?, groupType: String?, groupName: String?, callback: Callback?) {
        val param = CreateGroupParam(groupType!!, groupName!!)
        param.groupId = groupId
        runOnHandlerThread(Runnable {
            TIMGroupManager.getInstance().createGroup(param, object : TIMValueCallBack<String?> {
                override fun onError(i: Int, s: String) {
                    printDebugLog("创建群 {%s} 失败：%s(%d)", groupId!!, s, i)
                    if (i == 10036) {
                        val createRoomErrorMsg = "您当前使用的云通讯账号未开通音视频聊天室功能，创建聊天室数量超过限额，请前往腾讯云官网开通【IM音视频聊天室】，地址：https://buy.cloud.tencent.com/avc"
                        TXCLog.e(TAG, createRoomErrorMsg)
                        printDebugLog(createRoomErrorMsg)
                    }
                    if (i == 10025) {
                        mGroupID = groupId
                    }
                    callback!!.onError(i, s)
                }

                override fun onSuccess(s: String?) {
                    printDebugLog("创建群 {%s} 成功", groupId!!)
                    mGroupID = groupId
                    callback!!.onSuccess()
                }
            })
        })
    }

    /**
     * 发送自定义群消息
     * @param content   自定义消息的内容
     * @param callback
     */
    fun sendGroupCustomMessage(content: String, callback: Callback?) {
        runOnHandlerThread(Runnable {
            val message = TIMMessage()
            try {
                val customElem = TIMCustomElem()
                customElem.data = content.toByteArray(charset("UTF-8"))
                message.addElement(customElem)
            } catch (e: Exception) {
                printDebugLog("[sendGroupCustomMessage] 发送自定义群{%s}消息失败，组包异常", mGroupID!!)
                callback?.onError(-1, "发送CC消息失败")
                return@Runnable
            }
            val conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mGroupID)
            conversation.sendMessage(message, object : TIMValueCallBack<TIMMessage?> {
                override fun onError(i: Int, s: String) {
                    printDebugLog("[sendGroupCustomMessage] 发送自定义群{%s}消息失败: %s(%d)", mGroupID!!, s, i)
                    callback?.onError(i, s)
                }

                override fun onSuccess(timMessage: TIMMessage?) {
                    printDebugLog("[sendGroupCustomMessage] 发送自定义群消息成功", timMessage.toString())
                    callback?.onSuccess()
                }
            })
        })
    }

    fun setSelfProfile(nickname: String?, faceURL: String?) {
        if (nickname == null && faceURL == null) {
            return
        }
        runOnHandlerThread(Runnable {
            val profileMap = HashMap<String, Any>()
            if (nickname != null) {
                profileMap[TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK] = nickname
            }
            if (faceURL != null) {
                profileMap[TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL] = faceURL
            }
            TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, object : TIMCallBack {
                override fun onError(code: Int, desc: String) {
                    Log.e(TAG, "modifySelfProfile failed: $code desc$desc")
                }

                override fun onSuccess() {
                    Log.e(TAG, "modifySelfProfile success")
                }
            })
        })
    }

    override fun onNewMessages(list: List<TIMMessage>): Boolean {
        for (message in list) {
            var i = 0
            loop@ while (i < message.elementCount) {
                val element = message.getElement(i)
                printDebugLog("onNewMessage type = %s", element.type)
                when (element.type) {
                    TIMElemType.GroupSystem -> {
                        val systemElemType = (element as TIMGroupSystemElem).subtype
                        when (systemElemType) {
                            TIMGroupSystemElemType.TIM_GROUP_SYSTEM_DELETE_GROUP_TYPE -> {
                                printDebugLog("onNewMessage subType = %s", systemElemType)
                                mMessageListener?.onGroupDestroyed(element.groupId)
                            }
                            TIMGroupSystemElemType.TIM_GROUP_SYSTEM_CUSTOM_INFO -> {
                                val userData = element.userData
                                if (userData == null || userData.size == 0) {
                                    printDebugLog("userData == null")
                                    break@loop
                                }
                                val data = String(userData)
                                printDebugLog("onNewMessage subType = %s content = %s", systemElemType, data)
                                try {
                                    val commonJson = Gson().fromJson<CommonJson<Any>>(data, object : TypeToken<CommonJson<Any?>?>() {}.type)
                                    if (commonJson.cmd == "notifyPusherChange") {
                                        mMessageListener!!.onPusherChanged()
                                    }
                                } catch (e: JsonSyntaxException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } //case GroupSystem
                    TIMElemType.Custom -> {
                        val userData = (element as TIMCustomElem).data
                        if (userData == null || userData.size == 0) {
                            printDebugLog("userData == null")
                            break@loop
                        }
                        val data = String(userData)
                        printDebugLog("onNewMessage subType = Custom content = %s", data)
                        try {
                            val commonJson = Gson().fromJson<CommonJson<Any>>(data, object : TypeToken<CommonJson<Any?>?>() {}.type)
                            if (commonJson.cmd != null) {
                                if (commonJson.cmd.equals("CustomTextMsg", ignoreCase = true)) {
                                    ++i
                                    val userInfo = Gson().fromJson(Gson().toJson(commonJson.data), UserInfo::class.java)
                                    if (userInfo != null && i < message.elementCount) {
                                        val nextElement = message.getElement(i)
                                        val textElem = nextElement as TIMTextElem
                                        val text = textElem.text
                                        if (text != null) {
                                            mMessageListener!!.onGroupTextMessage(mGroupID, message.sender, userInfo.nickName, userInfo.headPic, text)
                                        }
                                    }
                                } else if (commonJson.cmd.equals("linkmic", ignoreCase = true) || commonJson.cmd.equals("pk", ignoreCase = true)) {
                                    mMessageListener!!.onC2CCustomMessage(message.sender, commonJson.cmd, Gson().toJson(commonJson.data))
                                } else if (commonJson.cmd.equals("CustomCmdMsg", ignoreCase = true)) {
                                    mMessageListener!!.onGroupCustomMessage(mGroupID, message.sender, Gson().toJson(commonJson.data))
                                } else if (commonJson.cmd.equals("notifyPusherChange", ignoreCase = true)) {
                                    mMessageListener!!.onPusherChanged()
                                }
                            }
                        } catch (e: JsonSyntaxException) {
                            e.printStackTrace()
                        }
                    }
                    TIMElemType.GroupTips -> {
                        val tipsElem = element as TIMGroupTipsElem
                        if (tipsElem.tipsType == TIMGroupTipsType.Join) {
                            val changedUserInfos = tipsElem.changedUserInfo
                            if (changedUserInfos != null && changedUserInfos.size > 0) {
                                val users = ArrayList<TIMUserProfile>()
                                for ((_, value) in changedUserInfos) {
                                    users.add(value)
                                }
                                mMessageListener!!.onGroupMemberEnter(tipsElem.groupId, users)
                            }
                        } else if (tipsElem.tipsType == TIMGroupTipsType.Quit) {
                            val users = ArrayList<TIMUserProfile>()
                            users.add(tipsElem.opUserInfo)
                            mMessageListener!!.onGroupMemberExit(tipsElem.groupId, users)
                        }
                    }
                }
                i++
            }
        }
        return false
    }

    private fun login(cb: Callback?) {
        if (mSelfUserID == null || mSelfUserSig == null) {
            cb?.onError(-1, "没有 UserId")
            return
        }
        Log.i(TAG, "start login: userId = " + mSelfUserID)
        val loginStartTS = System.currentTimeMillis()
        mIMLoginListener = IMMessageLoginCallback(loginStartTS, cb)
        TIMManager.getInstance().login(mSelfUserID, mSelfUserSig, mIMLoginListener)
    }

    private fun logout(callback: Callback?) {
        TIMManager.getInstance().logout(null)
    }

    private fun printDebugLog(format: String, vararg args: Any) {
        val log: String
        try {
            log = String.format(format, *args)
            Log.e(TAG, log)
            mMessageListener?.onDebugLog(log)
        } catch (e: FormatFlagsConversionMismatchException) {
            e.printStackTrace()
        }
    }

    /**
     * 辅助类 IM Connect Listener
     */
    private inner class IMMessageConnCallback(ts: Long, cb: Callback?) : TIMConnListener {
        private var initializeStartTS: Long = 0
        private var callback: Callback?
        fun clean() {
            initializeStartTS = 0
            callback = null
        }

        override fun onConnected() {
            printDebugLog("connect success，initialize() time cost %.2f secs", (System.currentTimeMillis() - initializeStartTS) / 1000.0)
            mMessageListener!!.onConnected()
            mConnectSuccess = true
        }

        override fun onDisconnected(i: Int, s: String) {
            printDebugLog("disconnect: %s(%d)", s, i)
            if (mLoginSuccess) {
                mMessageListener?.onDisconnected()
            } else {
                if (callback != null) {
                    callback!!.onError(i, s)
                }
            }
            mConnectSuccess = false
        }

        override fun onWifiNeedAuth(s: String) {
            printDebugLog("onWifiNeedAuth(): %s", s)
            if (mLoginSuccess) {
                mMessageListener!!.onDisconnected()
            } else {
                if (callback != null) {
                    callback!!.onError(-1, s)
                }
            }
            mConnectSuccess = false
        }

        init {
            initializeStartTS = ts
            callback = cb
        }
    }

    /**
     * 辅助类 IM Login Listener
     */
    private inner class IMMessageLoginCallback(private var loginStartTS: Long, private var callback: Callback?) : TIMCallBack {
        fun clean() {
            loginStartTS = 0
            callback = null
        }

        override fun onError(i: Int, s: String) {
            if (callback != null) {
                callback!!.onError(i, s)
            }
        }

        override fun onSuccess() {
            printDebugLog("login success, time cost %.2f secs", (System.currentTimeMillis() - loginStartTS) / 1000.0)
            if (callback != null) {
                callback!!.onSuccess()
            }
        }

    }

    /**
     * 辅助类 IM Message Listener
     */
    private inner class IMMessageCallback(private var listener: IMMessageListener?) : IMMessageListener {
        fun setListener(listener: IMMessageListener?) {
            this.listener = listener
        }

        override fun onConnected() {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onConnected() })
        }

        override fun onDisconnected() {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onDisconnected() })
        }

        override fun onPusherChanged() {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onPusherChanged() })
        }

        override fun onGroupDestroyed(groupID: String?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onGroupDestroyed(groupID) })
        }

        override fun onDebugLog(line: String) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onDebugLog("[IM] $line") })
        }

        override fun onGroupMemberEnter(groupID: String?, users: ArrayList<TIMUserProfile>?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onGroupMemberEnter(groupID, users) })
        }

        override fun onGroupMemberExit(groupID: String?, users: ArrayList<TIMUserProfile>?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onGroupMemberExit(groupID, users) })
        }

        override fun onForceOffline() {
            runOnHandlerThread(Runnable {
                if (listener != null) {
                    listener!!.onForceOffline()
                }
            })
        }

        override fun onGroupTextMessage(roomID: String?, senderID: String?, userName: String?, headPic: String?, message: String?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onGroupTextMessage(roomID, senderID, userName, headPic, message) })
        }

        override fun onGroupCustomMessage(groupID: String?, senderID: String?, message: String?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onGroupCustomMessage(groupID, senderID, message) })
        }

        override fun onC2CCustomMessage(senderID: String?, cmd: String?, message: String?) {
            runOnHandlerThread(Runnable { if (listener != null) listener!!.onC2CCustomMessage(senderID, cmd, message) })
        }

    }

    class CommonJson<T> {
        var cmd: String? = null
        var data: T? = null
    }

    private class UserInfo {
        var nickName: String? = null
        var headPic: String? = null
        var levelImg: String? = null
    }

    companion object {
        private val TAG = IMMessageMgr::class.java.simpleName
        private var mConnectSuccess = false
    }

    init {
        mContext = context.applicationContext
        mHandler = Handler(mContext?.getMainLooper())
        mMessageListener = IMMessageCallback(null)
    }
}