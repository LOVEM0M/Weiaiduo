package com.miyin.zhenbaoqi.ui.home.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.VideoCommentFirstBean
import com.miyin.zhenbaoqi.bean.VideoCommentSecondBean
import com.miyin.zhenbaoqi.bean.VideoCommentThirdBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.tencent.qcloud.tim.uikit.component.face.FaceManager

class CommentAdapter(list: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list) {

    companion object {
        const val TYPE_FIRST = 0
        const val TYPE_SECOND = 1
        const val TYPE_THIRD = 2
    }

    init {
        addItemType(TYPE_FIRST, R.layout.item_commnet)
        addItemType(TYPE_SECOND, R.layout.item_commnet_second)
        addItemType(TYPE_THIRD, R.layout.item_commnet_expand)
    }

    override fun convert(holder: BaseViewHolder, data: MultiItemEntity?) {
        when (holder.itemViewType) {
            TYPE_FIRST -> {
                val firstBean = data as VideoCommentFirstBean
                holder.loadImg(R.id.iv_cover, firstBean.headImg)
                        .setText(R.id.tv_name, firstBean.nickName)

                val time = TimeUtils.getFriendlyTime(firstBean.replyTime)
                FaceManager.handlerEmojiText(holder.getView(R.id.tv_content), "", firstBean.comment, time, false)
            }
            TYPE_SECOND -> {
                val secondBean = data as VideoCommentSecondBean
                holder.loadImg(R.id.iv_cover, secondBean.headImg)
                        .setText(R.id.tv_name, secondBean.nickName)

                val time = TimeUtils.getFriendlyTime(secondBean.replyTime)
                FaceManager.handlerEmojiText(holder.getView(R.id.tv_content), secondBean.parentNickName, secondBean.comment, time, false)
            }
            TYPE_THIRD -> {
                val thirdBean = data as VideoCommentThirdBean
                holder.setText(R.id.tv_title, thirdBean.desc)
            }
        }
    }

}
