package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class InviteRewardAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_invite_reward

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.setText(R.id.tv_title, "一只小猴子")
                    .setText(R.id.tv_phone, "19568954578")
                    .setText(R.id.tv_date_time, "2019-12-12  14:13:65")
        }
    }

}
