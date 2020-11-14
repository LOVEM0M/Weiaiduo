package com.miyin.zhenbaoqi.ui.live.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveChatBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.setBackground
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.noober.background.drawable.DrawableCreator

class ChatAdapter(list: List<LiveChatBean>) : BaseAdapter<LiveChatBean>(list) {

    override fun getContentView() = R.layout.item_chat

    override fun convert(holder: BaseViewHolder, data: LiveChatBean) {
        with(data) {
            when (data.type) {
                "room" -> {
                    holder.loadImg(R.id.img_level, levelImg)
                            .setText(R.id.tv_title, SpanUtils()
                                    .appendSpace(mContext.getDimension(R.dimen.dp_44).toInt())
                                    .append("${data.nickName}").setForegroundColor(0xFFC5813F.toInt())
                                    .append("进入房间").setForegroundColor(0xffffffff.toInt())
                                    .create())
                }
                "tip" -> {
                    holder.setGone(R.id.img_level, false)
                            .setText(R.id.tv_title, content)
                }
                else -> {
                    holder.loadImg(R.id.img_level, levelImg)
                            .setText(R.id.tv_title, SpanUtils()
                                    .appendSpace(mContext.getDimension(R.dimen.dp_44).toInt())
                                    .append("${data.nickName}: ").setForegroundColor(0xFFC5813F.toInt())
                                    .append(data.content!!)
                                    .create())
                }
            }

        }
    }

}
