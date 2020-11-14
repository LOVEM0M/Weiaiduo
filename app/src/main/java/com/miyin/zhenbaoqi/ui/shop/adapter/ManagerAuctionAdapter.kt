package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils

class ManagerAuctionAdapter(list: List<MerchantGoodsBean.ListBean>, private val state: Int) : BaseAdapter<MerchantGoodsBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_manager_auction

    override fun convert(holder: BaseViewHolder, data: MerchantGoodsBean.ListBean) {
        with(data) {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            holder.loadImg(R.id.iv_cover, goodsImg)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, "¥${FormatUtils.formatNumber(goods_amount / 100f)}")
                    .setGone(R.id.tv_top, state == 1)
                    .setGone(R.id.tv_delete, state == 3 || state == 2)
                    .setGone(R.id.tv_edit, false)
                    .addOnClickListener(R.id.cl_container)
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_delete)
                    .addOnClickListener(R.id.tv_top)
        }
    }

}
