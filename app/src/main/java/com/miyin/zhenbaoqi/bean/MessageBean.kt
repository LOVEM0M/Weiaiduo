package com.miyin.zhenbaoqi.bean

class MessageBean : PagerBean() {

    var change_message: String? = null
    var change_message_size: Int = 0
    var courier_message: String? = null
    var courier_message_size: Int = 0
    var system_message: String? = null
    var system_message_size: Int = 0
    var list: List<ListBean>? = null

    class ListBean {
        var add_time: String? = null
        var message_name: String? = null
        var message_value: String? = null
    }

}
