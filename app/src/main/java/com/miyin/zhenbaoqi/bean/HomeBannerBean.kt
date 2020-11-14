package com.miyin.zhenbaoqi.bean

class HomeBannerBean : ResponseBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var banner_Id = 0
        var create_Date: String? = null
        var goods_Id: String? = null
        var photo: String? = null
        var title: String? = null
        var update_Date: String? = null
        var url: String? = null
    }

}