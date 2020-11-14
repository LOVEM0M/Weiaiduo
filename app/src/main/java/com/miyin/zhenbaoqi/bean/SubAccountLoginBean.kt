package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class SubAccountLoginBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean : Serializable {
        var merchantsSub: MerchantsSubBean? = null
        var room: RoomBean? = null

        class MerchantsSubBean : Serializable {
            var id = 0
            var sub_id: String? = null
            var user_id = 0
            var merchants_id = 0
            var type = 0
            var account_name: String? = null
            var passward: String? = null
            var only_key: String? = null
            var create_time: String? = null
            var token: String? = null
            var head_img: String? = null
            var phone_no: String? = null
        }

        class RoomBean : Serializable {
            var room_id = 0
            var user_id = 0
            var merchants_id = 0
            var room_name: String? = null
            var room_desc: String? = null
            var icon_url: String? = null
            var play_url: String? = null
            var face_url: String? = null
            var popularity = 0
            var fans_no = 0
            var is_focus = false
        }
    }

}
