package com.miyin.zhenbaoqi.ui.recomment.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.bean.SeedingBean
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SpanUtils


class SeedingAdapter(list: List<SeedingBean.ListBean>) : BaseAdapter<SeedingBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_recommend_1

    override fun convert(holder: BaseViewHolder, data: SeedingBean.ListBean) {
        with(data) {
            val position = holder.adapterPosition

            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            if (position % 2 == 0) {
                params.rightMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
            } else {
                params.leftMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
            }
            holder.itemView.layoutParams = params

            val transform = RoundCornersTransform(DensityUtils.dp2px(8f).toFloat(), RoundCornersTransform.CornerType.TOP)
            val url = when {
               goods?.goods_img.isNullOrEmpty() -> ""
                goods?.goods_img!!.contains(",") -> goods!!.goods_img!!.split(",")[0]
                else -> goods!!.goods_img
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, goods?.goods_describe)
                    .setText(R.id.tv_buy_amount, "购买"+goods?.goods_sales)

        }
    }

}
