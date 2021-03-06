package com.miyin.zhenbaoqi.bean

class BannerCategoryBean : ResponseBean() {
    var data: List<DataBean>? = null

    class DataBean {
        var dictId = 0
        var codeType: String? = null
        var parentId = 0
        var codeName: String? = null
        var dictSort = 0
        var status = 0
        var codeValue: String? = null
        var remark: String? = null
        var extend: String? = null
    }

}