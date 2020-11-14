package com.miyin.zhenbaoqi.ui.live.activity

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.LiveRoomBean
import com.miyin.zhenbaoqi.ui.live.adapter.VerticalPullLiveAdapter
import kotlinx.android.synthetic.main.activity_vertical_pull_live.*

class VerticalPullLiveActivity : BaseActivity() {

    private var mPosition = 0
    private var mList = mutableListOf<LiveRoomBean.ListBean>()
    private lateinit var mAdapter: VerticalPullLiveAdapter

    override fun getContentView(): Int {
        with(intent) {
            mPosition = getIntExtra("position", 0)
            mList = (getSerializableExtra("list") as List<LiveRoomBean.ListBean>).toMutableList()
        }
        return R.layout.activity_vertical_pull_live
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()

        recycler_view.run {
            mAdapter = VerticalPullLiveAdapter(mList, this@VerticalPullLiveActivity)
            adapter = mAdapter
            setCurrentItem(mPosition, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val livePlayer = mAdapter.getLivePlayerArray()[mPosition]
                    val videoView = mAdapter.getVideoViewArray()[mPosition]
                    if (livePlayer.isPlaying) {
                        livePlayer.pause()
                        videoView.onPause()
                    } else {
                        livePlayer.resume()
                        livePlayer.resumeLive()
                        videoView.onResume()
                    }

                    mPosition = position
                }
            })
        }
//        recycler_view.run {
//            mAdapter = VerticalPullLiveAdapter(mList)
//            adapter = mAdapter
//            layoutManager == LinearLayoutManager(context)
//        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        mAdapter.getLivePlayerArray()[mPosition].stopPlay(true)
        mAdapter.getVideoViewArray()[mPosition].onDestroy()

        mAdapter.getLivePlayerArray().clear()
        mAdapter.getVideoViewArray().clear()
        super.onDestroy()
    }

}
