package com.miyin.zhenbaoqi.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class CommodityBean(private var type: Int = 0, var title: String = "", var cover: Any? = null,
                         var dictId: Int, var index: Int = 0, var parentId: Int = 0) : MultiItemEntity {

    override fun getItemType() = type
}
