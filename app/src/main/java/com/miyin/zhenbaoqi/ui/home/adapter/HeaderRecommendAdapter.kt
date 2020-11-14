package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class HeaderRecommendAdapter(list: List<String>) : com.miyin.zhenbaoqi.base.BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_header_recommed

    override fun convert(holder: BaseViewHolder, item: String?) {
        if (holder.adapterPosition == mData.size - 1) {
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.rightMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_8)
            holder.itemView.layoutParams = params
        }

        val transform = CornerTransform(mContext, DensityUtils.dp2px(5f).toFloat())
        val url = "https://p0.meituan.net/wedding/d956774f921be76e784fe39e18251be0496721.jpg%40800w_600h_0e_1l%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20"
        holder.transform(R.id.iv_cover, url, transform)
                .setText(R.id.tv_price, SpanUtils()
                        .append("¥").setFontSize(12, true)
                        .append("128")
                        .create())
    }

}
