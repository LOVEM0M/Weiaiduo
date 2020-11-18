package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class LoginBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean : Serializable {
        var userId = 0
        var phoneNo: String? = null
        var nickName: String? = null
        var headImg: String? = null
        var registerDate: String? = null
        var balance  = 0.0
        var token: String? = null
        var vipType = 0
        var vipTime: String? = null

    }

}
