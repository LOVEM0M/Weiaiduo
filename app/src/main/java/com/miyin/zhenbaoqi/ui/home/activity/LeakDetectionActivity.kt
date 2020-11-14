package com.miyin.zhenbaoqi.ui.home.activity

import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miyin.zhenbaoqi.ui.home.adapter.LeakDetectionAdapter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ui.home.adapter.TimeAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.TypeAdapter
import com.miyin.zhenbaoqi.ui.home.contract.LeakDetectionContract
import com.miyin.zhenbaoqi.ui.home.presenter.LeakDetectionPresenter
import com.miyin.zhenbaoqi.utils.CenterLayoutManager
import kotlinx.android.synthetic.main.activity_leak_detection.*
import kotlinx.android.synthetic.main.layout_refresh.*

class LeakDetectionActivity : BaseListActivity<LeakDetectionContract.IView, LeakDetectionContract.IPresenter>(), LeakDetectionContract.IView {

    private var mTimeList = mutableListOf<String>()
    private lateinit var mTimeAdapter: TimeAdapter
    private var mTypeList = mutableListOf<String>()
    private lateinit var mTypeAdapter: TypeAdapter
    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: LeakDetectionAdapter

    private lateinit var mCenterLayoutManager: CenterLayoutManager

    override fun getContentView() = R.layout.activity_leak_detection

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("极速捡漏")
        immersionBar { statusBarDarkFont(true) }

        rv_time.run {
            repeat((0..7).count()) { mTimeList.add("") }
            mTimeAdapter = TimeAdapter(mTimeList)
            adapter = mTimeAdapter
            mCenterLayoutManager = CenterLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = mCenterLayoutManager
        }
        rv_type.run {
            repeat((0..7).count()) { mTypeList.add("") }
            mTypeAdapter = TypeAdapter(mTypeList)
            adapter = mTypeAdapter
            layoutManager = GridLayoutManager(context, 4)
        }
        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = LeakDetectionAdapter(mList)
            adapter = mAdapter
            layoutManager = GridLayoutManager(applicationContext, 2)
        }

    }

    override fun initData() {
        Handler().postDelayed({
            mCenterLayoutManager.smoothScrollToPosition(rv_time, RecyclerView.State(), 3)
        }, 2500)

        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun createPresenter() = LeakDetectionPresenter()

}
