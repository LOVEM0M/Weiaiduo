package com.miyin.zhenbaoqi.ui.shop.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.InvitePlayerBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.setRightDrawable
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils

class ManagerInviteAdapter(list: List<InvitePlayerBean.DataBean>, private val title: String?) : BaseAdapter<InvitePlayerBean.DataBean>(list) {

    override fun getContentView() = R.layout.item_manager_invite

    override fun convert(holder: BaseViewHolder, data: InvitePlayerBean.DataBean) {
        with(data) {
            holder.loadImg(R.id.iv_avatar, headImg)
                    .setText(R.id.tv_title, nickName)
                    .setText(R.id.tv_date_time, registerTime)

            when (title) {
                "新秀" -> holder.setText(R.id.tv_price, SpanUtils()
                        .append("¥${FormatUtils.formatNumber(payAmount  )}").setForegroundColor(0xFFFF3D3D.toInt())
                        .create())
                "专属粉丝" -> holder.setText(R.id.tv_price, SpanUtils()
                        .appendLine("已购买${count}单").setForegroundColor(ContextCompat.getColor(mContext, R.color.text_33))
                        .append("¥${FormatUtils.formatNumber(payAmount  )}").setForegroundColor(0xFFFF3D3D.toInt())
                        .create())
                else -> holder.setText(R.id.tv_price, "")
                        .setRightDrawable(R.id.tv_price, R.drawable.ic_right_arrow_small)
            }
        }
    }

}
