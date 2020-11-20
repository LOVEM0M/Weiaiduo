package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class GoodsDetailBean : ResponseBean(), Serializable {

    var data: DataBean? = null

    class DataBean {
        var goodsId = 0//商品id
        var goodsName: String? = null//商品名称
        var goodsAmount = 0//商品普通价格
        var goodsImg: String? = null//商品多图滚动逗号分隔
        var state = 0//0正常1已下架2已删除
        var inventory = 0//库存
        var goodsSales = 0//销售数量
        var cateId1 = 0//一级类目
        var cateId2 = 0//二级类目
        var goodsDescribe: String? = null//商品描述
        var goodsOriginalAmount = 0//商品原价价格
        var goodsFreight = 0//邮费
        var goodsType = 0//1普通商品
        var isRestriction = 0//是否限购0是1否 限购的一个用户只能购买一次
        var isSeven = 0//是否7天退款 0是1否
        var addTime: String? = null//添加时间
        var publishTime: String? = null//发布时间
        var tags: String? = null//标签
        var image: String? = null//单个小图
        var goodsVipAmount = 0//vip价格
        var isRestore = 0//0不是返还商品 1是返还商品
        var isHot = 0//是否热门推荐0是1否
        var content: String? = null
        var cartNumber: Any? = null
    }


   /* class DataBean : Serializable {

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

    }*/

}