package com.miyin.zhenbaoqi.ui.shop.fragment

import androidx.core.content.ContextCompat

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.ReportBean
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantReportContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantReportPresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.fragment_funds_report.*

class FundsReportFragment : BaseMvpFragment<MerchantReportContract.IView, MerchantReportContract.IPresenter>(), MerchantReportContract.IView {

    companion object {
        fun newInstance() = FundsReportFragment()
    }

    override fun getContentView(): Int {
        return R.layout.fragment_funds_report
    }

    override fun initData() {
        mPresenter?.merchantsStatistics(1)
    }

    override fun createPresenter() = MerchantReportPresenter()

    override fun merchantsStatisticsSuccess(bean: ReportBean) {
        bean.data?.run {
            val color = ContextCompat.getColor(context!!, R.color.text_33)
            tv_auction_count.text = SpanUtils()
                    .append("今日成拍：")
                    .append("¥" + FormatUtils.formatNumber(hete_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_auction_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$hete_number").setForegroundColor(color)
                    .create()
            tv_pay_count.text = SpanUtils()
                    .append("今日已付款：")
                    .append("¥" + FormatUtils.formatNumber(pay_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_pay_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$pay_number").setForegroundColor(color)
                    .create()
            tv_refund_count.text = SpanUtils()
                    .append("今日已退款：")
                    .append("¥" + FormatUtils.formatNumber(back_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_refund_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$back_number").setForegroundColor(color)
                    .create()
            tv_profit_count.text = SpanUtils()
                    .append("今日已收款：")
                    .append("¥" + FormatUtils.formatNumber(put_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_wait_ship_count.text = SpanUtils()
                    .append("今日未发货：")
                    .append("¥" + FormatUtils.formatNumber(wait_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_wait_ship_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$wait_number").setForegroundColor(color)
                    .create()
            tv_wait_confirm_count.text = SpanUtils()
                    .append("今日待确认：")
                    .append("¥" + FormatUtils.formatNumber(waitconf_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_wait_confirm_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$waitconf_number").setForegroundColor(color)
                    .create()
            tv_return_goods_count.text = SpanUtils()
                    .append("今日退货中：")
                    .append("¥" + FormatUtils.formatNumber(refunding_amount / 100f)).setForegroundColor(color)
                    .create()
            tv_return_goods_number.text = SpanUtils()
                    .append("笔数：")
                    .append("$refunding_number").setForegroundColor(color)
                    .create()
        }
    }

}
