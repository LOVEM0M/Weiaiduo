package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.WeekNewGoodsBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform


class WeekNewGoodsAdapter(data: List<WeekNewGoodsBean.DataBeanX.DataBean>) : BaseAdapter<WeekNewGoodsBean.DataBeanX.DataBean>(data) {
    override fun getContentView() = R.layout.item_new_pro_week
    override fun convert(holder: BaseViewHolder, data: WeekNewGoodsBean.DataBeanX.DataBean?) {
        with(data) {
            val position = holder.adapterPosition
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            holder.itemView.layoutParams = params
            val transform = RoundCornersTransform(DensityUtils.dp2px(8f).toFloat(), RoundCornersTransform.CornerType.TOP)
            val url = when {
                this?.goodsImg.isNullOrEmpty() -> ""
              this?.goodsImg!!.contains(",") -> goodsImg!!.split(",")[0]
                else -> goodsImg
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, this?.goodsDescribe)
                    .setText(R.id.tv_price, "￥"+this?.goodsOriginalAmount)

        }
    }


}
