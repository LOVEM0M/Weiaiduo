package com.miyin.zhenbaoqi.ui.shop.activity.profit

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.InviteRewardAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.InviteRewardContract
import com.miyin.zhenbaoqi.ui.shop.presenter.InviteRewardPresenter
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_invite_reward.*
import kotlinx.android.synthetic.main.layout_refresh.*

class InviteRewardActivity : BaseListActivity<InviteRewardContract.IView, InviteRewardContract.IPresenter>(), InviteRewardContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: InviteRewardAdapter

    override fun getContentView() = R.layout.activity_invite_reward

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("邀请奖励")
        immersionBar { statusBarDarkFont(true) }

        tv_invite_count.text = SpanUtils()
                .appendLine("345").setFontSize(18, true).setForegroundColor(ContextCompat.getColor(this, R.color.text_33)).setBold()
                .append("邀请人数")
                .create()
        tv_brokerage_income.text = SpanUtils()
                .appendLine("8869.98").setFontSize(18, true).setForegroundColor(ContextCompat.getColor(this, R.color.text_33)).setBold()
                .append("佣金收益")
                .create()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = InviteRewardAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            val emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, recycler_view, false)
            mAdapter.emptyView = emptyView
        } else {

        }
    }

    override fun createPresenter() = InviteRewardPresenter()

}
