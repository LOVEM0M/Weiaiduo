package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.setSelect

class TimeAdapter(list: List<String>) : com.miyin.zhenbaoqi.base.BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_time

    override fun convert(holder: BaseViewHolder, data: String?) {
        val position = holder.adapterPosition
        holder.setText(R.id.tv_time, "10:00")
                .setSelect(R.id.tv_time, position == 0)
                .setText(R.id.tv_status, if (position == 0) "抢拍中" else "进行中")
                .setSelect(R.id.tv_status, position == 0)
                .setSelect(R.id.fl_container, position == 0)
    }

}
