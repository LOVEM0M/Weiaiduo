package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class LuckyBagDetailAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_lucky_bag_detail

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.loadImg(R.id.iv_avatar, "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4198784213,3117807869&fm=26&gp=0.jpg")
                    .setText(R.id.tv_name, "用户的昵称")
                    .setText(R.id.tv_date_time, "12-11 15:20")
                    .setText(R.id.tv_price, "1.5元")
                    .setText(R.id.tv_status, "成功绑定专粉")
        }
    }

}
