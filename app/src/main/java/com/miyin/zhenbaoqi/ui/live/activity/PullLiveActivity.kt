package com.miyin.zhenbaoqi.ui.live.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.EventBusBean
import com.miyin.zhenbaoqi.ui.live.IMLiveUtils
import com.miyin.zhenbaoqi.ui.live.callback.StandardCallback
import com.miyin.zhenbaoqi.ui.live.contract.PullLiveContract
import com.miyin.zhenbaoqi.ui.live.fragment.BlankFragment
import com.miyin.zhenbaoqi.ui.live.fragment.LiveInteractiveFragment
import com.miyin.zhenbaoqi.ui.live.presenter.PullLivePresenter
import com.orhanobut.logger.Logger
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayConfig
import com.tencent.rtmp.TXLivePlayer
import kotlinx.android.synthetic.main.activity_pull_live.*
import org.greenrobot.eventbus.EventBus

class PullLiveActivity : BaseMvpActivity<PullLiveContract.IView, PullLiveContract.IPresenter>(), PullLiveContract.IView {

    private var mRoomId = 0
    private var mFansCount = 0
    private var mIsFocus = false
    private var mPlayUrl: String? = null
    private var mCoverUrl: String? = null
    private var mName: String? = null
    private var mIconUrl: String? = null
    private var mContext: Context = this
    private var mLivePlayer: TXLivePlayer? = null
    private var mMerchantId = 0
    private var mUserId = 0
    private var mAccountName: String? = null
    private var mPosition = 0
    private var mIsSubAccount = false
    private var mIsShowLiveGoods = true

    override fun getContentView(): Int {
        ImmersionBar.hideStatusBar(window)
        with(intent) {
            mRoomId = getIntExtra("room_id", 0)
            mPlayUrl = getStringExtra("play_url")
            mCoverUrl = getStringExtra("cover_url")
            mIconUrl = getStringExtra("icon_url")
            mName = getStringExtra("name")
            mFansCount = getIntExtra("fans_count", 0)
            mIsFocus = getBooleanExtra("is_focus", false)
            mMerchantId = getIntExtra("merchant_id", 0)
            mUserId = getIntExtra("user_id", 0)
            mAccountName = getStringExtra("account_name")
            mPosition = getIntExtra("position", 0)
            mIsSubAccount = getBooleanExtra("is_sub_account", false)
            mIsShowLiveGoods = getBooleanExtra("is_show_live_goods", true)
        }
        return R.layout.activity_pull_live
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val mPlayConfig = TXLivePlayConfig()
        // 创建 player 对象 还是报那个错吗
        mLivePlayer = TXLivePlayer(mContext).apply {
            // 关键 player 对象与界面 view
            setPlayerView(video_view)
            // 设置填充模式
            setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN)
            // 设置画面渲染方向
            setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT)
            gone(iv_cover)
            visible(video_view)
            // 启动播放
            startPlay(mPlayUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP) // 推荐 FLV
            setPlayListener(object : ITXLivePlayListener {
                override fun onPlayEvent(event: Int, param: Bundle?) {
                    Logger.d("event == $event")
                    if (event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                        AlertDialog.Builder(this@PullLiveActivity)
                                .setTitle("温馨提示")
                                .setMessage("您已经与该主播断开连接，即将退出直播间")
                                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                    dialog.dismiss()
                                    IMLiveUtils.getIMLiveUtils(this@PullLiveActivity).quitIMGroup(this@PullLiveActivity, mRoomId.toString(), object : StandardCallback {
                                        override fun onError(errCode: Int, errInfo: String?) {
                                            EventBus.getDefault().post("refreshTXIMList")
                                            finish()
                                        }

                                        override fun onSuccess() {
                                            EventBus.getDefault().post("refreshTXIMList")
                                            finish()
                                        }
                                    })
                                }
                                .setCancelable(false)
                                .show()
                    }
                }

                override fun onNetStatus(status: Bundle?) {

                }
            })
        }
        mPlayConfig.setAutoAdjustCacheTime(true)
        mPlayConfig.setMinAutoAdjustCacheTime(1f)
        mPlayConfig.setMaxAutoAdjustCacheTime(5f)
        mLivePlayer?.setConfig(mPlayConfig)
        IMLiveUtils.getIMLiveUtils(mContext).joinIMGroup(mContext, mRoomId.toString(), object : StandardCallback {
            override fun onError(errCode: Int, errInfo: String?) {
                Log.e("加入IM群聊", "err_code == $errCode, err_info == $errInfo")
                AlertDialog.Builder(this@PullLiveActivity)
                        .setTitle("温馨提示")
                        .setMessage("进入直播间失败，即将退出直播间")
                        .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .setCancelable(false)
                        .show()
            }

            override fun onSuccess() {
                Log.e("加入IM群聊", "success")
                val bean = EventBusBean(mRoomId)
                EventBus.getDefault().post(bean)
            }
        })
        view_pager.run {
            adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> LiveInteractiveFragment.newInstance(mRoomId, mCoverUrl, mName, mIconUrl, mFansCount, mIsFocus, mMerchantId, mUserId, mAccountName, mPosition, mIsSubAccount, mIsShowLiveGoods)
                        else -> BlankFragment.newInstance()
                    }
                }

                override fun getCount() = 2
            }
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = PullLivePresenter()

    override fun onResume() {
        super.onResume()
        mLivePlayer?.resume()
    }

    override fun onPause() {
        super.onPause()
        mLivePlayer?.pause()
    }

    override fun onDestroy() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mLivePlayer?.stopPlay(true)
        video_view.onDestroy()
        super.onDestroy()
    }

}
