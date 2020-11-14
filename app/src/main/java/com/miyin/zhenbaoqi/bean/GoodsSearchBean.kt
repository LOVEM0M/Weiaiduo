package com.miyin.zhenbaoqi.bean

class GoodsSearchBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var goods_amount = 0L
        var goods_id: Int = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var goods_type: Int = 0
        var goods_video: String? = null
    }

}
