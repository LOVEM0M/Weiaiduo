package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class SuppliesAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_supplies

    override fun convert(holder: BaseViewHolder, data: String?) {

    }

}
