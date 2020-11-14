package com.miyin.zhenbaoqi.ui.home.adapter

import android.graphics.Color
import android.graphics.Typeface
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ext.setBackground
import com.miyin.zhenbaoqi.ext.setSelect


class TopAdapter(list: List<CityBean.CityListBean>, private val isShowLine: Boolean = true) : BaseAdapter<CityBean.CityListBean>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_home_classify3

    override fun convert(holder: BaseViewHolder, data: CityBean.CityListBean) {
        val position = holder.adapterPosition
        with(data) {
            holder.setText(R.id.tv_title, code_name)
                    .setSelect(R.id.tv_title, mPosition == position)
            if (mPosition == position){
                holder.setBackgroundRes(R.id.tv_title,R.drawable.shape_home_bg7)
                        .setTextColor(R.id.tv_title,Color.WHITE)
            }
            else
                holder.setBackgroundRes(R.id.tv_title,R.drawable.shape_home_bg14)
                        .setTextColor(R.id.tv_title,Color.BLACK)
            holder .addOnClickListener(R.id.tv_title)
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

    fun getPosition() = mPosition

}
