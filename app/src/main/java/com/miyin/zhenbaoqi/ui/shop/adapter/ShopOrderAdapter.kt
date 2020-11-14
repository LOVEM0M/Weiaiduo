package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MerchantOrderBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils

class ShopOrderAdapter(list: List<MerchantOrderBean.ListBean>) : BaseAdapter<MerchantOrderBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_shop_order

    override fun convert(holder: BaseViewHolder, data: MerchantOrderBean.ListBean) {
        with(data) {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            val status = when (state) {
                1 -> "待支付"
                2 -> "待发货"
                3 -> "已发货"
                4 -> "已签收"
                5 -> "已评价"
                6 -> "售后中"
                7 -> "已退款"
                8 -> "退款失败"
                9 -> "已关闭"
                else -> ""
            }
            holder.loadImg(R.id.iv_avatar, head_img)
                    .setText(R.id.tv_name, nick_name)
                    .setText(R.id.tv_level, "V0青铜")
                    .setText(R.id.tv_status, status)
                    .loadImg(R.id.iv_cover, goodsImg)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_desc, "容量:230ml")
                    .setText(R.id.tv_count, "x$pay_number")
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥")
                            .append(FormatUtils.formatNumber(order_amount / 100f)).setFontSize(15, true)
                            .create())
                    .setText(R.id.tv_date_time, order_time)
                    .setText(R.id.tv_total, "共${pay_number}件商品\u3000合计: ¥${FormatUtils.formatNumber(pay_amount / 100f)}(含运费 ¥${FormatUtils.formatNumber(courier_amount / 100f)})")
        }
    }

}
