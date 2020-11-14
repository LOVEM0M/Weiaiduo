package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg

class SearchVideoAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_search_video

    override fun convert(holder: BaseViewHolder, data: String) {
        with(data) {
            holder.loadImg(R.id.iv_cover, "")
        }
    }

}
