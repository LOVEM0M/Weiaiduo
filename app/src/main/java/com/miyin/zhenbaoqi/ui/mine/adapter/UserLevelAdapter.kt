package com.miyin.zhenbaoqi.ui.mine.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.UserLevelBean
import com.miyin.zhenbaoqi.ext.loadImg

class UserLevelAdapter(list: List<UserLevelBean.DataBean>) : BaseAdapter<UserLevelBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_user_level

    override fun convert(holder: BaseViewHolder, data: UserLevelBean.DataBean) {
        val position = holder.adapterPosition
        if (position % 2 == 0) {
            holder.setBackgroundColor(R.id.ll_container, ContextCompat.getColor(mContext, R.color.white))
        } else {
            holder.setBackgroundColor(R.id.ll_container, ContextCompat.getColor(mContext, R.color.bg_f5))
        }

        with(data) {
            holder.loadImg(R.id.iv_cover, icon)
                    .setText(R.id.tv_name, name)
                    .setText(R.id.tv_integral, "$min_point~$max_point")
        }
    }

}
