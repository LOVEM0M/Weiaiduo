package com.miyin.zhenbaoqi.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class AuctionOptionMultiBean : MultiItemEntity {

    var type = 0
    var title: String? = null
    var parent_id = 0
    var belong = 0

    override fun getItemType() = type

}