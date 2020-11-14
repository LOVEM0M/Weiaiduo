package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class SelectFirstGoodsBean : PagerBean() {
    var list: List<ListBean>? = null

    class ListBean : Serializable {
        var add_amount = 0
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
        var end_time_timestamp = 0L
        var ensure_amount = 0
        var goods_amount = 0
        var goods_describe: String? = null
        var goods_freight = 0
        var goods_id = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var goods_original_amount = 0
        var goods_sales = 0
        var goods_type = 0
        var goods_video: String? = null
        var inventory = 0
        var is_hot = 0
        var is_new = 0
        var is_quality = 0
        var is_restriction = 0
        var is_sale = 0
        var is_seven = 0
        var is_top = 0
        var is_top_time: String? = null
        var istype = 0
        var measure: String? = null
        var merchants_id = 0
        var place: String? = null
        var room_id = 0
        var service_time = 0L
        var start_amount = 0
        var start_time: String? = null
        var start_time_timestamp = 0L
        var state = 0
        var tags: String? = null
        var texture: String? = null
        var user_id = 0
        var weight: String? = null
    }
}