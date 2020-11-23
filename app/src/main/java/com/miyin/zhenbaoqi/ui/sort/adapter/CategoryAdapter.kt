package com.miyin.zhenbaoqi.ui.sort.adapter

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.SearchGoodsBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.noober.background.drawable.DrawableCreator

class CategoryAdapter(list: List<SearchGoodsBean.ListBean>) : BaseAdapter<SearchGoodsBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_category

    override fun convert(holder: BaseViewHolder, data: SearchGoodsBean.ListBean) {
        val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.TOP)

        val position = holder.adapterPosition
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position % 2 == 1) {
            params.leftMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
        } else {
            params.leftMargin = mContext.getDimensionPixelSize(R.dimen.dp_8)
        }
        holder.itemView.layoutParams = params

        with(data) {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            holder.transform(R.id.iv_cover, goodsImg, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setVisible(R.id.iv_video, !goods_video.isNullOrEmpty())
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥ ")
                            .append(FormatUtils.formatNumber(goods_amount  )).setFontSize(17, true)
                            .create())
                    .setText(R.id.tv_profit, when (goods_type) {
                        1 -> "赚 ¥${FormatUtils.formatNumber(goods_amount / 100.0 / 100 * 9.5)}"
                        2 -> "赚 ¥${FormatUtils.formatNumber(goods_amount / 100.0 / 100 * 9.5)}"
                        else -> "赚 9.5%"
                    })
                    .setVisible(R.id.tv_profit, SPUtils.getInt("merchant_id") != 0)

            val llContainer = holder.getView<LinearLayout>(R.id.ll_container)
            llContainer.removeAllViews()
            when {
                tags.isNullOrEmpty() -> {
                }
                tags!!.contains(",") -> {
                    tags!!.split(",").forEach {
                        val textView = getTextView(mContext, it)
                        llContainer.addView(textView)
                    }
                }
                else -> {
                    val textView = getTextView(mContext, tags!!)
                    llContainer.addView(textView)
                }
            }
        }
    }

    private fun getTextView(context: Context, text: String) = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, context.getDimensionPixelSize(R.dimen.dp_15)).apply {
            rightMargin = context.getDimensionPixelSize(R.dimen.dp_5)
        }
        setPadding(context.getDimensionPixelSize(R.dimen.dp_10), 0, context.getDimensionPixelSize(R.dimen.dp_10), 0)
        gravity = Gravity.CENTER
        setText(text)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_8))
        setTextColor(0xFFEA2F30.toInt())
        background = DrawableCreator.Builder()
                .setStrokeColor(0xFFEA2F30.toInt())
                .setStrokeWidth(context.getDimension(R.dimen.dp_1))
                .setCornersRadius(context.getDimension(R.dimen.dp_15))
                .build()
    }

}
