package com.miyin.zhenbaoqi.ui.sort.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseStateActivity
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.orhanobut.logger.Logger
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationProvider
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo
import kotlinx.android.synthetic.main.activity_chat_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatListActivity : BaseStateActivity<IBaseView, IBasePresenter<IBaseView>>() {

    private var mPosition = 0
    private var mConversationInfo: ConversationInfo? = null

    override fun useEventBus() = true

    override fun getContentView(): Int {
        return R.layout.activity_chat_list
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("聊天列表")
        immersionBar { statusBarDarkFont(true) }

        showLoading()
        initConversationList()
    }

    override fun initData() {

    }

    override fun createPresenter(): IBasePresenter<IBaseView>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mConversationInfo.run {
                conversation_layout.deleteConversation(mPosition, this)
            }
        }
    }

    override fun reload() {
        showLoading()
        initConversationList()
    }

    private fun initConversationList() {
        conversation_layout.run {
            initDefault()
            gone(titleBar)

            conversationList.enableItemRoundIcon(true)
            conversationList.adapter?.run {
                if (itemCount != 0) {
                    showNormal()
                } else {
                    showEmpty()
                }

                setOnItemClickListener { _, position, messageInfo ->
                    mPosition = position
                    mConversationInfo = messageInfo


                    val lastMessage = messageInfo.lastMessage
                    if (lastMessage.isGroup) {
                        AlertDialog.Builder(this@ChatListActivity)
                                .setTitle("温馨提示")
                                .setMessage("该消息不是商家消息，需要从商家列表中移除")
                                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                    conversation_layout.deleteConversation(position, messageInfo)
                                    dialog.dismiss()
                                }
                                .setCancelable(false)
                                .show()
                    } else {
                        startActivityForResult<OnlineCustomerActivity>(Constant.INTENT_REQUEST_CODE, "title" to messageInfo.title, "user_id" to messageInfo.id.toInt())
                    }
                }
                setOnItemLongClickListener { _, position, messageInfo ->
                    mPosition = position
                    mConversationInfo = messageInfo

                    AlertDialog.Builder(this@ChatListActivity)
                            .setTitle("温馨提示")
                            .setMessage("您确定要从聊天列表里移除吗？")
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                removeConversationInfo(position)
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "refreshTXIMList") {
            ConversationManagerKit.getInstance().loadConversation(object : IUIKitCallBack {
                override fun onSuccess(data: Any?) {
                    if (null != data) {
                        conversation_layout.conversationList.adapter?.setDataProvider(data as ConversationProvider)
                    }
                }

                override fun onError(module: String?, errCode: Int, errMsg: String?) {
                    Logger.d("消息加载失败")
                }
            })
        }
    }

}
