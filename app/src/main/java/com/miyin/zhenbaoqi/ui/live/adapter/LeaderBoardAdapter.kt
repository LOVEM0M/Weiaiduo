package com.miyin.zhenbaoqi.ui.live.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveShareBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.SpanUtils

class LeaderBoardAdapter(list: List<LiveShareBean.LiveShareListBean>) : BaseAdapter<LiveShareBean.LiveShareListBean>(list) {

    override fun getContentView() = R.layout.item_leader_board

    override fun convert(holder: BaseViewHolder, data: LiveShareBean.LiveShareListBean) {
        with(data) {
            val position = holder.adapterPosition
            holder.loadImg(R.id.iv_avatar, icon_url)
                    .setText(R.id.tv_title, if (room_name.isEmpty()) "商家很懒，什么信息也没有留下" else room_name)
                    .loadImg(R.id.iv_level, user_grade.icon)
                    .setText(R.id.tv_integral, SpanUtils()
                            .append("邀请积分: ")
                            .append("$popularity").setForegroundColor(0xFFE62F2D.toInt())
                            .create())
            if (position > 2) {
                holder.setVisible(R.id.tv_share_count, true)
                        .setVisible(R.id.img_share_count, false)
                        .setText(R.id.tv_share_count, "${position + 1}")
            } else {
                holder.setVisible(R.id.tv_share_count, false)
                        .setVisible(R.id.img_share_count, true)
                when (position) {
                    0 -> holder.loadImg(R.id.img_share_count, R.drawable.ic_level_first)
                    1 -> holder.loadImg(R.id.img_share_count, R.drawable.ic_level_second)
                    2 -> holder.loadImg(R.id.img_share_count, R.drawable.ic_level_third)
                    else -> {
                    }
                }
            }
        }
    }

}
