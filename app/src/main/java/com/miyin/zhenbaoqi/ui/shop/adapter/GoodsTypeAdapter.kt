package com.miyin.zhenbaoqi.ui.shop.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ext.setSelect

class GoodsTypeAdapter(list: List<CityBean.CityListBean>, private val type: Int) : BaseAdapter<CityBean.CityListBean>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_goods_type

    override fun convert(holder: BaseViewHolder, data: CityBean.CityListBean) {
        val position = holder.adapterPosition
        if (type == 0) {
            holder.setText(R.id.tv_title, data.code_name)
                    .setSelect(R.id.tv_title, mPosition == position)
                    .setSelect(R.id.view_line, mPosition == position)
                    .setVisible(R.id.iv_cover, false)
                    .addOnClickListener(R.id.tv_title)
        } else {
            holder.setText(R.id.tv_title, data.code_name)
                    .setTextColor(R.id.tv_title, ContextCompat.getColor(mContext, if (mPosition == position) R.color.theme_dark_purple else R.color.text_54))
                    .setBackgroundColor(R.id.tv_title, Color.WHITE)
                    .setVisible(R.id.iv_cover, mPosition == position)
                    .setVisible(R.id.view_line, false)
                    .setVisible(R.id.view, false)
                    .addOnClickListener(R.id.tv_title)
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

}
