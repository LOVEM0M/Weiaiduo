package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.ThridDayBean

class LogisticsAdapter(list: List<ThridDayBean.DataBean>) : BaseAdapter<ThridDayBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_logistics

    override fun convert(holder: BaseViewHolder, data: ThridDayBean.DataBean) {
        holder.setText(R.id.tv_title, data.context)
                .setText(R.id.tv_date_time, data.time)

        when (holder.adapterPosition) {
            0 -> holder.setVisible(R.id.view_line_top, false)
                    .setVisible(R.id.view_line_bottom, true)
                    .setVisible(R.id.view_current, true)
                    .setVisible(R.id.view_point, false)
            mData.size - 1 -> holder.setVisible(R.id.view_line_top, true)
                    .setVisible(R.id.view_line_bottom, false)
                    .setVisible(R.id.view_current, false)
                    .setVisible(R.id.view_point, true)
            else -> holder.setVisible(R.id.view_line_top, true)
                    .setVisible(R.id.view_line_bottom, true)
                    .setVisible(R.id.view_current, false)
                    .setVisible(R.id.view_point, true)
        }
    }

}
