package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.ReportBean
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantReportContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantReportPresenter
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.fragment_live_report.*

class LiveReportFragment : BaseMvpFragment<MerchantReportContract.IView, MerchantReportContract.IPresenter>(), MerchantReportContract.IView {

    companion object {
        fun newInstance() = LiveReportFragment()
    }

    override fun getContentView(): Int {
        return R.layout.fragment_live_report
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        val color = ContextCompat.getColor(context!!, R.color.text_33)
        tv_watch_count.text = SpanUtils()
                .append("今日观看人数：")
                .append("0").setForegroundColor(color)
                .create()
    }

    override fun initData() {
        mPresenter?.merchantsStatistics(2)
    }

    override fun createPresenter() = MerchantReportPresenter()

    override fun merchantsStatisticsSuccess(bean: ReportBean) {
        bean.data?.run {

        }
    }

}
