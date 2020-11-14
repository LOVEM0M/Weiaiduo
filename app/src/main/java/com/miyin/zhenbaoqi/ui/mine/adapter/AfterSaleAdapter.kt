package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.setSelect

class AfterSaleAdapter(list: List<String>) : BaseAdapter<String>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_after_sale

    override fun convert(holder: BaseViewHolder, data: String?) {
        holder.setSelect(R.id.tv_title, holder.adapterPosition == mPosition)
                .setText(R.id.tv_title, data)
                .addOnClickListener(R.id.tv_title)
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

}
