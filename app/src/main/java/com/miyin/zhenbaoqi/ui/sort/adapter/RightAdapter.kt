package com.miyin.zhenbaoqi.ui.sort.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.CommodityBean
import com.miyin.zhenbaoqi.ext.loadImg

class RightAdapter(data: List<CommodityBean>) : BaseMultiItemQuickAdapter<CommodityBean, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.item_commodity_title)
        addItemType(1, R.layout.item_commodity)
    }

    override fun convert(holder: BaseViewHolder, data: CommodityBean) {
        when (data.itemType) {
            0 -> holder.setText(R.id.tv_title, data.title)
            1 -> holder.setText(R.id.tv_title, data.title)
                    .loadImg(R.id.iv_cover, data.cover)
        }
    }

}
