package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.AuctionOptionMultiBean
import com.miyin.zhenbaoqi.ext.setSelect

class AuctionOptionAdapter(list: List<AuctionOptionMultiBean>) : BaseMultiItemQuickAdapter<AuctionOptionMultiBean, BaseViewHolder>(list) {

    private var mPosition = 0

    init {
        addItemType(0, R.layout.item_auction_option_header)
        addItemType(1, R.layout.item_auction_option)
    }

    override fun convert(holder: BaseViewHolder, data: AuctionOptionMultiBean) {
        when (data.itemType) {
            0 -> holder.setText(R.id.tv_title, data.title)
            1 -> holder.setText(R.id.tv_title, data.title)
                    .setSelect(R.id.tv_title, holder.adapterPosition == mPosition)
                    .addOnClickListener(R.id.tv_title)
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

}