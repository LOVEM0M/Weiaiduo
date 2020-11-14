package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class LiveRoomBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean : Serializable {

        var face_url: String? = null
        var icon_url: String? = null
        var merchants_id = 0
        var play_url: String? = null
        var popularity = 0
        var room_id = 0
        var room_name: String? = null
        var user_id = 0
        var fans_no = 0
        var is_focus: Boolean = false

    }

}