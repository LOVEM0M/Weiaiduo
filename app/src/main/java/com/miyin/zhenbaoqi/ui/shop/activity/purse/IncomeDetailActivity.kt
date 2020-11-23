package com.miyin.zhenbaoqi.ui.shop.activity.purse

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.MerchantTotalIncomeBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.TotalIncomeContract
import com.miyin.zhenbaoqi.ui.shop.fragment.IncomeDetailFragment
import com.miyin.zhenbaoqi.ui.shop.presenter.TotalIncomePresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_income_detail.*

class IncomeDetailActivity : BaseMvpActivity<TotalIncomeContract.IView, TotalIncomeContract.IPresenter>(), TotalIncomeContract.IView {

    private var mTitle: String? = null
    private var mWithdrawPrice = 0L

    override fun getContentView(): Int {
        mTitle = intent.getStringExtra("title")
        return R.layout.activity_income_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar(mTitle ?: "")
        immersionBar { statusBarDarkFont(true, 0.2f) }

        val tag = if ((mTitle ?: "").contains("收益")) {
            tv_income_title.text = "收益收入（元）"
            0
        } else {
            1
        }
        val titleList = listOf("全部", "收入", "待入账", "退款", "提现")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(IncomeDetailFragment.newInstance(index, tag))
        }
        val adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = titleList.size
        tab_layout.setViewPager(view_pager)

        tv_withdraw.setOnClickListener {
            mTitle?.run {
                val title = if (this.contains("收益")) {
                    "收益提现"
                } else {
                    tv_income_title.text = "货款收入（元）"
                    "货款提现"
                }
                startActivityForResult<MerchantWithdrawActivity>(Constant.INTENT_REQUEST_CODE,
                        "title" to title, "price" to mWithdrawPrice)
            }
        }
    }

    override fun initData() {
        if ((mTitle ?: "").contains("收益")) {
            mPresenter?.getTotalIncome(0)
        } else {
            mPresenter?.getTotalIncome(1)
        }
    }

    override fun createPresenter() = TotalIncomePresenter()

    override fun getTotalIncomeSuccess(bean: MerchantTotalIncomeBean) {
        bean.data?.run {
            mWithdrawPrice = withdrawAmount
            tv_income.text = FormatUtils.formatNumber(totalAmount  )
            tv_wait_income.text = FormatUtils.formatNumber(waitAmount  )
            tv_withdraw_price.text = FormatUtils.formatNumber(withdrawAmount  )
        }
    }

}
