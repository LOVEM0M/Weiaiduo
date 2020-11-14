package com.miyin.zhenbaoqi.bean

class MerchantGoodsBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var goods_amount = 0L
        var goods_id: Int = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var is_top: Int = 0
    }

}
