package com.miyin.zhenbaoqi.ui.sort.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.placeholder

class GoodsDetailPhotoAdapter(list: List<Any>) : BaseAdapter<Any>(list) {

    override fun getContentView() = R.layout.item_goods_detail_photo

    override fun convert(holder: BaseViewHolder, data: Any) {
        holder.placeholder(R.id.iv_cover, data, R.drawable.ic_live_bg)
    }

}