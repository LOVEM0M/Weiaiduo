package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class ArticleBean : PagerBean() {

    var list: List<ArticleListBean>? = null

    class ArticleListBean : Serializable {
        var arti_name: String? = null
        var arti_picture: String? = null
        var arti_profile: String? = null
        var mod_date: String? = null
        var url: String? = null
        var activity_id = 0
        var is_apply = false
    }

}