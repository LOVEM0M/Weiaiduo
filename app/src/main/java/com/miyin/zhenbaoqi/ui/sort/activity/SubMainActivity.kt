package com.miyin.zhenbaoqi.ui.sort.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.SubAccountLoginBean
import com.miyin.zhenbaoqi.ext.clearTask
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.live.activity.PullLiveActivity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.ui.shop.activity.OperateGoodsActivity
import com.miyin.zhenbaoqi.ui.shop.activity.order.ShopOrderActivity
import com.miyin.zhenbaoqi.ui.sort.contract.SubMainContract
import com.miyin.zhenbaoqi.ui.sort.presenter.SubMainPresenter
import com.miyin.zhenbaoqi.utils.ActivityManager
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import kotlinx.android.synthetic.main.activity_sub_main.*

class SubMainActivity : BaseMvpActivity<SubMainContract.IView, SubMainContract.IPresenter>(), SubMainContract.IView {

    private var mType = 0
    private var mBean: SubAccountLoginBean.DataBean.RoomBean? = null
    private var mLastClickTime = 0L
    private var mIsLive = false

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as SubAccountLoginBean.DataBean.RoomBean?
        return R.layout.activity_sub_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("子账号")
        immersionBar { statusBarDarkFont(true) }

        mType = SPUtils.getInt("type")
        tv_operate.text = when (mType) {
            0 -> {
                visible(fl_chat_list)
                "聊天"
            }
            1 -> {
                gone(fl_chat_list)
                "发货"
            }
            2 -> {
                gone(fl_chat_list)
                "商品"
            }
            else -> {
                gone(fl_chat_list)
                "聊天"
            }
        }

        setOnClickListener(tv_operate, fl_chat_list, tv_login_out)
    }

    override fun initData() {
        ConversationManagerKit.getInstance().addUnreadWatcher { count ->
            if (count > 0) {
                visible(tv_not_read_message)
            } else {
                gone(tv_not_read_message)
            }
            var unreadStr = "$count"
            if (count > 100) {
                unreadStr = "99+"
            }
            tv_not_read_message.text = unreadStr
        }
    }

    override fun createPresenter() = SubMainPresenter()

    override fun onDestroy() {
        ConversationManagerKit.getInstance().destroyConversation()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_operate -> {
                when (mType) {
                    0 -> {
                        if (null == mBean || mBean!!.play_url.isNullOrEmpty()) {
                            showToast("该主播尚未开播")
                        } else {
                            mBean?.run {
                                mPresenter?.liveRoomEntry(room_id, false)
                            }
                        }
                    }
                    1 -> {
                        startActivity<ShopOrderActivity>("tag" to 1)
                    }
                    2 -> {
                        if (null == mBean || mBean!!.play_url.isNullOrEmpty()) {
                            showToast("该主播尚未开播")
                        } else {
                            mBean?.run {
                                AlertDialog.Builder(this@SubMainActivity)
                                        .setTitle("提示")
                                        .setMessage("是否进入直播间发货？(竞拍商品请进入直播间发货)")
                                        .setNeutralButton(getString(R.string.cancel), null)
                                        .setNegativeButton("去发货") { dialog, _ ->
                                            dialog.dismiss()
                                            mIsLive = false
                                            startActivity<OperateGoodsActivity>("room_id" to room_id, "is_live" to mIsLive)
                                        }
                                        .setPositiveButton("进入直播间") { dialog, _ ->
                                            mIsLive = true
                                            mPresenter?.liveRoomEntry(room_id, mIsLive)
                                            dialog.dismiss()
                                        }
                                        .show()
                            }
                        }
                    }
                }
            }
            R.id.fl_chat_list -> {
                startActivity<ChatListActivity>()
            }
            R.id.tv_login_out -> {
                clearTask<WXLoginActivity>()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentClickTime = System.currentTimeMillis()
            if (currentClickTime - mLastClickTime > 2 * 1000) {
                mLastClickTime = currentClickTime
                ToastUtils.showToast("再按一次退出应用")
                return true
            } else {
                ActivityManager.exit()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun liveRoomEntrySuccess(isShowLiveGoods: Boolean) {
        mBean?.run {
            startActivity<PullLiveActivity>("room_id" to room_id, "play_url" to play_url, "cover_url" to face_url,
                    "name" to room_name, "icon_url" to icon_url, "fans_count" to fans_no, "is_focus" to is_focus,
                    "merchant_id" to merchants_id, "user_id" to user_id, "is_sub_account" to true, "is_show_live_goods" to isShowLiveGoods)
        }
    }

}
