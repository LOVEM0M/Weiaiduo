package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.ext.loadImg

class ManagerVideoAdapter(list: List<HomeVideoBean.DataBean>) : BaseAdapter<HomeVideoBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_manager_video

    override fun convert(holder: BaseViewHolder, data: HomeVideoBean.DataBean) {
        with(data) {
            holder.setText(R.id.tv_date_time, update_time)
                    .setGone(R.id.tv_status, state == 0)
                    .setText(R.id.tv_status, update_time)
                    .loadImg(R.id.iv_cover, media_url)
                    .setText(R.id.tv_title, video_describe)
                    .setGone(R.id.tv_edit, state == 0)
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_delete)
        }
    }

}