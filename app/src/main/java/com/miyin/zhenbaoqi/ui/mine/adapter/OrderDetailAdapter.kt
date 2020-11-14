package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class OrderDetailAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_order_detail

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.loadImg(R.id.iv_cover, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577249832054&di=a6b92b2ff9b21ef803bc01f115203786&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170614%2F205Za4Q-8.jpg")
                    .setText(R.id.tv_title, "商品 名字商品 名字商品 名字商品 名字商品 名字商品 名字")
                    .setText(R.id.tv_price, "¥0.01")
                    .setText(R.id.tv_count, "数量x1")
        }
    }
}
