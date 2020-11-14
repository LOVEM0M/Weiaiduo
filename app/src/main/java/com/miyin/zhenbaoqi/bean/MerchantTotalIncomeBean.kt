package com.miyin.zhenbaoqi.bean

class MerchantTotalIncomeBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var totalAmount = 0L
        var waitAmount = 0L
        var withdrawAmount = 0L
    }

}
