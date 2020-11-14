package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.ArticleBean

class HelpCenterAdapter(list: List<ArticleBean.ArticleListBean>) : BaseAdapter<ArticleBean.ArticleListBean>(list) {

    override fun getContentView() = R.layout.item_help_center

    override fun convert(holder: BaseViewHolder, data: ArticleBean.ArticleListBean) {
        holder.setText(R.id.tv_title, data.arti_name)
    }

}
