package com.miyin.zhenbaoqi.ui.shop.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.setSelect
import com.miyin.zhenbaoqi.utils.SpanUtils

class BulkShipAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getContentView() = R.layout.item_bulk_ship

    override fun convert(holder: BaseViewHolder, data: String?) {
        with(data) {
            val url = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3123218507,1369326272&fm=26&gp=0.jpg"
            val color = ContextCompat.getColor(mContext, R.color.text_33)
            holder.setSelect(R.id.iv_left, false)
                    .loadImg(R.id.iv_cover, url)
                    .setText(R.id.tv_name, "张三")
                    .setText(R.id.tv_level, "V0青铜")
                    .setText(R.id.tv_user_info, SpanUtils()
                            .append("收件人\u3000")
                            .append("杨森\u300012345678901").setForegroundColor(color)
                            .create())
                    .setText(R.id.tv_address, SpanUtils()
                            .append("地\u3000址\u3000")
                            .append("江苏省 镇江市")
                            .create())
                    .loadImg(R.id.iv_cover, url)
                    .setText(R.id.tv_title, "12121")
                    .setText(R.id.tv_price, "¥1400.00")
                    .setText(R.id.tv_count, "x1")
                    .setText(R.id.tv_total_price, "共1件商品\u3000合计：¥1400.00")
        }
    }

}
