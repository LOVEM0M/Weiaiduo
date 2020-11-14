package com.miyin.zhenbaoqi.bean

class CollectBean : PagerBean() {

    var list: List<CollectListBean>? = null

    class CollectListBean {
        var goods_amount = 0L
        var goods_id = 0
        var goods_img: String? = null
        var goods_name: String? = null
        var state = 0
    }

}
