package com.miyin.zhenbaoqi.bean


class CustomMessageBean {

    companion object {
        const val TYPE_GOODS_INFO = 1
    }

    var type = TYPE_GOODS_INFO
    var data: String? = null

}