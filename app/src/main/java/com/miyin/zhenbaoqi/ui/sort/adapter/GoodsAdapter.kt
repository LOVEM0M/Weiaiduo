package com.miyin.zhenbaoqi.ui.sort.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.NewGoodsBean
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class GoodsAdapter(list: List<NewGoodsBean.ListBean>, private val tag: Int = 1) : BaseAdapter<NewGoodsBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_goods

    override fun convert(holder: BaseViewHolder, data: NewGoodsBean.ListBean) {
        val transform = CornerTransform(mContext, DensityUtils.dp2px(6f).toFloat())
        transform.setExceptCorner(false, false, true, true)

        val position = holder.adapterPosition

        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position % 2 == tag) {
            params.rightMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
        } else {
            params.leftMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
        }
        holder.itemView.layoutParams = params

        with(data) {
            val goodsImg = if (goods_img!!.contains(",")) goods_img!!.split(",")[0] else goods_img
            holder.transform(R.id.iv_cover, goodsImg, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, SpanUtils()
                            .append("当前  ¥")
                            .append(FormatUtils.formatNumber(goods_amount  )).setFontSize(17, true)
                            .create())
                    .setText(R.id.tv_count, SpanUtils()
                            .append("已出价 ")
                            .append("5").setForegroundColor(0xFFEA2F30.toInt())
                            .append(" 次")
                            .create())
        }
    }

}
