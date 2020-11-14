package com.miyin.zhenbaoqi.bean

class ServiceAmountBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var original_price: String? = null
        var special_price: String? = null
    }

}