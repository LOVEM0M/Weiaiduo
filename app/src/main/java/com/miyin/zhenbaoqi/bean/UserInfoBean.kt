package com.miyin.zhenbaoqi.bean

class UserInfoBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var balance: Int = 0
        var coupon_number: Int = 0
        var gold: Int = 0
        var point: Int = 0
        var waitPayNumber = 0
        var waitSendNumber = 0
        var waitReceivingNumber = 0
        var waitAfterNumber = 0
    }

}
