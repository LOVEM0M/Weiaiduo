package com.miyin.zhenbaoqi.ui.mine.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CollectBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SpanUtils

class CollectAdapter(list: List<CollectBean.CollectListBean>) : BaseAdapter<CollectBean.CollectListBean>(list) {

    override fun getContentView() = R.layout.item_collect

    override fun convert(holder: BaseViewHolder, data: CollectBean.CollectListBean) {
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (holder.adapterPosition % 2 == 0) {
            params.leftMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_8)
        } else {
            params.leftMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_4)
        }

        holder.itemView.layoutParams = params

        with(data) {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img!!
            }
            val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.TOP)
            holder.transform(R.id.iv_cover, goodsImg, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥ ")
                            .append(FormatUtils.formatNumber(goods_amount  )).setFontSize(17, true)
                            .create())
        }
    }

}
