package com.miyin.zhenbaoqi.bean

class MaterialBean : PagerBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var auther: String? = null
        var content: String? = null
        var id = 0
        var material_img: String? = null
        var post_time: String? = null
        var state = 0
        var title: String? = null
        var update_time: String? = null
    }

}