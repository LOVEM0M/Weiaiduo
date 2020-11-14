package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class ExchangeRecordAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_exchange_record

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.setText(R.id.tv_title, "免费的卖家名额")
                    .setText(R.id.tv_date_time, "2019-12-01 06:20")
                    .setText(R.id.tv_price, "-5000 玩家币")
        }
    }

}
