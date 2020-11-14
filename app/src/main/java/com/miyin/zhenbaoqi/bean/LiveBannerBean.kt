package com.miyin.zhenbaoqi.bean

class LiveBannerBean : ResponseBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var banner_id = 0
        var img_url: String? = null
        var jump_url: String? = null
        var order_dec = 0
        var play_url: String? = null
        var title: String? = null
    }

}
