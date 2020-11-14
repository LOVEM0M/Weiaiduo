package com.miyin.zhenbaoqi.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.miyin.zhenbaoqi.ui.home.adapter.CommentAdapter
import java.io.Serializable

class VideoCommentFirstBean : MultiItemEntity, Serializable {

    var id = 0
    var comment: String? = null
    var headImg: String? = null
    var nickName: String? = null
    var replyTime = 0L

    override fun getItemType() = CommentAdapter.TYPE_FIRST

}

class VideoCommentSecondBean : MultiItemEntity, Serializable {

    var id = 0
    var comment: String? = null
    var headImg: String? = null
    var nickName: String? = null
    var parentNickName: String? = null
    var replyTime = 0L

    override fun getItemType() = CommentAdapter.TYPE_SECOND

}

class VideoCommentThirdBean : MultiItemEntity, Serializable {

    var desc: String? = null

    override fun getItemType(): Int {
        return CommentAdapter.TYPE_THIRD
    }

}