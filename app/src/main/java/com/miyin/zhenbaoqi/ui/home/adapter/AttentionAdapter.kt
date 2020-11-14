package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.HomeMerchantBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils

class AttentionAdapter(list: List<HomeMerchantBean.DataBean>) : BaseAdapter<HomeMerchantBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_attention

    override fun convert(holder: BaseViewHolder, data: HomeMerchantBean.DataBean) {
        with(data) {
            val focusSize = if (focus_size >= 10000) {
                FormatUtils.formatNumber(focus_size / 1000f) + "万"
            } else {
                focus_size.toString()
            }
            holder.loadImg(R.id.iv_cover, head_img)
                    .setText(R.id.tv_title, merchants_name)
                    .setText(R.id.tv_attention_count, "${focusSize}人关注")
                    .setGone(R.id.tv_attention, is_focus == 0)
                    .setGone(R.id.tv_see, is_focus == 1)
                    .addOnClickListener(R.id.tv_attention)
                    .addOnClickListener(R.id.tv_see)
        }
    }

}
