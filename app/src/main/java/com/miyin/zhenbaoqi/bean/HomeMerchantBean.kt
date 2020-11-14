package com.miyin.zhenbaoqi.bean

class HomeMerchantBean : ResponseBean() {

    var data: List<DataBean>? = null

    class DataBean {
        var add_time: String? = null
        var address: String? = null
        var city_id: Int = 0
        var consignee: String? = null
        var contact_phone: String? = null
        var county_id: Int = 0
        var focus_size: Int = 0
        var head_img: String? = null
        var identity_images: String? = null
        var identity_type: Int = 0
        var is_focus: Int = 0
        var licence_image: String? = null
        var main_cate: Int = 0
        var merchants_back: String? = null
        var merchants_id: Int = 0
        var merchants_name: String? = null
        var merchants_subtitle: String? = null
        var opt_status: String? = null
        var pcc_name: String? = null
        var phone_no: String? = null
        var province_id: Int = 0
        var state: Int = 0
        var status: Int = 0
        var wechat_id: String? = null
    }

}
