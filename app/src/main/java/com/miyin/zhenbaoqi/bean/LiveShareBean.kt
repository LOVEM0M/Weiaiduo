package com.miyin.zhenbaoqi.bean

class LiveShareBean : PagerBean() {

    val list: List<LiveShareListBean>? = null

    data class LiveShareListBean(
            val room_id: Int,
            val user_id: Int,
            val merchants_id: Int,
            val room_name: String,
            val room_desc: Any,
            val icon_url: String,
            val play_url: String,
            val face_url: String,
            val popularity: Int,
            val share_no: Int,
            val fans_no: Int,
            val is_focus: Boolean,
            val user_grade:UserGradeDataBean
    )
    data class UserGradeDataBean(
            val user_id: Int,
            val point:Int,
            val grade_name:String,
            val privilege:Int,
            val is_birthday:Int,
            val is_free:Int,
            val icon:String
    )

}
