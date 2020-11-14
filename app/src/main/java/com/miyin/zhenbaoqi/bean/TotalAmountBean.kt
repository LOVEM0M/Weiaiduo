package com.miyin.zhenbaoqi.bean

class TotalAmountBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var id: Int = 0
        var user_id: Int = 0
        var merchants_id: Int = 0
        var earn_balance: Int = 0
        var payment_balance: Int = 0
        var quality_balance: Int = 0
    }

}
