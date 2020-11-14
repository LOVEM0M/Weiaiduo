package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class SubAccountInfoBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean : Serializable {
        var id = 0
        var user_id = 0
        var merchants_id = 0
        var type = 0
        var account_name: String? = null
        var passward: String? = null
        var only_key: String? = null
        var create_time: String? = null
    }

}
