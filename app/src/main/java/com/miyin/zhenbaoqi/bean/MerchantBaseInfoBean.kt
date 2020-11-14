package com.miyin.zhenbaoqi.bean

class MerchantBaseInfoBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var amount: Int = 0
        var play_number: Int = 0
        var fans_number: Int = 0
        var waitSendNumber = 0
        var waitReceivingNumber = 0
        var successNumber = 0
        var waitAfterNumber = 0
    }

}
