package com.miyin.zhenbaoqi.bean

class OrderEvalListBean : ResponseBean() {

    var goods_img: String? = null
    var goods_name: String? = null
    var head_img: String? = null
    var merchants_name: String? = null
    var merchants_subtitle: String? = null
    var order_amount: Int = 0
    var pay_number = 0
    var dicts: List<DictBean>? = null

    class DictBean {
        var code_name: String? = null
        var dict_id: Int = 0
    }

}
