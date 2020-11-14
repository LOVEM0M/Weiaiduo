package com.miyin.zhenbaoqi.ui.message.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseStateFragment
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.bean.UserTypeBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.message.activity.*
import com.miyin.zhenbaoqi.ui.message.contract.MessageContract
import com.miyin.zhenbaoqi.ui.message.presenter.MessagePresenter
import com.miyin.zhenbaoqi.ui.shop.activity.invite.ManagerInviteActivity
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.fragment_message.*
import com.orhanobut.logger.Logger
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationProvider
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MessageFragment : BaseStateFragment<MessageContract.IView, MessageContract.IPresenter>(), MessageContract.IView {

    private var mPosition = 0
    private var mConversationInfo: ConversationInfo? = null
    private var mContent: String? = null

    companion object {
        fun newInstance() = MessageFragment()
    }

    override fun useImmersionBar() = true

    override fun useEventBus() = true

    override fun getContentView() = R.layout.fragment_message

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mImmersionBar.titleBar(title_bar).init()

        initConversationList()

        et_content.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }
            }
        })
        et_content.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mContent.isNullOrEmpty()) {
                        initConversationList()
                    } else {
                        ConversationManagerKit.getInstance().loadConversation(object : IUIKitCallBack {
                            override fun onSuccess(data: Any?) {
                                if (null != data) {
                                    val provide = data as ConversationProvider
                                    val dataSource = provide.dataSource.filter { it.title.contains(mContent!!) }
                                    provide.dataSource = dataSource
                                    conversation_layout.conversationList.adapter?.setDataProvider(provide)
                                    if (dataSource.isEmpty()) {
                                        showEmpty()
                                    } else {
                                        showNormal()
                                    }

                                } else {
                                    showEmpty()
                                }
                            }

                            override fun onError(module: String?, errCode: Int, errMsg: String?) {
                                showError()
                            }
                        })
                    }
                    return true
                }
                return false
            }
        })

        setOnClickListener(fl_official_notification, fl_logistics, fl_income, fl_system_message, iv_fans, iv_customer)
    }

    override fun initData() {
        //mPresenter?.notReadMessageSize()
    }

    override fun createPresenter() = MessagePresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mConversationInfo.run {
                conversation_layout.deleteConversation(mPosition, this)
            }
        }
    }

    override fun reload() {
        initConversationList()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_official_notification -> startActivity<OfficialNotificationActivity>()
            R.id.fl_logistics -> startActivity<TransactionLogisticsActivity>()
            R.id.fl_income -> startActivity<IncomeActivity>()
            R.id.fl_system_message -> startActivity<SystemMessageActivity>()
            R.id.iv_fans -> startActivity<ManagerInviteActivity>()
            R.id.iv_customer -> WebActivity.openActivity(context!!, "官方客服", Constant.SYSTEM_CUSTOMER)
        }
    }

    private fun initConversationList() {
        conversation_layout.run {
            initDefault()
            gone(titleBar)

            conversationList.enableItemRoundIcon(true)
            conversationList.adapter?.run {
                if (itemCount == 0) {
                    showEmpty()
                } else {
                    showNormal()
                }

                setOnItemClickListener { _, position, messageInfo ->
                    mPosition = position
                    mConversationInfo = messageInfo

                    val lastMessage = messageInfo.lastMessage
                    if (lastMessage.isGroup) {
                        AlertDialog.Builder(activity)
                                .setTitle("温馨提示")
                                .setMessage("该消息不是商家消息，需要从商家列表中移除")
                                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                    conversation_layout.deleteConversation(position, messageInfo)
                                    dialog.dismiss()
                                }
                                .setCancelable(false)
                                .show()
                    } else {
                        if (messageInfo.id.length == 11) {
                            startActivityForResult<OnlineCustomerActivity>(Constant.INTENT_REQUEST_CODE, "title" to messageInfo.title, "account_name" to messageInfo.id)
                        } else {
                            startActivityForResult<OnlineCustomerActivity>(Constant.INTENT_REQUEST_CODE, "title" to messageInfo.title, "user_id" to messageInfo.id.toInt())
                        }
                    }
                }
                setOnItemLongClickListener { _, position, messageInfo ->
                    mPosition = position
                    mConversationInfo = messageInfo

                    AlertDialog.Builder(activity)
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

    private fun isQQInstall(context: Context?): Boolean {
        val packageManager = context?.packageManager
        val pinfo = packageManager?.getInstalledPackages(0)
        pinfo?.indices?.map {
            pinfo[it].packageName
            // 通过遍历应用所有包名进行判断
        }?.filter { it == "com.tencent.mobileqq" }?.forEach { _ -> return true }
        return false
    }

    override fun getMessageListSuccess(bean: MessageBean) {

    }

    override fun notReadMessageSize(bean: MessageBean) {
        with(bean) {

        }
    }

    override fun searchMerchantIdSuccess(bean: UserTypeBean) {

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
