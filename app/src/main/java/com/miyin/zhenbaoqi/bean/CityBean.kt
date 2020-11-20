package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class CityBean : ResponseBean(), Serializable {

    var data: List<DataBean>? = null

    class DataBean : Serializable {
        var dictId = 0
        var codeType: Any? = null
        var parentId: Any? = null
        var codeName: String? = null
        var dictSort: Any? = null
        var status: Any? = null
        var codeValue: String? = null
        var remark: Any? = null
        var extend: Any? = null
    }

}
