package com.miyin.zhenbaoqi.ui.live.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.LiveRoomBean
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.CornerTransform

class LiveAdapter(list: List<LiveRoomBean.ListBean>) : BaseAdapter<LiveRoomBean.ListBean>(list) {

    override fun getContentView() = R.layout.item_live

    override fun convert(holder: BaseViewHolder, data: LiveRoomBean.ListBean) {
        with(data) {
            val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            if (holder.adapterPosition % 2 == 0) {
                params.rightMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
            } else {
                params.leftMargin = mContext.getDimensionPixelSize(R.dimen.dp_4)
            }
            holder.itemView.layoutParams = params

            val transform = CornerTransform(mContext, DensityUtils.dp2px(5f).toFloat())
            holder.transform(R.id.iv_cover, face_url, transform)
                    .loadImg(R.id.iv_avatar, icon_url)
                    .setText(R.id.tv_shop_name, room_name)
                    .setText(R.id.tv_watch_count, "${popularity}人正在观看")
                    .setText(R.id.tv_profit, "赚 9.0%")
                    .setVisible(R.id.tv_profit, false)
        }
    }

}
