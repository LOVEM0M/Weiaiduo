package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.WelfareCouponBean
import com.miyin.zhenbaoqi.utils.SpanUtils

class NewcomerWelfareAdapter(list: List<WelfareCouponBean.DataBean>) : com.miyin.zhenbaoqi.base.BaseAdapter<WelfareCouponBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_newcomer_welfare

    override fun convert(holder: BaseViewHolder, data: WelfareCouponBean.DataBean) {
        with(data) {
            holder.setText(R.id.tv_price, SpanUtils()
                    .append("${coupons_amount / 100}").setBold()
                    .append("元").setFontSize(11, true)
                    .create())
                    .setText(R.id.tv_desc, SpanUtils()
                            .appendLine("满${order_min_amount / 100}元可用")
                            .appendLine(usable_scope ?: "")
                            .appendLine()
                            .appendLine(remark ?: "")
                            .create())
        }
    }

}
