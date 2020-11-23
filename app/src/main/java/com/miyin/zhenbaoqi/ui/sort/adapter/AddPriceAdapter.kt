package com.miyin.zhenbaoqi.ui.sort.adapter

import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.AuctionGoodsRecordBean
import com.miyin.zhenbaoqi.ext.setSelect
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils

class AddPriceAdapter(list: List<AuctionGoodsRecordBean.ListBean>) : BaseAdapter<AuctionGoodsRecordBean.ListBean>(list) {

    private var mIsEnd = false

    override fun getContentView() = R.layout.item_add_price

    override fun convert(holder: BaseViewHolder, data: AuctionGoodsRecordBean.ListBean) {
        with(data) {
            holder.transform(R.id.iv_avatar, head_img, CircleCrop())
                    .setText(R.id.tv_name, nick_name)
                    .setText(R.id.tv_price, "¥${FormatUtils.formatNumber(auction_bid_amount  )}")
                    .setText(R.id.tv_status, "淘汰")
                    .setText(R.id.tv_date_time, update_time)
            if (holder.adapterPosition == 0) {
                if (state == 2) {
                    holder.setText(R.id.tv_status, "成功")
                            .setSelect(R.id.tv_status, true)
                } else {
                    holder.setText(R.id.tv_status, "领先")
                            .setSelect(R.id.tv_status, true)
                }
            } else {
                holder.setSelect(R.id.tv_status, false)
            }
        }
    }

    fun setAuctionState(isEnd: Boolean) {
        mIsEnd = isEnd
        notifyDataSetChanged()
    }

}
