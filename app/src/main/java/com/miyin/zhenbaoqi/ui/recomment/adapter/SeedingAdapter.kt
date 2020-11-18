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


class SeedingAdapter(list: List<SeedingBean.DataBeanX.DataBean>) : BaseAdapter<SeedingBean.DataBeanX.DataBean>(list) {

    override fun getContentView() = R.layout.item_recommend_1

    override fun convert(holder: BaseViewHolder, data: SeedingBean.DataBeanX.DataBean) {
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
                images.isNullOrEmpty() -> ""
                images!!.contains(",") -> images!!.split(",")[0]
                else -> images
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title,title)
                    .setText(R.id.tv_buy_amount, "购买")

        }
    }

}
