package com.miyin.zhenbaoqi.bean

class HomeGoodsHotBean : PagerBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var add_amount: Int = 0
        var add_time: String? = null
        var auction_state: Int = 0
        var cate_id1: Int = 0
        var cate_id2: Int = 0
        var cate_id3: Int = 0
        var commission_ratio: Int = 0
        var delay_time: Int = 0
        var end_time: String? = null
        var ensure_amount: Int = 0
        var goods_amount = 0L
        var goods_describe: String? = null
        var goods_freight: Int = 0
        var goods_id: Int = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var goods_original_amount: Int = 0
        var goods_sales: Int = 0
        var goods_type: Int = 0
        var goods_video: String? = null
        var inventory: Int = 0
        var is_hot: Int = 0
        var is_new: Int = 0
        var is_restriction: Int = 0
        var is_seven: Int = 0
        var is_top: Int = 0
        var is_top_time: String? = null
        var merchants_id: Int = 0
        var pay_type: Int = 0
        var start_amount: Int = 0
        var start_time: String? = null
        var state: Int = 0
        var istype = 0
        var roomm_id = 0
    }

}
