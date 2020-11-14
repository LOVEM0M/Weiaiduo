package com.miyin.zhenbaoqi.bean

class MerchantShopDataBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var today_order_num = 0
        var today_refund_num = 0
        var today_wait_pay_num = 0
        var today_wait_send_num = 0
        var yes_total_amount = 0
        var yes_total_visitor_num = 0
    }

}
