package com.miyin.zhenbaoqi.bean

class AuctionGoodsRecordBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var auction_bid_amount = 0L
        var head_img: String? = null
        var nick_name: String? = null
        var state = 0
        var update_time: String? = null
        var user_id = 0
        var order_number: String? = null
    }

}
