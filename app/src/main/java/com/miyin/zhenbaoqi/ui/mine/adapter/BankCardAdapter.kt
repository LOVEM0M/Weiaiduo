package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.transform
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class BankCardAdapter(list: List<BankCardBean.ListBean>) : BaseAdapter<BankCardBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_bank_card

    override fun convert(holder: BaseViewHolder, data: BankCardBean.ListBean) {
        val transform = CornerTransform(mContext, mContext.getDimension(R.dimen.dp_5))
        transform.setExceptCorner(false, true, false, true)

        with(data) {
            holder.loadImg(R.id.iv_cover, bank_logo)
                    .transform(R.id.iv_avatar, bank_img, transform)
                    .setText(R.id.tv_title, bank_name)
                    .setText(R.id.tv_card_num, bank_card_num)
                    .addOnClickListener(R.id.fl_container)
                    .addOnClickListener(R.id.fl_right_menu)
        }
    }

}
