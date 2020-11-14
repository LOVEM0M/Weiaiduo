package com.miyin.zhenbaoqi.bean

class UserLevelBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var grade_type = 0
        var icon: String? = null
        var id = 0
        var is_birthday = 0
        var is_free = 0
        var max_point = 0
        var min_point = 0
        var name: String? = null
        var privilege = 0
    }

}
