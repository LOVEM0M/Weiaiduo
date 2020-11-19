package com.miyin.zhenbaoqi.ui.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseAdapter
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.ext.setSelect

class AddressAdapter(list: List<AddressBean.DataBeanX.UserAddressBean>) : BaseAdapter<AddressBean.DataBeanX.UserAddressBean>(list) {

    override fun getContentView() = R.layout.item_address

    override fun convert(holder: BaseViewHolder, data: AddressBean.DataBeanX.UserAddressBean) {
        with(data) {
            holder.setText(R.id.tv_name, "收货人:$consignee")
                    .setText(R.id.tv_phone, phoneNo)
                    .setText(R.id.tv_address, "收货地址：${pccName + address}")
                    .setSelect(R.id.tv_is_default, isDefault == 0)
                    .addOnClickListener(R.id.cl_container)
                    .addOnClickListener(R.id.tv_edit)
                    .addOnClickListener(R.id.tv_delete)
                    .addOnClickListener(R.id.tv_is_default)
        }
    }

}
