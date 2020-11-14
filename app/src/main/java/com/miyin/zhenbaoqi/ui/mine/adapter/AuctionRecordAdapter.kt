package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.AuctionRecoredBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class AuctionRecordAdapter(list: List<AuctionRecoredBean.ListBean>) : BaseAdapter<AuctionRecoredBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_auction_record

    override fun convert(holder: BaseViewHolder, data: AuctionRecoredBean.ListBean) {
        with(data) {
            val transform = CornerTransform(mContext, DensityUtils.dp2px(6f).toFloat())
            transform.setExceptCorner(false, false, true, true)

            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            holder.transform(R.id.iv_cover, goodsImg, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, SpanUtils()
                            .append("当前 ¥ ")
                            .append(FormatUtils.formatNumber(auction_bid_amount / 100f)).setFontSize(17, true)
                            .create())
                    .setText(R.id.tv_status, when (state) {
                        0 -> "竞拍中"
                        1 -> "淘汰"
                        2 -> "待支付"
                        3 -> "支付成功"
                        else -> "交易结束"
                    })
                    .setText(R.id.tv_result, when (state) {
                        0 -> "竞拍中"
                        1 -> "淘汰"
                        2 -> "待支付"
                        3 -> "支付成功"
                        else -> "交易结束"
                    })
        }
    }

}