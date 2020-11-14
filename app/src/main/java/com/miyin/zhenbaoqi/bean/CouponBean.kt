package com.miyin.zhenbaoqi.bean

class CouponBean : ResponseBean() {

    var current_page: Int = 0
    var page_size: Int = 0
    var pages: Int = 0
    var total_record: Int = 0
    var list: List<ListBean>? = null

    class ListBean {
        var coupons_amount: Long = 0
        var coupons_id: Int = 0
        var coupons_name: String? = null
        var goods_type: Int = 0
        var order_min_amount: Long = 0
        var use_end_date: String? = null
        var coupons_descr: String? = null
    }

}
