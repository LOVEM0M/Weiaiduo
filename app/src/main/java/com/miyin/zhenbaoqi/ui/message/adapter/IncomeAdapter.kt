package com.miyin.zhenbaoqi.ui.message.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.utils.TimeUtils

class IncomeAdapter(list: List<MessageBean.ListBean>) : BaseAdapter<MessageBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_imcome

    override fun convert(holder: BaseViewHolder, data: MessageBean.ListBean) {
        with(data) {
            holder.setText(R.id.tv_date_time, TimeUtils.millis2String(add_time!!.toLong(), "MM.dd HH.mm"))
                    .setText(R.id.tv_name, message_name)
                    .setText(R.id.tv_time, TimeUtils.millis2String(add_time!!.toLong(), "yyyy-MM-dd"))
                    .setText(R.id.tv_title, message_value)
        }
    }

}
