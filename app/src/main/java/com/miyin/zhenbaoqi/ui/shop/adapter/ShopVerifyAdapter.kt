package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class ShopVerifyAdapter(list: List<String>, private val title: String?) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_shop_verify

    override fun convert(holder: BaseViewHolder, data: String?) {
        val transform = CornerTransform(mContext, DensityUtils.dp2px(3f).toFloat())
        val url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3443713359,3306682691&fm=26&gp=0.jpg"
        with(data) {
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, "捞面和田玉工作室")
                    .setText(R.id.tv_desc, "副标题信息啊")
                    .loadImg(R.id.iv_avatar, url)
                    .setText(R.id.tv_name, "张三（邀请码245464）")

            when (title) {
                "待核实" -> {
                    holder.setVisible(R.id.fl_container, true)
                            .setGone(R.id.tv_date_time, false)
                }
                else -> {
                    holder.setGone(R.id.fl_container, false)
                            .setVisible(R.id.tv_date_time, true)
                            .setText(R.id.tv_date_time, "核实时间：2019-12-10 15:20:10")
                }
            }
        }
    }

}
