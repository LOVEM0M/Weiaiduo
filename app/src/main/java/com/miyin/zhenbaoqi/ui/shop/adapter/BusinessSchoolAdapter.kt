package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.CollegeBean
import com.miyin.zhenbaoqi.ext.loadImg

class BusinessSchoolAdapter(list: List<CollegeBean.ListBean>) : BaseAdapter<CollegeBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_business_school

    override fun convert(holder: BaseViewHolder, data: CollegeBean.ListBean) {
        with(data) {
            holder.loadImg(R.id.iv_cover, arti_picture)
                    .setText(R.id.tv_title, arti_name)
                    .setText(R.id.tv_desc, arti_profile)
        }
    }

}