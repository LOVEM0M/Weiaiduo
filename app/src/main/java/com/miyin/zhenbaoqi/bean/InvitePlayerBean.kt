package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class InvitePlayerBean : PagerBean(), Serializable {

    var list: List<DataBean>? = null

    class DataBean : Serializable {
        var nickName: String? = null
        var userId: String? = null
        var payAmount = 0
        var headImg: String? = null
        var count = 0
        var registerTime: String? = null
    }

}
