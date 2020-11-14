package com.miyin.zhenbaoqi.ui.sort.adapter

import android.graphics.Color
import android.util.ArrayMap
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class GoodsInfoAdapter(list: List<ArrayMap<String, String>>) : BaseAdapter<ArrayMap<String, String>>(list) {

    override fun getContentView() = R.layout.item_goods_info

    override fun convert(holder: BaseViewHolder, data: ArrayMap<String, String>) {
        with(data) {
            holder.setText(R.id.tv_title, this["title"])
                    .setText(R.id.tv_desc, this["desc"])
                    .setBackgroundColor(R.id.fl_container, if (holder.layoutPosition % 2 == 0) Color.parseColor("#FBFBFB") else Color.WHITE)
        }
    }

}