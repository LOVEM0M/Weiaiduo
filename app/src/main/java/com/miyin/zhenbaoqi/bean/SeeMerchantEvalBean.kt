package com.miyin.zhenbaoqi.bean

class SeeMerchantEvalBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var evaluation_label: String? = null
        var label_size: Int = 0
    }

}
