package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class MerchantCoinAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_merchain_coin

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            holder.loadImg(R.id.iv_cover, "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3742196187,1219651313&fm=26&gp=0.jpg")
                    .setText(R.id.tv_title, "免费的卖家名额")
                    .setText(R.id.tv_desc, "5000")
                    .addOnClickListener(R.id.tv_exchange)
        }
    }

}
