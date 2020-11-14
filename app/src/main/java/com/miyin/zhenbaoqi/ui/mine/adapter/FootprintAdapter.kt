package com.miyin.zhenbaoqi.ui.mine.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.FootprintBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.setSelect
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.noober.background.drawable.DrawableCreator

class FootprintAdapter(list: List<FootprintBean>) : BaseMultiItemQuickAdapter<FootprintBean, BaseViewHolder>(list) {

    private var mIsShowDelete = false

    init {
        addItemType(0, R.layout.item_footprint_title)
        addItemType(1, R.layout.item_footprint)
    }

    override fun convert(holder: BaseViewHolder, entity: FootprintBean) {
        if (mIsShowDelete) {
            DrawableCreator.setDrawable(DrawableCreator.Builder()
                    .setSelectedDrawable(mContext.resources.getDrawable(R.drawable.ic_release_select))
                    .setUnSelectedDrawable(mContext.resources.getDrawable(R.drawable.ic_release_normal))
                    .build(),
                    holder.getView(R.id.tv_title), DrawableCreator.DrawablePosition.Left)

            holder.setSelect(R.id.tv_title, entity.isSelect)
        } else {
            val textView = holder.getView<TextView>(R.id.tv_title)
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        if (entity.itemType == 0) {
            holder.setText(R.id.tv_title, entity.data)
                    .addOnClickListener(R.id.tv_title)
        } else if (entity.itemType == 1) {
            val json = entity.bean?.data
            if (!json.isNullOrEmpty()) {
                val map = JSONUtils.gson.fromJson<Map<String, Any>>(json, Map::class.java)

                val transform = RoundCornersTransform(mContext.getDimension(R.dimen.dp_4), RoundCornersTransform.CornerType.TOP)
                holder.transform(R.id.iv_cover, map["goods_img"], transform)
                        .setText(R.id.tv_title, FormatUtils.formatNumber(map["goods_amount"].toString().toDouble() / 100f))
                        .addOnClickListener(R.id.ll_container)
            }

            holder.addOnClickListener(R.id.tv_title)
        }
    }

    fun getDeleteFlag() = mIsShowDelete

    fun setDeleteFlag(isShowDelete: Boolean) {
        mIsShowDelete = isShowDelete
        notifyDataSetChanged()
    }

}