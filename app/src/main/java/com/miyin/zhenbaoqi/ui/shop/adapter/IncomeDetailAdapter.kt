package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MerchantPayList
import com.miyin.zhenbaoqi.utils.FormatUtils

class IncomeDetailAdapter(list: List<MerchantPayList.ListBean>) : BaseAdapter<MerchantPayList.ListBean>(list) {

    override fun getContentView() = R.layout.item_income_detail

    override fun convert(holder: BaseViewHolder, data: MerchantPayList.ListBean) {
        with(data) {
            holder.setText(R.id.tv_title, change_name)
                    .setText(R.id.tv_price, (if (asset_type == 1) "+" else "-") + (if (change_amount == 0) "0.00" else FormatUtils.formatNumber(change_amount / 100f)))
                    .setText(R.id.tv_date_time, create_time)
                    .setText(R.id.tv_state, when (type) {
                        1 -> "等待入账"
                        2 -> "已经入账"
                        3 -> "退款"
                        4 -> "支出"
                        else -> ""
                    })
        }
    }

}
