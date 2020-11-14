package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.ext.setSelect

class AddressAdapter(list: List<AddressBean.UserAddressBean>) : BaseAdapter<AddressBean.UserAddressBean>(list) {

    override fun getContentView() = R.layout.item_address

    override fun convert(holder: BaseViewHolder, data: AddressBean.UserAddressBean) {
        with(data) {
            holder.setText(R.id.tv_name, "收货人:$consignee")
                    .setText(R.id.tv_phone, phone_no)
                    .setText(R.id.tv_address, "收货地址：${pcc_name + address}")
                    .setSelect(R.id.tv_is_default, is_default == 0)
                    .addOnClickListener(R.id.cl_container)
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_delete)
                    .addOnClickListener(R.id.tv_is_default)
        }
    }

}
