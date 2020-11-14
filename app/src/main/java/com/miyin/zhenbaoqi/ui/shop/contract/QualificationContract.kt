package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.MerchantBean

class QualificationContract {

    interface IView : IBaseView {
        fun getMerchantInfoSuccess(bean: MerchantBean)

        fun uploadImageSuccess(path: String)

        fun getProvinceListSuccess(list: List<CityBean.CityListBean>)

        fun getAreaListSuccess(list: List<CityBean.CityListBean>, position: Int, state: Int)

        fun getCategoryListSuccess(bean: CityBean)

        fun merchantAuthSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantInfo()

        fun uploadImage(path: String?)

        fun getProvinceList()

        fun getAreaList(position: Int, state: Int, parentId: Int)

        fun getCategoryList(codeType: String)

        fun merchantAuth(address: String?, cityId: Int, consignee: String?, countyId: Int, headImg: String?,
                         identityImages: String, identityType: Int, licenceImage: String, mainCate: Int,
                         merchantsBack: String?, merchantsName: String?, merchantsSubtitle: String?,
                         phoneNo: String?, provinceId: Int, wechatId: String?)
    }

}
