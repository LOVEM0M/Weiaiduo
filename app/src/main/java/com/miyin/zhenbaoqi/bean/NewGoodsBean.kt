package com.miyin.zhenbaoqi.bean

class NewGoodsBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var goods_id = 0
        var goods_name: String? = null
        var goods_img: String? = null
        var goods_amount = 0L
        var goods_type = 0
        var goods_video: String? = null
    }

}
