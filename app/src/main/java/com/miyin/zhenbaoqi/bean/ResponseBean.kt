package com.miyin.zhenbaoqi.bean

import java.io.Serializable

open class ResponseBean : Serializable {

    var code = 0//状态
    var msg: String? = null//消息

}
