package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.BillBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.TimeUtils

class WalletAdapter(list: List<BillBean.BillListBean>) : BaseAdapter<BillBean.BillListBean>(list) {

    override fun getContentView() = R.layout.item_wallet

    override fun convert(holder: BaseViewHolder, data: BillBean.BillListBean) {
        with(data) {
            holder.loadImg(R.id.iv_cover, head_img)
                    .setText(R.id.tv_name, nick_name)
//                    .setText(R.id.tv_desc, "$change_name ${FormatUtils.formatNumber(change_amount / 100f)}")
                    .setText(R.id.tv_time, TimeUtils.millis2String(asset_time!!.toLong(), "MM-dd HH:mm"))
                    .setText(R.id.tv_price, "${if (asset_type == 1) "+" else "-"}${FormatUtils.formatNumber(change_amount / 100f)}")

            when (state) {
                0 -> {
                    holder.setText(R.id.tv_title, change_name)
//                            .setTextColor(R.id.tv_title, 0xFFFE3C3C.toInt())
                            .setTextColor(R.id.tv_price, 0xFFFE3C3C.toInt())
                }
                1 -> {
                    holder.setText(R.id.tv_title, change_name)
//                            .setTextColor(R.id.tv_title, 0xFF2D9F3A.toInt())
                            .setTextColor(R.id.tv_price, 0xFF080808.toInt())
                }
                2 -> {
                    holder.setText(R.id.tv_title, change_name)
//                            .setTextColor(R.id.tv_title, 0xFFFE3C3C.toInt())
                            .setTextColor(R.id.tv_price, 0xFF080808.toInt())
                }
                3 -> {
                    holder.setTextColor(R.id.tv_price, 0xFF080808.toInt())
                }
                else -> {
                    holder.setTextColor(R.id.tv_price, 0xFF080808.toInt())
                }
            }
        }
    }

}
