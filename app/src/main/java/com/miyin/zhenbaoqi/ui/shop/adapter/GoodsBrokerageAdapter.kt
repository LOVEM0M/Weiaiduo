package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class GoodsBrokerageAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_goods_brokerage

    override fun convert(holder: BaseViewHolder, data: String?) {
        val url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577196628448&di=21e22de1980bcd847505a4f0edc11543&imgtype=0&src=http%3A%2F%2Fimg.daimg.com%2Fuploads%2Fallimg%2F181011%2F3-1Q011224015.jpg"
        with(data) {
            holder.setText(R.id.tv_date_time, "2019-12-12 16:13:36")
                    .setText(R.id.tv_status, "待入账")
                    .loadImg(R.id.iv_cover, url)
                    .setText(R.id.tv_title, "【翡翠成品一条龙服务】2019-12-11分享榜第一名礼品")
                    .setText(R.id.tv_price, "¥2.00")
                    .setText(R.id.tv_count, "数量 x1")
                    .loadImg(R.id.iv_avatar, url)
                    .setText(R.id.tv_name, "玉茗堂客服")
                    .setText(R.id.tv_profit, "赚 ¥2.00")
        }
    }

}
