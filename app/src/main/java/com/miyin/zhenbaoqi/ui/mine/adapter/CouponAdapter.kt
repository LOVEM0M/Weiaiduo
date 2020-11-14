package com.miyin.zhenbaoqi.ui.mine.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils

class CouponAdapter(list: List<CouponBean.ListBean>, private val state: Int) : BaseAdapter<CouponBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_coupon

    override fun convert(holder: BaseViewHolder, data: CouponBean.ListBean) {
        with(data) {
            holder.setText(R.id.tv_price, SpanUtils()
                    .append("¥ ").setFontSize(15, true)
                    .append(FormatUtils.formatNumber(coupons_amount / 100f)).setFontSize(26, true).setBold()
                    .appendLine()
                    .append("满${FormatUtils.formatNumber(order_min_amount / 100f)}元可用").setFontSize(11, true)
                    .create())
                    .setText(R.id.tv_time, "过期时间：$use_end_date")
                    .setText(R.id.tv_desc, SpanUtils()
                            .appendLine(coupons_name ?: "")
                            .appendLine(coupons_descr ?: "").setFontSize(11, true)
                            .create())
                    .setVisible(R.id.iv_cover, state != 0)
                    .setVisible(R.id.tv_use, state == 0)
                    .addOnClickListener(R.id.tv_use)

            when (state) {
                1 -> {
                    holder.setBackgroundRes(R.id.iv_cover, R.drawable.ic_use)
                            .setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.text_99))
                            .setTextColor(R.id.tv_time, ContextCompat.getColor(mContext, R.color.text_99))
                            .setTextColor(R.id.tv_desc, ContextCompat.getColor(mContext, R.color.text_99))
                }
                2 -> {
                    holder.setBackgroundRes(R.id.iv_cover, R.drawable.ic_expired)
                            .setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.text_99))
                            .setTextColor(R.id.tv_time, ContextCompat.getColor(mContext, R.color.text_99))
                            .setTextColor(R.id.tv_desc, ContextCompat.getColor(mContext, R.color.text_99))
                }
                else -> {
                    holder.setTextColor(R.id.tv_time, ContextCompat.getColor(mContext, R.color.text_66))
                            .setTextColor(R.id.tv_desc, ContextCompat.getColor(mContext, R.color.text_66))
                            .setText(R.id.tv_price, SpanUtils()
                                    .append("¥ ").setFontSize(15, true).setForegroundColor(Color.parseColor("#FF3131"))
                                    .append(FormatUtils.formatNumber(coupons_amount / 100f)).setFontSize(26, true).setBold().setForegroundColor(Color.parseColor("#FF3131"))
                                    .appendLine()
                                    .append("满${FormatUtils.formatNumber(order_min_amount / 100f)}元可用").setFontSize(11, true).setForegroundColor(ContextCompat.getColor(mContext, R.color.text_66))
                                    .create())
                }
            }
        }
    }

}