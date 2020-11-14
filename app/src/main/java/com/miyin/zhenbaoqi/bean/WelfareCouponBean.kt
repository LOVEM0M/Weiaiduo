package com.miyin.zhenbaoqi.bean

class WelfareCouponBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var coupons_amount = 0
        var goods_ids: String? = null
        var goods_type = 0
        var id = 0
        var order_min_amount = 0
        var remark: String? = null
        var type = 0
        var usable_day = 0
        var usable_scope: String? = null
    }

}