package com.miyin.zhenbaoqi.bean

class ShopAttentionBean : PagerBean() {

    var list: List<ShopListBean>? = null

    class ShopListBean {

        var head_img: String? = null
        var merchants_id: Int = 0
        var merchants_name: String? = null
        var merchants_subtitle: String? = null
        var state: Int = 0
        var list: List<ListBean>? = null

        class ListBean {
            var goods_amount = 0L
            var goods_id: Int = 0
            var goods_img: String? = null
            var goods_name: String? = null
        }

    }

}
