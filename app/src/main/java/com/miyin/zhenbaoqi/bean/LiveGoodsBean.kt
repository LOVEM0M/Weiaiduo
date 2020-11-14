package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class LiveGoodsBean : PagerBean() {

    val list: List<LiveGoodsDataBean>? = null

    data class LiveGoodsDataBean(
            val add_amount: Int,
            val add_time: String,
            val auction_bid_amount: Int,
            val auction_state: Int,
            val bid_size: Int,
            val cate_id1: Any,
            val cate_id2: Any,
            val cate_id3: Any,
            val collection_state: Int,
            val commission_ratio: Int,
            val end_time: String,
            val end_time_timestamp: Long,
            val ensure_amount: Int,
            val goods_amount: Long,
            val goods_describe: String,
            val goods_freight: Int,
            val goods_id: Int,
            val goods_img: String,
            val goods_name: String,
            val goods_original_amount: Long,
            val goods_sales: Int,
            val goods_type: Int,
            val goods_video: String,
            val inventory: Int,
            val is_hot: Int,
            val is_new: Int,
            val is_quality: Int,
            val is_restriction: Int,
            val is_sale: Int,
            val is_seven: Int,
            val is_top: Int,
            val is_top_time: String,
            val istype: Int,
            val merchants_id: Int,
            val room_id: Int,
            val service_time: Long,
            val start_amount: Int,
            val start_time: String,
            val start_time_timestamp: Long,
            val state: Int,
            val tags: String,
            val user_id: Int
    ) : Serializable

}