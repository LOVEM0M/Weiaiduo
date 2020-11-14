package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class ReferrerBean : ResponseBean() {

    val data: DataBean? = null

    class DataBean : Serializable {
        var wechat_id: String? = null
        var merchants_id = 0
        var user_id = 0
        var nick_name: String? = null
        var state = 0
        var wechat_image: String? = null
        var merchants_name: String? = null
    }

}