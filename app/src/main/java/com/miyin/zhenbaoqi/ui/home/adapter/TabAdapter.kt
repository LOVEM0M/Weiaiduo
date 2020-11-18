package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.bean.takeThreeVipBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform


class TabAdapter(data: List<VipFirstFreegoodsBean.DataBeanX.DataBean>) : BaseAdapter<VipFirstFreegoodsBean.DataBeanX.DataBean>(data) {
    override fun getContentView() = R.layout.item_home_classify2
    override fun convert(holder: BaseViewHolder, data: VipFirstFreegoodsBean.DataBeanX.DataBean?) {
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
                    .setText(R.id.tv_goods_name, this?.goodsName)
                    .setText(R.id.tv_price, "￥"+this?.goodsAmount)
                    .setText(R.id.tv_price1, "返现￥"+this?.goodsAmount+">")

        }
    }


}
