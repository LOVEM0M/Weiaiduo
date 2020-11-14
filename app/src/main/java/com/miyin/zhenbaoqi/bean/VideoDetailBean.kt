package com.miyin.zhenbaoqi.bean

class VideoDetailBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var video_id = 0
        var merchants_id = 0
        var file_id: String? = null
        var cover_url: String? = null
        var media_url: String? = null
        var video_describe: String? = null
        var duration = 0
        var size = 0
        var container: String? = null
        var width = 0
        var height = 0
        var md5: String? = null
        var type = 0
        var state = 0
        var reply_amount = 0
        var like_amount = 0
        var post_time: String? = null
        var update_time: String? = null
        var merchants_name: String? = null
        var head_img: String? = null
        var focus_state = 0
        var fans_no = 0
        var likes_state = 0
    }

}