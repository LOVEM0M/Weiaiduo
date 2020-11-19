package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CityBean

class CityAdapter(list: List<CityBean.DataBean>) : BaseAdapter<CityBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_city

    override fun convert(holder: BaseViewHolder, data: CityBean.DataBean) {
        with(data) {
            holder.setText(R.id.tv_title, codeName)
        }
    }

}