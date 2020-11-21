package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class GoodsSearchAdapter(list: List<GoodsSearchBean.ListBean>) : BaseAdapter<GoodsSearchBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_goods_search

    override fun convert(holder: BaseViewHolder, data: GoodsSearchBean.ListBean) {
        with(data) {
            val url = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.TOP)
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, "¥${FormatUtils.formatNumber(goods_amount / 100f)}")
        }
    }

}