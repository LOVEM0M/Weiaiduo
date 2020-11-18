package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.FirstCategorySecondBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class FirstCategorySecondAdapter(data: List<FirstCategorySecondBean.DataBean>) : BaseAdapter<FirstCategorySecondBean.DataBean>(data) {


    override fun getContentView() = R.layout.item_snacks_tab
    override fun convert(holder: BaseViewHolder, data: FirstCategorySecondBean.DataBean?) {
        with(data) {
            val position = holder.adapterPosition
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            holder.itemView.layoutParams = params
            val transform = RoundCornersTransform(DensityUtils.dp2px(8f).toFloat(), RoundCornersTransform.CornerType.TOP)
            val url = when {
                this?.codeValue.isNullOrEmpty() -> ""
              this?.codeValue!!.contains(",") -> codeValue!!.split(",")[0]
                else -> codeValue
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, this?.codeName)


        }
    }


}
