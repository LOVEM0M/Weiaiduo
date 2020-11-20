package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.SecondCategoryGoodsBean
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class SecondCategoryGoodsAdapter(data: List<SecondCategoryGoodsBean.DataBeanX.DataBean>) : BaseAdapter<SecondCategoryGoodsBean.DataBeanX.DataBean>(data) {


    override fun getContentView() = R.layout.item_snacks
    override fun convert(holder: BaseViewHolder, data: SecondCategoryGoodsBean.DataBeanX.DataBean?) {//bind??
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
                this?.goodsImg.isNullOrEmpty() -> ""
              this?.goodsImg!!.contains(",") -> goodsImg!!.split(",")[0]
                else -> goodsImg
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, this?.goodsName)
                    .setText(R.id.tv_price, "￥"+ this!!.goodsAmount)
            holder.addOnClickListener(R.id.tv_black_point)//点击显示弹窗，但是怎么点击？？
        }
    }


}
