package com.miyin.zhenbaoqi.bean

class CheckBlackListBean : ResponseBean() {

    var data: DataBean? = null

    class DataBean {
        var check_status = false
    }

}