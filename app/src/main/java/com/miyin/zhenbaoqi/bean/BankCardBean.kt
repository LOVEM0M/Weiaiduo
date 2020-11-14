package com.miyin.zhenbaoqi.bean

import java.io.Serializable

class BankCardBean : ResponseBean(), Serializable {

    var list: List<ListBean>? = null

    class ListBean : Serializable {
        var bank_branch: String? = null
        var bank_card_num: String? = null
        var bank_id = 0
        var bank_img: String? = null
        var bank_logo: String? = null
        var bank_name: String? = null
        var user_bank_id = 0
    }

}