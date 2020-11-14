package com.miyin.zhenbaoqi.bean

class LiveHotBean : PagerBean() {

    val list: List<LiveHotDataBean>? = null

    data class LiveHotDataBean(
            val merchants_id: Int,
            val face_url: String,
            val icon_url: String,
            val play_url: String,
            val room_name: String,
            val popularity: Int,
            val room_id: Int,
            val user_id: Int,
            val fans_no: Int,
            val is_focus: Boolean
    )
}