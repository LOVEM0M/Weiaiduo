package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class VideoBean : PagerBean(), Serializable {

    var data: List<DataBean>? = null

    class DataBean : Serializable {
        var id: Int = 0
        var like_amount: Int = 0
        var merchants_id: Int = 0
        var post_time: String? = null
        var reply_amount: Int = 0
        var state: Int = 0
        var type: Int = 0
        var update_time: String? = null
        var url: String? = null
        var video_describe: String? = null
    }

}
