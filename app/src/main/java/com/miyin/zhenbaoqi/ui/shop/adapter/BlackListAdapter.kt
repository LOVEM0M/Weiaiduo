package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.BlackListBean
import com.miyin.zhenbaoqi.ext.loadImg

class BlackListAdapter(list: List<BlackListBean.DataBean>) : BaseAdapter<BlackListBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_black_list

    override fun convert(holder: BaseViewHolder, data: BlackListBean.DataBean) {
        with(data) {
            holder.loadImg(R.id.iv_avatar, head_img)
                    .setText(R.id.tv_title, nick_name)
                    .setText(R.id.tv_date_time, create_time)
                    .addOnClickListener(R.id.tv_operate)
        }
    }

}
