package com.miyin.zhenbaoqi.bean

class UpdateAppBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var code = 0
        var create_time: String? = null
        var name: String? = null
        var new_code = 0
        var remark: String? = null
        var status = 0
        var update_time: String? = null
        var url: String? = null
    }

}