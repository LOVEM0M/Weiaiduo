package com.miyin.zhenbaoqi.ui.shop.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MaterialBean
import com.miyin.zhenbaoqi.ext.loadImg

class MaterialAdapter(list: List<MaterialBean.DataBean>) : BaseAdapter<MaterialBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_material

    override fun convert(holder: BaseViewHolder, data: MaterialBean.DataBean) {
        with(data) {
            val goodsImg = when {
                material_img.isNullOrEmpty() -> ""
                material_img!!.contains(",") -> material_img!!.split(",")[0]
                else -> material_img
            }
            holder.loadImg(R.id.iv_avatar, R.drawable.ic_app_logo)
                    .setText(R.id.tv_name, auther)
                    .setText(R.id.tv_time, update_time)
                    .setText(R.id.tv_title, Html.fromHtml(content))
                    .loadImg(R.id.iv_cover, goodsImg)
                    .addOnClickListener(R.id.iv_cover)
                    .addOnClickListener(R.id.tv_forward)
                    .addOnClickListener(R.id.tv_save_photo)
                    .addOnClickListener(R.id.tv_copy)
        }
    }

}
