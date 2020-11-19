package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.MerchantBean

class MerchantInfoContract {

    interface IView : IBaseView {
        fun getMerchantInfoSuccess(bean: MerchantBean)

        fun uploadMerchantHeadSuccess(path: String)

        fun uploadMerchantBackgroundSuccess(path: String)

        fun updateMerchantInfoSuccess()

        fun getProvinceListSuccess(list: List<CityBean.DataBean>)

        fun getAreaListSuccess(list: List<CityBean.DataBean>, position: Int, state: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantInfo()

        fun uploadMerchantHead(path: String)

        fun uploadMerchantBackground(path: String)

        fun updateMerchantInfo(address: String?, cityId: Int, consignee: String?, countyId: Int, headImg: String?,
                               merchantsBack: String?, merchantsName: String?, merchantsSubtitle: String?,
                               phoneNo: String?, provinceId: Int, wechatId: String?)

        fun getProvinceList()

        fun getAreaList(position: Int, state: Int, parentId: Int)
    }

}
