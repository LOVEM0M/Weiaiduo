package com.miyin.zhenbaoqi.bean

class NoticeBean : PagerBean() {

    var list: List<ListBean>? = null

    class ListBean {
        var arti_name: String? = null
        var arti_picture: String? = null
        var arti_profile: String? = null
        var mod_date: String? = null
        var url: String? = null
    }

}
