package com.miyin.zhenbaoqi.ui.mine.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.OrderBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.setBackground
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.noober.background.drawable.DrawableCreator

class OrderAdapter(list: List<OrderBean.ListBean>) : BaseAdapter<OrderBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_order

    override fun convert(holder: BaseViewHolder, data: OrderBean.ListBean) {
        val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL)

        with(data) {
            holder.setText(R.id.tv_time, order_time)
                    .transform(R.id.iv_cover, if (goods_img!!.contains(",")) goods_img!!.split(",")[0] else goods_img, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_desc, "容量:230ml")
                    .setText(R.id.tv_count, "x$pay_number")
                    .setText(R.id.tv_price, "¥${FormatUtils.formatNumber(order_amount / 100f)}")
                    .setText(R.id.tv_total_price, "共${pay_number}件商品 合计：¥${FormatUtils.formatNumber(pay_amount / 100f)}(含运费 ¥${FormatUtils.formatNumber(courier_amount / 100f)})")
                    .addOnClickListener(R.id.tv_left_title)
                    .addOnClickListener(R.id.tv_middle_title)
                    .addOnClickListener(R.id.tv_right_title)

            when (state) {
                1 -> { // 待支付
                    holder.setText(R.id.tv_status, "等待买家付款")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "立即支付")
                            .setTextColor(R.id.tv_right_title, ContextCompat.getColor(mContext, R.color.theme_dark_purple))
                            .setBackground(R.id.tv_right_title, getStrokeSelect())
                }
                2 -> { // 待发货
                    holder.setText(R.id.tv_status, "待发货")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "提醒发货")
                }
                3 -> { // 已发货
                    holder.setText(R.id.tv_status, "待收货")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, true)
                            .setText(R.id.tv_middle_title, "查看物流")
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "确认收货")
                            .setTextColor(R.id.tv_right_title, ContextCompat.getColor(mContext, R.color.theme_dark_purple))
                            .setBackground(R.id.tv_right_title, getSolidSelect())
                }
                4 -> { // 已签收
                    holder.setText(R.id.tv_status, "待评价")
                            .setVisible(R.id.tv_left_title, true)
                            .setText(R.id.tv_left_title, "查看物流")
                            .setVisible(R.id.tv_middle_title, true)
                            .setText(R.id.tv_middle_title, "申请售后")
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "评价")
                            .setTextColor(R.id.tv_right_title, ContextCompat.getColor(mContext, R.color.theme_dark_purple))
                            .setBackground(R.id.tv_right_title, getSolidSelect())
                }
                5 -> { // 已评价
                    holder.setText(R.id.tv_status, "交易成功")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, true)
                            .setText(R.id.tv_middle_title, "删除订单")
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "再次购买")
                }
                6 -> { // 退款中
                    holder.setText(R.id.tv_status, "退款中")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "查看详情")
                }
                7 -> { // 已退款
                    holder.setText(R.id.tv_status, "退款成功")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "查看详情")
                }
                8 -> { // 退款失败
                    holder.setText(R.id.tv_status, "退款失败")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "查看详情")
                }
                9 -> { // 已关闭
                    holder.setText(R.id.tv_status, "交易关闭")
                            .setVisible(R.id.tv_left_title, false)
                            .setVisible(R.id.tv_middle_title, false)
                            .setVisible(R.id.tv_right_title, true)
                            .setText(R.id.tv_right_title, "删除订单")
                }
                else -> {

                }
            }
        }
    }

    private fun getStrokeSelect() = DrawableCreator.Builder()
            .setStrokeWidth(mContext.getDimension(R.dimen.dp_1))
            .setStrokeColor(0xFFFF0000.toInt())
            .setCornersRadius(mContext.getDimension(R.dimen.dp_30))
            .build()

    private fun getSolidSelect() = DrawableCreator.Builder()
            .setCornersRadius(mContext.getDimension(R.dimen.dp_30))
            .setSolidColor(ContextCompat.getColor(mContext, R.color.theme_dark_purple))
            .build()

}
