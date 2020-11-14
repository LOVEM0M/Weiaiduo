package com.miyin.zhenbaoqi.ui.message.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.NoticeBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.TimeUtils

class OfficialNotificationAdapter(list: List<NoticeBean.ListBean>) : BaseAdapter<NoticeBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_official_notification

    override fun convert(holder: BaseViewHolder, data: NoticeBean.ListBean) {
        with(data) {
            val timestamp = mod_date?.toLong() ?: System.currentTimeMillis()
            holder.loadImg(R.id.iv_cover, arti_picture)
                    .setText(R.id.tv_date_time, TimeUtils.millis2String(timestamp, "yyyy-MM-dd"))
                    .setText(R.id.tv_title, arti_name)
        }
    }

}
