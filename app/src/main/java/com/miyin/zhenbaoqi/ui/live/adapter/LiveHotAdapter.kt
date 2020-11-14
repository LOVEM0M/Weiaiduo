package com.miyin.zhenbaoqi.ui.live.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveHotBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.SpanUtils

class LiveHotAdapter(list: List<LiveHotBean.LiveHotDataBean>) : BaseAdapter<LiveHotBean.LiveHotDataBean>(list) {

    override fun getContentView() = R.layout.item_live_hot

    override fun convert(holder: BaseViewHolder, data: LiveHotBean.LiveHotDataBean) {
        val position = holder.adapterPosition
        holder.loadImg(R.id.iv_avatar, data.icon_url)
                .setText(R.id.tv_hot_title, if (data.room_name.isEmpty()) "商家很懒，什么信息也没有留下" else data.room_name)
                .setText(R.id.tv_hot_content, "")
                .setText(R.id.tv_hot_heat, SpanUtils()
                        .append("${data.popularity}").setForegroundColor(0xFFE62F2D.toInt())
                        .create())
        if (position > 2) {
            holder.setVisible(R.id.tv_hot_count, true)
                    .setVisible(R.id.img_hot_count, false)
                    .setText(R.id.tv_hot_count, "${position + 1}")
        } else {
            holder.setVisible(R.id.tv_hot_count, false)
                    .setVisible(R.id.img_hot_count, true)
            when (position) {
                0 -> holder.loadImg(R.id.img_hot_count, R.drawable.ic_level_first)
                1 -> holder.loadImg(R.id.img_hot_count, R.drawable.ic_level_second)
                2 -> holder.loadImg(R.id.img_hot_count, R.drawable.ic_level_third)
            }
        }
    }

}
