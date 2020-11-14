package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class MerchantInfoBean : ResponseBean(), Serializable {

    var data: DataBean? = null

    class DataBean : Serializable {
        var describe_grade: String? = null
        var focus_number: Int = 0
        var focus_state: Int = 0
        var head_img: String? = null
        var is_update_name: Int = 0
        var logistics_grade: String? = null
        var merchants_back: String? = null
        var merchants_grade: String? = null
        var merchants_name: String? = null
        var merchants_subtitle: String? = null
        var quality_retention_money: Int = 0
        var service_grade: String? = null
        var fans_count = 0
        var live_room: LiveRoomBean? = null

        class LiveRoomBean : Serializable {
            var end_time: String? = null
            var face_url: String? = null
            var fans_no = 0
            var icon_url: String? = null
            var merchants_cat = 0
            var merchants_id = 0
            var play_url: String? = null
            var popularity = 0
            var push_url: String? = null
            var room_desc: String? = null
            var room_id = 0
            var room_name: String? = null
            var share_no = 0
            var start_time: String? = null
            var status = 0
            var user_id = 0
        }
    }

}
