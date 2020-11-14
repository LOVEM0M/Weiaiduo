package com.miyin.zhenbaoqi.ui.message.adapter

import androidx.core.content.ContextCompat
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.MessageBean
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.orhanobut.logger.Logger

class SystemMessageAdapter(list: List<MessageBean.ListBean>) : BaseAdapter<MessageBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_system_message

    override fun convert(holder: BaseViewHolder, data: MessageBean.ListBean) {
        with(data) {
            holder.setText(R.id.tv_title, message_name)
                    .setText(R.id.tv_time, TimeUtils.millis2String(add_time!!.toLong(), "yyyy-MM-dd HH:mm"))

            SpanUtils.with(holder.getView(R.id.tv_desc))
                    .append("$message_value\u3000")
                    .append("点击查看》")
                    .setClickSpan(object : ClickableSpan() {
                        override fun onClick(view: View) {

                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.run {
                                isUnderlineText = false
                                color = ContextCompat.getColor(mContext, R.color.text_33)
                            }
                        }
                    })
                    .create()
        }
    }

}
