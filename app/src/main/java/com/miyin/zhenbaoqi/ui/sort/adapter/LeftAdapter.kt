package com.miyin.zhenbaoqi.ui.sort.adapter

import android.graphics.Typeface
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ext.setSelect

class LeftAdapter(list: List<CityBean.CityListBean>, private val isShowLine: Boolean = true) : BaseAdapter<CityBean.CityListBean>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_title

    override fun convert(holder: BaseViewHolder, data: CityBean.CityListBean) {
        val position = holder.adapterPosition
        with(data) {
            holder.setText(R.id.tv_title, code_name)
                    .setSelect(R.id.tv_title, mPosition == position)
                    .setVisible(R.id.view_line, mPosition == position && isShowLine)
                    .setTypeface(R.id.tv_title, if (mPosition == position) Typeface.defaultFromStyle(Typeface.BOLD) else Typeface.defaultFromStyle(Typeface.NORMAL))
                    .addOnClickListener(R.id.tv_title)
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

    fun getPosition() = mPosition

}
