package com.miyin.zhenbaoqi.ui.live.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class LiveGoodsAdapter(list: List<LiveGoodsBean.LiveGoodsDataBean>, private val title: String?) : BaseAdapter<LiveGoodsBean.LiveGoodsDataBean>(list) {

    override fun getContentView() = R.layout.item_live_goods

    override fun convert(holder: BaseViewHolder, data: LiveGoodsBean.LiveGoodsDataBean) {
        val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.LEFT)

        with(data) {
            val goodsImg = when {
                goods_img.isEmpty() -> ""
                goods_img.contains(",") -> goods_img.split(",")[0]
                else -> goods_img
            }
            holder.transform(R.id.iv_cover, goodsImg, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_shop_price, "¥${FormatUtils.formatNumber(goods_amount / 100f)}")
                    .addOnClickListener(R.id.iv_cover)
                    .addOnClickListener(R.id.tv_delete)
            if (title == "编辑中的宝贝") {
                holder.addOnClickListener(R.id.cl_container)
            }

            when (title) {
                "秒杀中" -> {
                    holder.setVisible(R.id.tv_add_price, false)
                            .setVisible(R.id.tv_price_add, true)
                            .setVisible(R.id.tv_delete, false)
                            .setText(R.id.tv_price_add, "库存：${inventory} 件")
                            .setText(R.id.tv_operate, "立即购买")
                            .addOnClickListener(R.id.tv_operate)
                }
                "拍卖中" -> {
                    holder.setVisible(R.id.tv_add_price, true)
                            .setVisible(R.id.tv_price_add, true)
                            .setVisible(R.id.tv_delete, false)
                            .setText(R.id.tv_price_add, "参考价：¥${FormatUtils.formatNumber(goods_original_amount / 100f)}\u3000加¥${FormatUtils.formatNumber(add_amount / 100f)}")
                            .setText(R.id.tv_add_price, "点击加价 ${FormatUtils.formatNumber(add_amount / 100f)}")
                            .setText(R.id.tv_operate, "加一手")
                            .addOnClickListener(R.id.tv_operate)
                }
                "全部宝贝", "编辑中的宝贝" -> {
                    /*
                    * state '0正常1已下架2已删除',
                    */
                    when (state) {
                        0 -> {
                            holder.setVisible(R.id.tv_add_price, false)
                                    .setVisible(R.id.tv_price_add, true)
                                    .setVisible(R.id.tv_operate, true)
                                    .setText(R.id.tv_operate, "下架")
                                    .addOnClickListener(R.id.tv_operate)
                            if (goods_type == 3) {
                                holder.setText(R.id.tv_price_add, "参考价：¥${FormatUtils.formatNumber(goods_original_amount / 100f)}\u3000加¥${FormatUtils.formatNumber(add_amount / 100f)}")
                                        .setVisible(R.id.tv_delete, false)
                            } else {
                                holder.setText(R.id.tv_price_add, "库存：${inventory} 件")
                                        .setVisible(R.id.tv_delete, false)
                            }
                        }
                        1 -> {
                            holder.setVisible(R.id.tv_add_price, false)
                                    .setVisible(R.id.tv_price_add, true)
                                    .setVisible(R.id.tv_delete, true)
                                    .setVisible(R.id.tv_operate, true)
                                    .setText(R.id.tv_operate, "上架")
                                    .addOnClickListener(R.id.tv_operate)
                            if (goods_type == 3) {
                                holder.setText(R.id.tv_price_add, "参考价：¥${FormatUtils.formatNumber(goods_original_amount / 100f)}\u3000加¥${FormatUtils.formatNumber(add_amount / 100f)}")
                            } else {
                                holder.setText(R.id.tv_price_add, "库存：${inventory} 件")
                            }
                        }
                        2 -> {
                            holder.setVisible(R.id.tv_add_price, false)
                                    .setVisible(R.id.tv_price_add, true)
                                    .setVisible(R.id.tv_delete, false)
                                    .setVisible(R.id.tv_operate, false)
                            if (goods_type == 3) {
                                holder.setText(R.id.tv_price_add, "参考价：¥${FormatUtils.formatNumber(goods_original_amount / 100f)}\u3000加¥${FormatUtils.formatNumber(add_amount / 100f)}")
                            } else {
                                holder.setText(R.id.tv_price_add, "库存：${inventory} 件")
                            }
                        }
                        else -> {

                        }
                    }
                }
                else -> {
                }
            }
        }
    }

}
