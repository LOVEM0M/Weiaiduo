package com.miyin.zhenbaoqi.ui.home.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveHotBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.RoundCornersTransform

class HeaderLiveAdapter(list: List<LiveHotBean.LiveHotDataBean>) : com.miyin.zhenbaoqi.base.BaseAdapter<LiveHotBean.LiveHotDataBean>(list) {

    override fun getContentView() = R.layout.item_header_live

    override fun convert(holder: BaseViewHolder, data: LiveHotBean.LiveHotDataBean) {
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        when (holder.adapterPosition) {
            0 -> params.leftMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_10)
            mData.size - 1 -> params.rightMargin = mContext.resources.getDimensionPixelOffset(R.dimen.dp_10)
        }
        holder.itemView.layoutParams = params

        with(data) {
            val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
            holder.transform(R.id.iv_cover, face_url, transform)
                    .setText(R.id.tv_desc, room_name)
                    .setText(R.id.tv_watch_count, "${popularity}人观看")
        }
    }

}
