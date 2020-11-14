package com.miyin.zhenbaoqi.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HomeVideoBean : PagerBean(), Serializable {

    var data: List<DataBean>? = null

    class DataBean : Serializable {
        var container: String? = null
        var cover_url: String? = null
        var duration: Int = 0
        var file_id: String? = null
        var height: String? = null
        @SerializedName("video_id")
        var id: Int = 0
        var like_amount: Int = 0
        var md5: String? = null
        var media_url: String? = null
        var merchants_id: Int = 0
        var post_time: String? = null
        var reply_amount: Int = 0
        var size: Int = 0
        var state: Int = 0
        var type: Int = 0
        var update_time: String? = null
        var video_describe: String? = null
        var weight: String? = null
    }

}
