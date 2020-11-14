package com.miyin.zhenbaoqi.bean

class SearchBean : ResponseBean() {

    var total_serachs: List<TotalSearchBean>? = null
    var user_serachs: List<UserSearchBean>? = null

    class TotalSearchBean {
        var search_param: String? = null
    }

    class UserSearchBean {
        var search_param: String? = null
    }

}
