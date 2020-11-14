package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class AddAddressContract {

    interface IView : IBaseView {
        fun addAddressSuccess()

        fun updateAddressSuccess()

        fun getProvinceListSuccess(list: List<CityBean.CityListBean>)

        fun getAreaListSuccess(list: List<CityBean.CityListBean>, position: Int, state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun addAddress(address: String?, cityId: Int, consignee: String? = null, countyId: Int,
                       isDefault: Int, phoneNo: String? = null, provinceId: Int)

        fun updateAddress(address: String?, addressId: Int, cityId: Int, consignee: String? = null,
                          countyId: Int, isDefault: Int, phoneNo: String? = null, provinceId: Int)

        fun getProvinceList()

        fun getAreaList(position: Int, state: Int, parentId: Int)
    }

}
