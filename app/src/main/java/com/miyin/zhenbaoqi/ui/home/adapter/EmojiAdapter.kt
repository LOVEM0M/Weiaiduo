package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.tencent.qcloud.tim.uikit.component.face.Emoji

class EmojiAdapter(list: List<Emoji>) : BaseAdapter<Emoji>(list) {

    override fun getContentView() = R.layout.item_emoji

    override fun convert(holder: BaseViewHolder, data: Emoji) {
        holder.setImageBitmap(R.id.iv_cover, data.icon)
    }

}
