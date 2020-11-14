package com.miyin.zhenbaoqi.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.miyin.zhenbaoqi.sql.FootprintEntity

class FootprintBean : MultiItemEntity {

    var type = 0
    var data: String? = null
    var bean: FootprintEntity? = null
    var isSelect = false

    override fun getItemType() = type
}