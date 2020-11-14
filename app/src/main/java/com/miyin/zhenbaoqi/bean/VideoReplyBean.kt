package com.miyin.zhenbaoqi.bean

class VideoReplyBean : PagerBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var head_img: String? = null
        var id: Int = 0
        var likes: Int = 0
        var nick_name: String? = null
        var reply_content: String? = null
        var reply_id: Int = 0
        var reply_time = 0L
        var user_id: Int = 0
        var replys: List<ReplyBean>? = null

        class ReplyBean {
            var head_img: String? = null
            var id: Int = 0
            var likes: Int = 0
            var nick_name: String? = null
            var reply_content: String? = null
            var reply_id: Int = 0
            var reply_time = 0L
            var user_id: Int = 0
        }
    }

}
