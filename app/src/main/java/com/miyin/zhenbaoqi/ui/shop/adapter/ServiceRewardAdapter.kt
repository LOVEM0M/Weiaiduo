package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class ServiceRewardAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_service_reward

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.loadImg(R.id.iv_avatar, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577189223486&di=7bfdac3f2e276426f5c66393510c23d3&imgtype=0&src=http%3A%2F%2Fimg01.imgcdc.com%2Ffun%2Fzh_cn%2Fstar%2Fnews%2F11052670%2F20160317%2F2016031709115074874200.jpg")
                    .setText(R.id.tv_title, "一只小猴子")
                    .setText(R.id.tv_phone, "19568954578（高级玩家）")
                    .setText(R.id.tv_price, "0.00")
                    .setText(R.id.tv_count, "1人")
        }
    }

}
