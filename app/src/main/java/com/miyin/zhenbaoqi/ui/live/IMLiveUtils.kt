package com.miyin.zhenbaoqi.ui.live

import android.content.Context
import android.util.Log
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.live.callback.StandardCallback
import com.miyin.zhenbaoqi.ui.live.callback.StandardMemberCallback
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import com.tencent.imsdk.*
import com.tencent.liteav.basic.log.TXCLog
import io.reactivex.Observable

class IMLiveUtils private constructor() {

    private var mIMMessageMgr: IMMessageMgr? = null
    private var isInitialize: Boolean = false

    fun getName(): String {
        return SPUtils.getString("nick_name")
    }

    fun getAvater(): String {
        return SPUtils.getString("head_img")
    }

    companion object {
        private var imLiveUtils: IMLiveUtils = IMLiveUtils()

        fun getIMLiveUtils(context: Context): IMLiveUtils {
            if (imLiveUtils.mIMMessageMgr == null) {
                imLiveUtils.mIMMessageMgr = IMMessageMgr(context)
            }
            return imLiveUtils
        }
    }

    fun createIMGroup(context: Context, groupId: String?, groupName: String?, callback: StandardCallback) {
        if (!isInitialize) {
            val send = initialize(context).subscribe({
                if (it) {
                    create(groupId, groupName, callback, context)
                }
            }, {
                Logger.d("createIMGroup onError == ${it.message}")
            })
        } else {
            create(groupId, groupName, callback, context)
        }
    }

    private fun create(groupId: String?, groupName: String?, callback: StandardCallback, context: Context) {
        mIMMessageMgr?.createGroup(groupId, "AVChatRoom", groupName, object : IMMessageMgr.Callback {
            override fun onError(code: Int, errInfo: String?) {
                val msg = "[IM] 创建群失败[$errInfo:$code]"
                TXCLog.e("创建群聊 == >", "msg")
                if (code == 10025) {
                    callback.onSuccess()
                } else {
                    callback.onError(code, msg)
                }
            }

            override fun onSuccess(vararg args: Any?) {
                joinIMGroup(context, groupId, callback)
                callback.onSuccess()
            }
        })
    }

    fun quitIMGroup(context: Context, groupId: String, callback: StandardCallback) {
        if (isInitialize) {
            quitGroup(groupId, callback)
        } else {
            val send = initialize(context).subscribe({
                if (it) {
                    quitGroup(groupId, callback)
                }
            }, {
                Logger.d("quitIMGroup onError == ${it.message}")
            })
        }
    }

    private fun initialize(context: Context) =
            Observable.create<Boolean> {
                val loginUser = TIMManager.getInstance().loginUser
                Logger.d("login_user == $loginUser")
                if (loginUser.isNullOrEmpty()) {
                    val accountName = SPUtils.getString("account_name")
                    val userId = if (accountName.isEmpty()) {
                        SPUtils.getInt("user_id").toString()
                    } else {
                        accountName
                    }
                    mIMMessageMgr?.initialize(userId, SPUtils.getString("user_sig"), Constant.TENCENT_APPID.toInt(), object : IMMessageMgr.Callback {
                        override fun onError(code: Int, errInfo: String?) {
                            val msg = "[IM] 初始化失败[$errInfo:$code]"
                            TXCLog.e("IM初始化==>", msg)
                            ToastUtils.showToast(errInfo ?: "未知错误")
                            // it.onError(Throwable("code:${code} $errInfo"))
                        }

                        override fun onSuccess(vararg args: Any?) {
                            isInitialize = true
                            //设置IM的个人信息
                            val msg = java.lang.String.format("[LiveRoom] 登录成功, userID {%s}, userName {%s} " + "sdkAppID {%s}", SPUtils.getInt("user_id").toString(), SPUtils.getString("user_sig"), Constant.TENCENT_APPID.toInt())
                            val imMessageMgr = mIMMessageMgr
                            imMessageMgr?.setSelfProfile(getName(), getAvater())
                            TXCLog.d("IM初始化", msg)
                            it.onNext(true)
                            it.onComplete()
                        }
                    })
                } else {
                    isInitialize = true
                    it.onNext(true)
                    it.onComplete()
                }
            }


    fun joinIMGroup(context: Context, roomID: String?, callback: StandardCallback) {
        if (!isInitialize) {
            val send = initialize(context).subscribe {
                if (it) {
                    addGroup(roomID, callback)
                }
            }
        } else {
            addGroup(roomID, callback)
        }
    }

    private fun addGroup(roomID: String?, callback: StandardCallback) {
        mIMMessageMgr?.jionGroup(roomID, object : IMMessageMgr.Callback {
            override fun onError(code: Int, errInfo: String?) {
                val msg = "[IM] 进群失败[$errInfo:$code]"
                TXCLog.e("加入IM群聊==>", msg)
                callback.onError(code, msg)
            }

            override fun onSuccess(vararg args: Any?) {
                callback.onSuccess()
            }
        })
    }

    private fun quitGroup(roomID: String, callback: StandardCallback) {
        mIMMessageMgr?.quitGroup(roomID, object : IMMessageMgr.Callback {
            override fun onError(code: Int, errInfo: String?) {
                callback.onError(code, errInfo)
            }

            override fun onSuccess(vararg args: Any?) {
                callback.onSuccess()
            }
        })
    }

    fun sendRoomCustomMsg(context: Context, data: String, callback: StandardCallback) {
        if (!isInitialize) {
            val send = initialize(context).subscribe({
                if (it) {
                    sendGroupCustomMessage(data, callback)
                }
            }, {
                Logger.d("sendRoomCustomMsg onError == ${it.message}")
            })
        } else {
            sendGroupCustomMessage(data, callback)
        }
    }

    fun sendGroupCustomMessage(content: String?, callback: StandardCallback) {
        content?.let {
            mIMMessageMgr?.sendGroupCustomMessage(it, object : IMMessageMgr.Callback {
                override fun onError(code: Int, errInfo: String?) {
                    val msg = "[IM] 消息发送失败[$errInfo:$code]"
                    TXCLog.e("IM消息", msg)
                    callback.onError(code, errInfo)
                }

                override fun onSuccess(vararg args: Any?) {
                    callback.onSuccess()
                }
            })
        }
    }

    fun destroyGroup(groupId: String, callback: StandardCallback) {
        TIMGroupManager.getInstance().deleteGroup(groupId, object : TIMCallBack {
            override fun onError(i: Int, s: String) {
                Log.e("解散群 {%s} 失败：%s(%d)", s)
                callback.onError(i, s)
            }

            override fun onSuccess() {
                Log.e("解散群 {%s} 成功", groupId)
                callback.onSuccess()
            }
        })

    }

    fun obtainGroupMembers(groupId: String, callback: StandardMemberCallback?) {
        TIMGroupManager.getInstance().getGroupMembers(groupId, object : TIMValueCallBack<List<TIMGroupMemberInfo>> {
            override fun onSuccess(list: List<TIMGroupMemberInfo>?) {
                Log.e("获取群成员", list.toString())
                callback?.onSuccess(list?.size)
            }

            override fun onError(p0: Int, p1: String?) {
                Log.e("获取群成员失败", groupId)
            }

        })
    }

}

