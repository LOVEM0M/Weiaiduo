package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.DraftAuctionGoodsBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.JSONUtils

class DraftGoodsAdapter(list: List<AuctionGoodsDraftEntity>) : BaseAdapter<AuctionGoodsDraftEntity>(list) {

    override fun getContentView(): Int {
        return R.layout.item_manager_auction
    }

    override fun convert(holder: BaseViewHolder, data: AuctionGoodsDraftEntity) {
        val bean = JSONUtils.fromJSON<DraftAuctionGoodsBean>(data.data)
        with(bean) {
            val goodsImg = when {
                goodsImg.isNullOrEmpty() -> ""
                goodsImg!!.contains(",") -> goodsImg!!.split(",")[0]
                else -> goodsImg!!
            }
            holder.loadImg(R.id.iv_cover, goodsImg)
                    .setText(R.id.tv_title, goodsName)
                    .setText(R.id.tv_price, "¥${FormatUtils.formatNumber(initPrice  )}")
                    .setGone(R.id.tv_top, false)
                    .setVisible(R.id.tv_delete, true)
                    .setVisible(R.id.tv_edit, true)
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_delete)
        }
    }

}
