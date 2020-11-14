package com.miyin.zhenbaoqi.bean

class BindOfferBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var auction_bid_amount = 0
        var bid_id = 0
        var bid_no: String? = null
        var ensure_amount = 0
        var ensure_state = 0
        var goods_id = 0
        var head_img: String? = null
        var icon: String? = null
        var nick_name: String? = null
        var order_number: String? = null
        var pay_type = 0
        var return_status = 0
        var state = 0
        var update_time: String? = null
    }

}