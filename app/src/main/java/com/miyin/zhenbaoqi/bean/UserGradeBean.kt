package com.miyin.zhenbaoqi.bean

class UserGradeBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var icon: String? = null
        var grade_name: String? = null
        var grade_type = 0
        var is_birthday = 0
        var is_free = 0
        var point = 0
        var privilege = 0
        var user_id = 0
    }

}
