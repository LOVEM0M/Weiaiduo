package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.HomeGoodsHotBean
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SpanUtils

class RecommendAdapter(list: List<HomeGoodsHotBean.DataBean>) : BaseAdapter<HomeGoodsHotBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_recommend

    override fun convert(holder: BaseViewHolder, data: HomeGoodsHotBean.DataBean) {
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
                goodsImg.isNullOrEmpty() -> ""
                goodsImg!!.contains(",") -> goodsImg!!.split(",")[0]
                else -> goodsImg
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, goodsName)
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥ ")
                            .append(FormatUtils.formatNumber(goodsAmount  )).setFontSize(15, true)
                            .create())
        }
    }

}
