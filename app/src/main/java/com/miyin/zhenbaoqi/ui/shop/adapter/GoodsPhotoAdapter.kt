package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class GoodsPhotoAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_goods_photo

    override fun convert(holder: BaseViewHolder, data: String?) {
        holder.loadImg(R.id.iv_cover, data)
                .addOnClickListener(R.id.iv_cover)
                .addOnClickListener(R.id.iv_delete)
    }

}
