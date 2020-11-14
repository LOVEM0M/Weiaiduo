package com.miyin.zhenbaoqi.ui.shop.adapter

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ext.loadImg

class UploadPhotoAdapter(list: List<String>) : BaseItemDraggableAdapter<String, BaseViewHolder>(R.layout.item_upload_photo, list) {

    private var mIsShowDelete = false

    override fun convert(holder: BaseViewHolder, data: String) {
        holder.loadImg(R.id.iv_cover, data)
                .setVisible(R.id.iv_delete, mIsShowDelete)
                .setVisible(R.id.tv_cover, holder.adapterPosition == 0)
                .addOnClickListener(R.id.iv_cover, R.id.iv_delete)
    }

    fun setDeleteState(isShowDelete: Boolean) {
        mIsShowDelete = isShowDelete
        notifyDataSetChanged()
    }

}
