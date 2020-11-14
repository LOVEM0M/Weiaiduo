package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.ext.loadImg

class BusinessAdapter(list: List<ArticleBean.ArticleListBean>) : BaseAdapter<ArticleBean.ArticleListBean>(list) {

    override fun getContentView() = R.layout.item_business

    override fun convert(holder: BaseViewHolder, data: ArticleBean.ArticleListBean) {
        with(data) {
            holder.loadImg(R.id.iv_cover, arti_picture)
                    .setText(R.id.tv_title, arti_name)
        }
    }

}
