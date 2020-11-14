package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.SpanUtils

class ManagerGoodsAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_manager_goods

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.loadImg(R.id.iv_cover, "https://p1.meituan.net/dpmerchantpic/9d1b7f6131615b546f7b2b28daf1b18a2295438.jpg%40watermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20")
                    .setText(R.id.tv_title, "商品信息昵称")
                    .setText(R.id.tv_desc, "已售：3件    库存：200件")
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥ ").setFontSize(12, true)
                            .append("5000").setFontSize(15, true)
                            .create())
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_take_off)
                    .addOnClickListener(R.id.tv_top)
        }
    }

}
