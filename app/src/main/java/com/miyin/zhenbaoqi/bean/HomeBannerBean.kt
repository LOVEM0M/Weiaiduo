package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class HomeBannerBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean : Serializable {
        var bannerId = 0
        var url: String? = null
        var title: String? = null
        var photo: String? = null
        var createDate: String? = null
        var updateDate: String? = null
        var status = 0
        var type = 0
        var jumpId = 0
    }

}