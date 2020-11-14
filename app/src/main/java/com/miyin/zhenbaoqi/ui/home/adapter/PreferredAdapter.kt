package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class PreferredAdapter(list: List<String>) :BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_preferred

    override fun convert(holder: BaseViewHolder, data: String?) {
        val shopTransform = CornerTransform(mContext, DensityUtils.dp2px(3f).toFloat())
        val goodsTransform = CornerTransform(mContext, DensityUtils.dp2px(5f).toFloat())
        val url = "http://01.minipic.eastday.com/20170213/20170213165739_a6f7877b5e3417de31114a54a4f36264_4.jpeg"
        holder.transform(R.id.iv_cover, url, shopTransform)
                .setText(R.id.tv_title, "捞面和田玉工作室")
                .setText(R.id.tv_desc, "质保金：20800")
                .setText(R.id.tv_attention_count, "100人关注")
                .transform(R.id.iv_left, url, goodsTransform)
                .setText(R.id.tv_left_title, "产品项目新上...")
                .setText(R.id.tv_left_price, SpanUtils()
                        .append("¥").setFontSize(11, true)
                        .append("128")
                        .create())
                .transform(R.id.iv_middle, url, goodsTransform)
                .setText(R.id.tv_middle_title, "产品项目新上...")
                .setText(R.id.tv_middle_price, SpanUtils()
                        .append("¥").setFontSize(11, true)
                        .append("128")
                        .create())
                .transform(R.id.iv_right, url, goodsTransform)
                .setText(R.id.tv_right_title, "产品项目新上...")
                .setText(R.id.tv_right_price, SpanUtils()
                        .append("¥").setFontSize(11, true)
                        .append("128")
                        .create())
                .setText(R.id.tv_goods_count, "共160件宝贝，快去看看")
    }

}
