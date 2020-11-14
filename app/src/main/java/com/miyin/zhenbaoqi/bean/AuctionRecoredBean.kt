package com.miyin.zhenbaoqi.bean

class AuctionRecoredBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var auction_bid_amount: Int = 0
        var auction_state: Int = 0
        var goods_id: Int = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var state: Int = 0
    }

}
