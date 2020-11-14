package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AddressBean

class AddressContact {

    interface IView : IBaseView {
        fun getAddressListSuccess(bean: AddressBean)

        fun deleteAddressSuccess()

        fun setDefaultAddressSuccess()

        fun onFailure(msg: String, flag: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAddressList(currentPage: Int, pageSize: Int)

        fun deleteAddress(addressId: Int)

        fun setDefaultAddress(address: String, addressId: Int, cityId: Int, consignee: String,
                              countyId: Int, isDefault: Int, phoneNo: String, provinceId: Int)
    }

}
