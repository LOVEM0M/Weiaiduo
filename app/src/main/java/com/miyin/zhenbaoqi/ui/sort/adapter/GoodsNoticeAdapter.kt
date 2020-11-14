package com.miyin.zhenbaoqi.ui.sort.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter

class GoodsNoticeAdapter(list: List<String>) : BaseAdapter<String>(list) {

    private val mDrawableList = listOf(R.drawable.ic_goods_notice_first, R.drawable.ic_goods_notice_second, R.drawable.ic_goods_notice_third, R.drawable.ic_goods_notice_firth)
    private val mTitleList = listOf("真品保障", "关于快递", "关于色差", "关于尺寸")
    private val mDescList = listOf("唯爱多平台所售产品均要求商户如实描述。所有通过鉴宝服务的产品均可接受全国复检，并且支持7天无理由退换货，实现客户权益最大化。",
            "唯爱多平台与国内主要快递机构均有密切合作。发货期间，会根据客户地址实际情况，选择时间成本最低的合作商，进行发货，保证商品急速送达。",
            "因手机、电脑等显示设备的色彩偏差和个人对颜色理解等不同，导致实物可能与照片存在细微色差，请您以收到的实物为准。",
            "商品详情页货品尺码仅供参考，由于供货商尺码标准、产品款型及测量方法不同,  商品实际尺寸、重量可能会存在些许偏差，均属于正常情况。")

    override fun getContentView() = R.layout.item_goods_notice

    override fun convert(holder: BaseViewHolder, data: String) {
        val position = holder.adapterPosition

        holder.setBackgroundRes(R.id.iv_cover, mDrawableList[position])
                .setText(R.id.tv_title, mTitleList[position])
                .setText(R.id.tv_desc, mDescList[position])
    }

}