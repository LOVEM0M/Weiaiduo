package com.miyin.zhenbaoqi.ui.mine.activity.wallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.mine.fragment.WalletFragment
import kotlinx.android.synthetic.main.activity_balance.*
import java.text.DecimalFormat

@SuppressLint("SetTextI18n")
class BalanceActivity : BaseActivity() {

    private var mBalance = 0f

    override fun getContentView(): Int {
        mBalance = intent.getFloatExtra("balance", 0f)
        return R.layout.activity_balance
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("余额")
        immersionBar { statusBarDarkFont(true) }

        tv_balance.text = "¥${DecimalFormat("0.00").format(mBalance)}"

        val titleList = listOf("全部", "收入", "提现", "支出")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(WalletFragment.newInstance(index))
        }

        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size

        setOnClickListener(tv_withdraw)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_withdraw -> startActivityForResult<WithdrawActivity>(Constant.INTENT_REQUEST_CODE, "balance" to mBalance)
        }
    }

}
