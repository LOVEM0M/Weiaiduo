package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class LeakDetectionAdapter(list: List<String>) : com.miyin.zhenbaoqi.base.BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_leak_detection

    override fun convert(holder: BaseViewHolder, data: String?) {
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (holder.adapterPosition % 2 == 0) {
            params.leftMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_19)
        } else {
            params.leftMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_5)
        }
        holder.itemView.layoutParams = params

        val transform = CornerTransform(mContext, DensityUtils.dp2px(6f).toFloat())
        transform.setExceptCorner(false, false, true, true)
        val url = "http://pic1.win4000.com/wallpaper/2019-11-08/5dc4e5acc79a7_270_185.jpg"
        holder.transform(R.id.iv_cover, url, transform)
                .setText(R.id.tv_title, "和田玉佛")
                .setText(R.id.tv_price, SpanUtils()
                        .append("当前 ")
                        .append("¥").setFontSize(9, true)
                        .append("45").setFontSize(16, true).setBold()
                        .create())
    }

}
