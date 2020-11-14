package com.miyin.zhenbaoqi.ui.live.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class AudienceAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_audience

    override fun convert(holder: BaseViewHolder, data: String?) {
        holder.loadImg(R.id.iv_avatar, "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4073180673,464610865&fm=26&gp=0.jpg")
    }

}
