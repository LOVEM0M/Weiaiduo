package com.miyin.zhenbaoqi.bean

class PayResultBean : ResponseBean() {

    var alipayBody: String? = null

    var noncestr: String? = null
    var order_number: String? = null
    var partnerid: String? = null
    var prepayid: String? = null
    var sign: String? = null
    var timestamp: String? = null
    var package_value: String? = null

}
