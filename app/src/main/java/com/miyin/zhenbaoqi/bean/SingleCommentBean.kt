package com.miyin.zhenbaoqi.bean

class SingleCommentBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var id = 0
        var user_id = 0
        var reply_id = 0
        var reply_type = 0
        var reply_time: String? = null
        var likes = 0
        var reply_content: String? = null
        var state = 0
        var head_img: String? = null
        var nick_name: String? = null
    }

}
