package com.miyin.zhenbaoqi.ui.shop.fragment

import androidx.core.content.ContextCompat

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.ReportBean
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantReportContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantReportPresenter
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.fragment_manage_report.*

class ManageReportFragment : BaseMvpFragment<MerchantReportContract.IView, MerchantReportContract.IPresenter>(), MerchantReportContract.IView {

    companion object {
        fun newInstance() = ManageReportFragment()
    }

    override fun getContentView(): Int {
        return R.layout.fragment_manage_report
    }

    override fun initData() {
        mPresenter?.merchantsStatistics(0)
    }

    override fun createPresenter() = MerchantReportPresenter()

    override fun merchantsStatisticsSuccess(bean: ReportBean) {
        bean.data?.run {
            val color = ContextCompat.getColor(context!!, R.color.text_33)
            tv_today_access_people.text = SpanUtils()
                    .append("今日访问人数：")
                    .append("$access_number").setForegroundColor(color)
                    .create()
            tv_today_access_count.text = SpanUtils()
                    .append("今日访问次数：")
                    .append("$access_count").setForegroundColor(color)
                    .create()
            tv_fans_count.text = SpanUtils()
                    .append("累计粉丝：")
                    .append("$total_fans").setForegroundColor(color)
                    .create()
            tv_add_fans_count.text = SpanUtils()
                    .append("今日增长粉丝：")
                    .append("$group_fans").setForegroundColor(color)
                    .create()
            tv_release_count.text = SpanUtils()
                    .append("今日发布件数：")
                    .append("$good_num").setForegroundColor(color)
                    .create()
            tv_pay_people.text = SpanUtils()
                    .append("今日付款人数：")
                    .append("$pay_number").setForegroundColor(color)
                    .create()
            tv_new_people.text = SpanUtils()
                    .append("今日新客占比：")
                    .append(if (ratio.isNullOrEmpty()) "0" else "$ratio").setForegroundColor(color)
                    .create()
        }
    }

}
