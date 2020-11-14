package com.miyin.zhenbaoqi.ui.mine.fragment

import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.activity.NewcomerWelfareActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.CouponAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.CouponContract
import com.miyin.zhenbaoqi.ui.mine.presenter.CouponPresenter
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.EventBus


class CouponFragment : BaseListFragment<CouponContract.IView, CouponContract.IPresenter>(), CouponContract.IView {

    private var mState = 0
    private var mList = mutableListOf<CouponBean.ListBean>()
    private lateinit var mAdapter: CouponAdapter

    companion object {
        fun newInstance(state: Int) = CouponFragment().apply {
            val bundle = Bundle()
            bundle.putInt("state", state)
            arguments = bundle
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_coupon
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            //            val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right)
//            layoutAnimation = animation

            mAdapter = CouponAdapter(mList, mState)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, _ ->
                if (view.id == R.id.tv_use) {
                    // startActivity<NewcomerWelfareActivity>()
                    val map = ArrayMap<String, Any>().apply {
                        put("type", "switch")
                        put("position", 0)
                    }
                    EventBus.getDefault().post(map)
                    activity?.finish()
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getCouponList(mPage, mCount, mState)
    }

    override fun createPresenter() = CouponPresenter()

    override fun reload() {
        mPage = 1
        initData()
    }

    override fun getCouponListSuccess(bean: CouponBean) {
        bean.run {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

}
