package com.miyin.zhenbaoqi.bean

class BankBean : ResponseBean() {

    var list: List<BankListBean>? = null

    class BankListBean {
        var bank_id = 0
        var bank_name: String? = null
    }

}