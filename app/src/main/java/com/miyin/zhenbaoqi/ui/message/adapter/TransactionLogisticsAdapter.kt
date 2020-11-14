package com.miyin.zhenbaoqi.ui.message.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.utils.TimeUtils

class TransactionLogisticsAdapter(list: List<MessageBean.ListBean>) : BaseAdapter<MessageBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_transaction_logistics

    override fun convert(holder: BaseViewHolder, data: MessageBean.ListBean) {
        with(data) {
            val datetime = if (add_time.isNullOrEmpty()) System.currentTimeMillis() else add_time!!.toLong()
            holder.setText(R.id.tv_title, message_name)
                    .setText(R.id.tv_date_time, TimeUtils.millis2String(datetime))
                    .setText(R.id.tv_content, message_value)
        }
    }

}
