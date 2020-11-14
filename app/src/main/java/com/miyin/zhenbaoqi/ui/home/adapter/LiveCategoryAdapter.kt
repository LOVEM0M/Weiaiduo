package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.setSelect

class LiveCategoryAdapter(list: List<String>) :BaseAdapter<String>(list) {

    private var mPosition = 0

    override fun getContentView() = R.layout.item_live_category

    override fun convert(holder: BaseViewHolder, data: String?) {
        val position = holder.adapterPosition
        holder.setText(R.id.tv_title, data)
                .setSelect(R.id.tv_title, position == mPosition)
                .addOnClickListener(R.id.tv_title)

        /*val tvTitle = holder.getView<TextView>(R.id.tv_title)
        if (position == 0) {
            tvTitle.run {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_heart, 0, 0, 0)
                setPadding(mContext.getDimensionPixelSize(R.dimen.dp_15), 0, 0, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
        } else if (position == 1) {
            tvTitle.run {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_hot, 0, 0, 0)
                setPadding(mContext.getDimensionPixelSize(R.dimen.dp_15), 0, 0, 0)
                gravity = Gravity.CENTER_VERTICAL
            }
        }*/
    }

    fun setPosition(position: Int) {
        mPosition = position
        notifyDataSetChanged()
    }

}