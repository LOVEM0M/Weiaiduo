package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class VideoAdapter(list: List<HomeVideoBean.DataBean>) : com.miyin.zhenbaoqi.base.BaseAdapter<HomeVideoBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_video

    override fun convert(holder: BaseViewHolder, data: HomeVideoBean.DataBean) {
        with(data) {
            val transformer = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.TOP)
            val url = if (cover_url.isNullOrEmpty()) media_url else cover_url
            holder.transform(R.id.iv_cover, url, transformer)
                    .setText(R.id.tv_title, video_describe)
        }
    }

}
