package com.miyin.zhenbaoqi.ui.live.fragment

import android.os.Bundle
import android.util.ArrayMap
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.LiveBannerBean
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.LiveRoomBean
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.ui.live.activity.PullLiveActivity
import com.miyin.zhenbaoqi.ui.live.adapter.LiveAdapter
import com.miyin.zhenbaoqi.ui.live.contract.LiveCategoryContract
import com.miyin.zhenbaoqi.ui.live.presenter.LiveCategoryPresenter
import kotlinx.android.synthetic.main.header_live.view.*
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LiveCategoryFragment : BaseListFragment<LiveCategoryContract.IView, LiveCategoryContract.IPresenter>(), LiveCategoryContract.IView {

    private var mState = 0
    private var mList = mutableListOf<LiveRoomBean.ListBean>()
    private var mAdapter: LiveAdapter? = null
    private var mHeaderView: View? = null
    private var title: String? = null

    companion object {
        fun newInstance(state: Int, title: String) = LiveCategoryFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
                putString("title", title)
            }
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
            title = getString("title")
        }
        return R.layout.fragment_live_category
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = LiveAdapter(mList)
//            mAdapter?.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter?.setOnItemClickListener { _, _, position ->
                mPresenter?.liveRoomEntry(mList[position].room_id, position)
            }

            /*mHeaderView = LayoutInflater.from(context).inflate(R.layout.header_live, this, false).apply {
                banner.setDelayTime(3000)
                        .setImageLoader(object : ImageLoaderInterface {
                            override fun createImageView(context: Context, path: Any?) = ImageView(context).apply {
                                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                                val size = context.getDimensionPixelSize(R.dimen.dp_10)
                                setPadding(size, 0, size, 0)
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }

                            override fun displayImage(context: Context, path: Any?, imageView: View) {
                                val transform = CornerTransform(context, DensityUtils.dp2px(6f).toFloat())
                                (imageView as ImageView).transform((path as LiveBannerBean.ListBean).img_url, transform)
                            }
                        })
                        .start()
            }
            mAdapter?.addHeaderView(mHeaderView)*/
        }
        refreshAndLoadMore(smart_refresh_layout) {
            initData()
        }
    }

    override fun initData() {
        if (mPage == 1) {
//            mPresenter?.getLiveBanner()
        }
        mPresenter?.getLiveRoomList(mPage, mCount, mState, title)
    }

    override fun createPresenter() = LiveCategoryPresenter()

    override fun onStart() {
        super.onStart()
        mHeaderView?.banner?.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        mHeaderView?.banner?.stopAutoPlay()
    }

    override fun onDestroyView() {
        mList.clear()
        mAdapter?.removeAllHeaderView()
        mAdapter = null
        mHeaderView = null
        super.onDestroyView()
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getLiveBanner(bean: LiveBannerBean) {
        mHeaderView?.banner?.update(bean.list)
    }

    override fun getLiveRoomListSuccess(bean: LiveRoomBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter?.setNewData(mList)
            } else {
                mAdapter?.addData(list!!)
            }

            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter?.removeAllFooterView()
            } else {
                mAdapter?.addNoMoreDataFooter()
            }
        }
    }

    override fun setEmptyView() {
        mList.clear()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, recycler_view, false)
        mAdapter?.emptyView = emptyView

        smart_refresh_layout.setEnableLoadMore(false)
    }

    override fun liveRoomEntrySuccess(data: LiveEntryRoomBean, position: Int) {
        val bean = mList[position]
//        startActivity<VerticalPullLiveActivity>("list" to mList, "position" to position)
        startActivity<PullLiveActivity>("room_id" to bean.room_id,
                "play_url" to bean.play_url, "cover_url" to bean.face_url,
                "name" to bean.room_name, "icon_url" to bean.icon_url,
                "fans_count" to bean.fans_no, "is_focus" to bean.is_focus,
                "merchant_id" to bean.merchants_id, "user_id" to bean.user_id,
                "account_name" to data.data?.accuount_name, "position" to position
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(map: ArrayMap<String, Any>) {
        val type = map["type"].toString()
        if (type == "refreshLiveList") {
            val position = map["position"].toString().toDouble().toInt()
            if (position != -1) {
                val bean = mList[position]
                bean.is_focus = true
            }
        }
    }

}
