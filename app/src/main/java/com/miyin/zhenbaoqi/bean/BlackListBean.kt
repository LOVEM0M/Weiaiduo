package com.miyin.zhenbaoqi.bean

class BlackListBean : PagerBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var create_time: String? = null
        var head_img: String? = null
        var merchants_id = 0
        var nick_name: String? = null
        var user_id = 0
    }

}
