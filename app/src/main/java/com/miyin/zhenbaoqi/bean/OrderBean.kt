package com.miyin.zhenbaoqi.bean

class OrderBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var courier_amount: Int = 0
        var goods_id: Int = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var order_amount: Int = 0
        var order_number: String? = null
        var order_time: String? = null
        var pay_amount: Int = 0
        var state: Int = 0
        var remark: String? = null
        var pay_number = 0
    }

}
