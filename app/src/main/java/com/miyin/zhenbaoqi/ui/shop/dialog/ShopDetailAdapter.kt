package com.miyin.zhenbaoqi.ui.shop.dialog

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MerchantGoodsStoreBean
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class ShopDetailAdapter(list: List<MerchantGoodsStoreBean.ListBean>) : BaseAdapter<MerchantGoodsStoreBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_shop_detail

    override fun convert(holder: BaseViewHolder, data: MerchantGoodsStoreBean.ListBean) {
        with(data) {
            val transform = CornerTransform(mContext, DensityUtils.dp2px(7f).toFloat())
            val url = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            holder.transform(R.id.iv_cover, url, transform)
                    .setText(R.id.tv_title, goods_name)
                    .setText(R.id.tv_price, SpanUtils()
                            .append("¥").setFontSize(11, true)
                            .append(FormatUtils.formatNumber(goods_amount  )).setFontSize(17, true).setBold()
                            .create())
        }
    }

}
