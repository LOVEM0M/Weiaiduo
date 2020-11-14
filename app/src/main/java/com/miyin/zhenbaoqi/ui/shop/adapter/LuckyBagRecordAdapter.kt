package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.utils.SpanUtils

class LuckyBagRecordAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_lucky_bag_record

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.setText(R.id.tv_title, SpanUtils()
                    .append("分享福袋\u3000")
                    .append("¥1000.00").setFontSize(12, true)
                    .create())
                    .setText(R.id.tv_desc, "已过期4/100，已绑定专粉2位")
                    .setText(R.id.tv_date_time, "12.11 15:20")
        }
    }

}
