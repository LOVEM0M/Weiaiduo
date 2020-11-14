package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class GoodsDetailBean : ResponseBean(), Serializable {

    var data: DataBean? = null

    class DataBean : Serializable {

        var goods: GoodsBean? = null
        var merchants: MerchantsBean? = null

        class GoodsBean : Serializable {
            var add_amount = 0L
            var add_time: String? = null
            var auction_state = 0
            var cate_id1 = 0
            var cate_id2 = 0
            var cate_id3 = 0
            var collection_state = 0
            var commission_ratio = 0
            var end_time: String? = null
            var end_time_timestamp = 0L
            var ensure_amount = 0L
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
            var start_amount = 0L
            var start_time: String? = null
            var start_time_timestamp = 0L
            var state: Int = 0
            var user_id = 0
            var service_time = 0L
            var is_quality = 0
            var measure: String? = null
            var place: String? = null
            var texture: String? = null
            var weight: String? = null
            var is_live = "0"
        }

        class MerchantsBean : Serializable {
            var add_time: String? = null
            var address: String? = null
            var city_id = 0
            var consignee: String? = null
            var contact_phone: String? = null
            var county_id = 0
            var describe_grade: String? = null
            var evaluation_size = 0
            var focus_size = 0
            var head_img: String? = null
            var identity_images: String? = null
            var identity_type = 0
            var is_focus = 0
            var is_update_name = 0
            var licence_image: String? = null
            var logistics_grade: String? = null
            var main_cate = 0
            var merchants_back: String? = null
            var merchants_grade: String? = null
            var merchants_id = 0
            var merchants_name: String? = null
            var merchants_subtitle: String? = null
            var opt_status: String? = null
            var pcc_name: String? = null
            var phone_no: String? = null
            var province_id = 0
            var quality_balance = 0L
            var service_grade: String? = null
            var state = 0
            var status = 0
            var wechat_id: String? = null
            var user_id = 0
        }

    }

}