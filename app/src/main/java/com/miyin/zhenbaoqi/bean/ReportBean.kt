package com.miyin.zhenbaoqi.bean

class ReportBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var access_count = 0
        var access_number = 0
        var back_amount = 0
        var back_number = 0
        var good_num = 0
        var group_fans = 0
        var hete_amount = 0
        var hete_number = 0
        var pay_amount = 0
        var pay_number = 0
        var put_amount = 0
        var ratio: String? = null
        var refunding_amount = 0
        var refunding_number = 0
        var total_fans = 0
        var wait_amount = 0
        var wait_number = 0
        var waitconf_amount = 0
        var waitconf_number = 0
    }

}