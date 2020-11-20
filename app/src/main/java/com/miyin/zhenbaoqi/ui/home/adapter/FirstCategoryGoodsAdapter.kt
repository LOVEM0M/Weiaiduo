package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.FirstCategoryGoodsBean
import com.miyin.zhenbaoqi.bean.FirstCategorySecondBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SPUtils

class FirstCategoryGoodsAdapter(data: List<FirstCategoryGoodsBean.DataBeanX.DataBean>) : BaseAdapter<FirstCategoryGoodsBean.DataBeanX.DataBean>(data) {

    override fun getContentView() = R.layout.item_home_classify4
    override fun convert(holder: BaseViewHolder, data: FirstCategoryGoodsBean.DataBeanX.DataBean?) {
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
            var vipType = SPUtils.getInt("vipType")
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, this?.goodsName)
                    .setText(R.id.tv_praise, "好评率")
            if(vipType==0||vipType==1){
                holder.setText(R.id.tv_price,"￥"+this?.goodsAmount)
            }
            else {
                holder.setText(R.id.tv_price, "￥" + this?.goodsVipAmount)
            }

        }
    }


}
