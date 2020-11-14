package com.miyin.zhenbaoqi.bean

class BillBean : PagerBean() {

    val list: List<BillListBean>? = null

    class BillListBean {
        var asset_time: String? = null
        var asset_type = 1
        var change_amount = 0
        var change_name: String? = null
        var change_number: String? = null
        var change_type = 0
        var head_img: String? = null
        var nick_name: String? = null
        var state = 0
    }

}
