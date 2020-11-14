package com.miyin.zhenbaoqi.bean

class MerchantEvalBean : ResponseBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var evaluation_label: String? = null
        var label_size = 0
    }

}