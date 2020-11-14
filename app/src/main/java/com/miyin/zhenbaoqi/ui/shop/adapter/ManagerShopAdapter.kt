package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.ext.loadImg
import java.text.DecimalFormat

class ManagerShopAdapter(list: List<MerchantGoodsBean.ListBean>, private val state: Int) : BaseAdapter<MerchantGoodsBean.ListBean>(list) {

    private val mDecimalFormat = DecimalFormat("0.00")

    override fun getContentView() = R.layout.item_manager_shop

    override fun convert(holder: BaseViewHolder, data: MerchantGoodsBean.ListBean) {
        with(data) {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            holder.loadImg(R.id.iv_cover, goodsImg)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, "¥ " + mDecimalFormat.format(goods_amount / 100f))
                    .setVisible(R.id.tv_sticky, true)
                    .setVisible(R.id.tv_take_off, true)
                    .setVisible(R.id.tv_relaunch, false)
                    .setVisible(R.id.tv_delete, false)
                    .addOnClickListener(R.id.tv_sticky)
                    .addOnClickListener(R.id.tv_take_off)
                    .addOnClickListener(R.id.tv_relaunch)
                    .addOnClickListener(R.id.tv_delete)
                    .addOnClickListener(R.id.fl_container)

            when (state) {
                2 -> {
                    holder.setVisible(R.id.tv_take_off, true)
                            .setVisible(R.id.tv_sticky, true)
                            .setVisible(R.id.tv_relaunch, false)
                            .setVisible(R.id.tv_delete, false)
                            .setText(R.id.tv_sticky, "取消置顶")
                }
                0 -> {
                    holder.setVisible(R.id.tv_take_off, true)
                            .setVisible(R.id.tv_sticky, true)
                            .setVisible(R.id.tv_relaunch, false)
                            .setVisible(R.id.tv_delete, false)
                            .setText(R.id.tv_sticky, "置顶首页")
                }
                1 -> {
                    holder.setVisible(R.id.tv_take_off, false)
                            .setVisible(R.id.tv_sticky, false)
                            .setVisible(R.id.tv_relaunch, true)
                            .setVisible(R.id.tv_delete, true)
                }
                else -> {
                }
            }
        }
    }

}
