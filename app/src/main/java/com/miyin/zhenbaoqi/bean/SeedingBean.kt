package  com.miyin.zhenbaoqi.bean

import java.io.Serializable

class SeedingBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var id = 0
        var images: String? = null
        var title: String? = null
        var create_time: String? = null
        var goods_id = 0
        var goods_name: String? = null
        var state = 0
        var content: String? = null
        var goods: GoodsBean ? = null
       class GoodsBean : Serializable{
           var goods_id = 0
           var goods_name: String? = null
           var goods_img: String? = null
           var goods_amount = 0
           var state = 0
           var goods_sales = 0//销售数量
           var inventory = 0
           var merchants_id = 0
           var cate_id1 = 0
           var cate_id2 = 0
           var cate_id3 = 0
           var goods_describe: String? = null//商品介绍
           var goods_video: String? = null
           var goods_original_amount = 0
           var commission_ratio = 0
           var goods_freight = 0
           var goods_type  = 0
           var is_restriction = 0
           var is_seven  = 0
           var end_time: String? = null
           var end_time_timestamp = 0L
           var is_top = 0
           var is_hot = 0
           var is_sale = 0
           var is_top_time: String? = null
           var add_time: String? = null
           var start_amount = 0
           var start_time: String? = null
           var start_time_timestamp = 0L
           var add_amount = 0
           var ensure_amount = 0
           var auction_state = 0
           var is_new = 0//是否新品
           var collection_state= 0
           var user_id = 0
           var service_time = 0L
           var bid_size = 0
           var auction_bid_amount = 0
           var is_quality = 0
           var istype = 0
           var room_id = 0
           var tags: String? = null


           var measure: String? = null
           var texture: String? = null
           var place: String? = null
           var weight: String? = null
        }
    }
}