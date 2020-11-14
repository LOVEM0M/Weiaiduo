package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.SearchShopBean
import com.miyin.zhenbaoqi.ext.loadImg

class SearchShopAdapter(list: List<SearchShopBean.DataBean>) : BaseAdapter<SearchShopBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_search_shop

    override fun convert(holder: BaseViewHolder, data: SearchShopBean.DataBean) {
        with(data) {
            holder.loadImg(R.id.iv_avatar, head_img)
                    .setText(R.id.tv_title, merchants_name)
                    .setText(R.id.tv_fans_count, "关注数：$focus_size")
                    .setText(R.id.tv_desc, merchants_subtitle)
        }
    }

}
