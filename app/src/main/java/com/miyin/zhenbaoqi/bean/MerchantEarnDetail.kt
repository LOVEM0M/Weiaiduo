package com.miyin.zhenbaoqi.bean

class MerchantEarnDetail : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var asset_type: Int = 0
        var be_invite_id: Int = 0
        var change_amount: Int = 0
        var change_name: String? = null
        var create_time: String? = null
        var id: Int = 0
        var invite_id: Int = 0
        var order_number: String? = null
        var state: Int = 0
        var type: Int = 0
    }

}
