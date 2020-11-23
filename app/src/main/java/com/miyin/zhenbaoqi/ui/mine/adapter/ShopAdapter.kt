package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.ShopAttentionBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SpanUtils

class ShopAdapter(list: List<ShopAttentionBean.ShopListBean>) : BaseAdapter<ShopAttentionBean.ShopListBean>(list) {

    override fun getContentView() = R.layout.item_shop

    override fun convert(holder: BaseViewHolder, data: ShopAttentionBean.ShopListBean) {
        val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
        with(data) {
            holder.transform(R.id.iv_cover, head_img, transform)
                    .setText(R.id.tv_title, merchants_name)
                    .setText(R.id.tv_desc, merchants_subtitle)
                    .addOnClickListener(R.id.tv_entry_shop)

            list?.forEachIndexed { index, bean ->
                val goodsImg = when {
                    bean.goods_img.isNullOrEmpty() -> ""
                    bean.goods_img!!.contains(",") -> bean.goods_img!!.split(",")[0]
                    else -> bean.goods_img!!
                }
                when (index) {
                    0 -> {
                        holder.transform(R.id.iv_left, goodsImg, transform)
                                .setText(R.id.tv_left_title, bean.goods_name)
                                .setText(R.id.tv_left_price, SpanUtils()
                                        .append("¥").setFontSize(9, true)
                                        .append(FormatUtils.formatNumber(bean.goods_amount  ))
                                        .create())
                    }
                    1 -> {
                        holder.transform(R.id.iv_middle, goodsImg, transform)
                                .setText(R.id.tv_middle_title, bean.goods_name)
                                .setText(R.id.tv_middle_price, SpanUtils()
                                        .append("¥").setFontSize(9, true)
                                        .append(FormatUtils.formatNumber(bean.goods_amount  ))
                                        .create())
                    }
                    2 -> {
                        holder.transform(R.id.iv_right, goodsImg, transform)
                                .setText(R.id.tv_right_title, bean.goods_name)
                                .setText(R.id.tv_right_price, SpanUtils()
                                        .append("¥").setFontSize(9, true)
                                        .append(FormatUtils.formatNumber(bean.goods_amount  ))
                                        .create())
                    }
                }
            }
        }
    }

}
