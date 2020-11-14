package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CheckBlackListBean
import com.miyin.zhenbaoqi.bean.SingleCommentBean
import com.miyin.zhenbaoqi.bean.VideoDetailBean
import com.miyin.zhenbaoqi.bean.VideoReplyBean

class VideoContract {

    interface IView : IBaseView {
        fun getVideoDetailSuccess(bean: VideoDetailBean)

        fun checkBlackListSuccess(bean: CheckBlackListBean)

        fun getReplyListSuccess(bean: VideoReplyBean)

        fun addReplySuccess(bean: SingleCommentBean)

        fun getChildReplyListSuccess(bean: VideoReplyBean)

        fun addChildReplySuccess(bean: SingleCommentBean)

        fun updateLikesSuccess(flag: Int)

        fun updateMerchantStateSuccess(focusState: Int)
    }

    interface IPresenter :IBasePresenter<IView> {
        fun getVideoDetail(videoId: Int, merchantId: Int)

        fun getReplyList(videoId: Int, currentPage: Int, pageSize: Int)

        fun addReply(replyId: Int, replyContent: String)

        fun getChildReplyList(replyId: Int, currentPage: Int, pageSize: Int)

        fun addChildReply(replyId: Int, replyContent: String)

        fun addLikes(likeId: Int)

        fun deleteLike(likeId: Int)

        fun updateMerchantState(merchantId: Int, focusState: Int)
    }

}