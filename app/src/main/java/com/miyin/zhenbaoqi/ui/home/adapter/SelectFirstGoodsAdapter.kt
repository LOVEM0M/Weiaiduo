package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.RestoreBean
import com.miyin.zhenbaoqi.bean.SelectFirstGoodsBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class SelectFirstGoodsAdapter(data: List<SelectFirstGoodsBean.ListBean>) : BaseAdapter<SelectFirstGoodsBean.ListBean>(data) {


    override fun getContentView() = R.layout.item_home_classify4
    override fun convert(holder: BaseViewHolder, data: SelectFirstGoodsBean.ListBean?) {
        with(data) {
            val position = holder.adapterPosition
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            holder.itemView.layoutParams = params
            val transform = RoundCornersTransform(DensityUtils.dp2px(8f).toFloat(), RoundCornersTransform.CornerType.TOP)
            val url = when {
                this?.goods_img.isNullOrEmpty() -> ""
              this?.goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, this?.goods_describe)
                    .setText(R.id.tv_praise, "好评率")

        }
    }


}
