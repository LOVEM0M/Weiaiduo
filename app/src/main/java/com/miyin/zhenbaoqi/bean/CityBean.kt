package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class CityBean : ResponseBean(), Serializable {

    var dicts: List<CityListBean>? = null

    class CityListBean : Serializable {
        var code_name: String? = null
        var code_value: String? = null
        var dict_id = 0
        var parent_id = 0
    }

}
