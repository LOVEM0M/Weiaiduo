package com.miyin.zhenbaoqi.bean

import java.io.Serializable

open class PagerBean : ResponseBean(), Serializable {

    var current_page = 0
    var page_size = 0
    var pages = 0
    var total_record = 0

}
