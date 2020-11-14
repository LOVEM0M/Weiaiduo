package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.setSelect

class DelayBidAdapter(list: List<Int>) : BaseAdapter<Int>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_delay_bid

    override fun convert(holder: BaseViewHolder, data: Int) {
        val position = holder.adapterPosition
        holder.setSelect(R.id.tv_title, position == mPosition)
                .setText(R.id.tv_title, "${data}分钟")
                .addOnClickListener(R.id.tv_title)
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

}
