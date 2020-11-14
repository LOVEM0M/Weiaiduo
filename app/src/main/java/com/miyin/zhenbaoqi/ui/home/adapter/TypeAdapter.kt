package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class TypeAdapter(list: List<String>) : com.miyin.zhenbaoqi.base.BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_type

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.setText(R.id.tv_title, "玉翠珠宝")
        }
    }

}
