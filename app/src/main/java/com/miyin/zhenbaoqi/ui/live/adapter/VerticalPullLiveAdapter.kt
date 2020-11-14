package com.miyin.zhenbaoqi.ui.live.adapter

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.EventBusBean
import com.miyin.zhenbaoqi.bean.LiveRoomBean
import com.miyin.zhenbaoqi.ui.live.IMLiveUtils
import com.miyin.zhenbaoqi.ui.live.callback.StandardCallback
import com.miyin.zhenbaoqi.ui.live.fragment.BlankFragment
import com.miyin.zhenbaoqi.ui.live.fragment.LiveInteractiveFragment
import com.orhanobut.logger.Logger
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayConfig
import com.tencent.rtmp.TXLivePlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import org.greenrobot.eventbus.EventBus

class VerticalPullLiveAdapter(list: List<LiveRoomBean.ListBean>, private val activity: AppCompatActivity) : BaseAdapter<LiveRoomBean.ListBean>(list) {

    private val mLivePlayerArray = SparseArray<TXLivePlayer>()
    private val mVideoViewArray = SparseArray<TXCloudVideoView>()

    override fun getContentView() = R.layout.item_vertical_pull_live

    override fun convert(holder: BaseViewHolder, data: LiveRoomBean.ListBean) {
        with(data) {
            val flContainer = holder.getView<FrameLayout>(R.id.fl_container)
            val viewPager = ViewPager(mContext).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            }
            flContainer.addView(viewPager)

            val videoView = holder.getView<TXCloudVideoView>(R.id.video_view)
            val ivCover = holder.getView<ImageView>(R.id.iv_cover)

            val mPlayConfig = TXLivePlayConfig()
            // 创建 player 对象 还是报那个错吗
            val mLivePlayer = TXLivePlayer(mContext).apply {
                // 关键 player 对象与界面 view
                setPlayerView(videoView)
                // 设置填充模式
                setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN)
                // 设置画面渲染方向
                setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT)

                // 启动播放
                startPlay(play_url, TXLivePlayer.PLAY_TYPE_LIVE_RTMP) // 推荐 FLV
                setPlayListener(object : ITXLivePlayListener {
                    override fun onPlayEvent(event: Int, param: Bundle?) {
                        Logger.d("event == $event")
                        if (event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                            AlertDialog.Builder(activity)
                                    .setTitle("温馨提示")
                                    .setMessage("该直播间已经关闭，您即将退出直播间")
                                    .setPositiveButton(mContext.getString(R.string.confirm)) { dialog, _ ->
                                        dialog.dismiss()

                                        IMLiveUtils.getIMLiveUtils(mContext).quitIMGroup(mContext, room_id.toString(), object : StandardCallback {
                                            override fun onError(errCode: Int, errInfo: String?) {
                                                activity.finish()
                                            }

                                            override fun onSuccess() {
                                                activity.finish()
                                            }
                                        })
                                    }
                                    .setCancelable(false)
                                    .show()
                        } else if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
                            ivCover.visibility = View.GONE
                        }
                    }

                    override fun onNetStatus(status: Bundle?) {

                    }
                })
            }

            val position = holder.adapterPosition
            mLivePlayerArray.put(position, mLivePlayer)
            mVideoViewArray.put(position, videoView)

            mPlayConfig.setAutoAdjustCacheTime(true)
            mPlayConfig.setMinAutoAdjustCacheTime(1f)
            mPlayConfig.setMaxAutoAdjustCacheTime(5f)
            mLivePlayer.setConfig(mPlayConfig)
            IMLiveUtils.getIMLiveUtils(mContext).joinIMGroup(mContext, room_id.toString(), object : StandardCallback {
                override fun onError(errCode: Int, errInfo: String?) {
                    Log.e("加入IM群聊", errInfo ?: "未知错误")
                    //showToast("进入聊天室失败")
                }

                override fun onSuccess() {
                    Log.e("加入IM群聊", "success")
                    val bean = EventBusBean(room_id)
                    EventBus.getDefault().post(bean)
                }
            })
            viewPager.run {
                adapter = object : FragmentPagerAdapter(activity.supportFragmentManager) {
                    override fun getItem(position: Int): Fragment {
                        return when (position) {
                            0 -> LiveInteractiveFragment.newInstance(room_id, face_url, room_name, icon_url, fans_no, is_focus, merchants_id, user_id, "", 0, false, false)
                            else -> BlankFragment.newInstance()
                        }
                    }

                    override fun getCount() = 2
                }
            }
        }
    }

    fun getLivePlayerArray() = mLivePlayerArray

    fun getVideoViewArray() = mVideoViewArray

}
