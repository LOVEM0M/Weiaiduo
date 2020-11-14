package com.miyin.zhenbaoqi.bean

class MerchantGoodsStoreBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var add_amount = 0L
        var add_time: String? = null
        var auction_bid_amount = 0
        var auction_state = 0
        var bid_size = 0
        var cate_id1 = 0
        var cate_id2 = 0
        var cate_id3 = 0
        var collection_state = 0
        var commission_ratio = 0
        var end_time: String? = null
        var ensure_amount = 0
        var goods_amount = 0L
        var goods_describe: String? = null
        var goods_freight = 0
        var goods_id = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var goods_original_amount = 0L
        var goods_sales = 0
        var goods_type = 0
        var goods_video: String? = null
        var inventory = 0
        var is_hot = 0
        var is_new = 0
        var is_restriction = 0
        var is_seven = 0
        var is_top = 0
        var is_top_time: String? = null
        var merchants_id = 0
        var service_time = 0L
        var start_amount = 0L
        var start_time: String? = null
        var state = 0
        var user_id = 0
    }

}