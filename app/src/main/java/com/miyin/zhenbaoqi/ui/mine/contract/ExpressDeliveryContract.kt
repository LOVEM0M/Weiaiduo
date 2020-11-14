package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class ExpressDeliveryContract {

    interface IView : IBaseView {
        fun confirmShipSuccess()

        fun merchantShipSuccess()

        fun getCourierCompanyListSuccess(list: List<CityBean.CityListBean>)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCourierCompanyList(codeType: String)

        fun confirmShip(courierNo: String?, dictId: Int, orderNumber: String)

        fun merchantShip(courierCom: String?, courierName: String, courierNo: String?, orderNumber: String)

        fun updateCourierNo(courierCom: String?, courierName: String, courierNo: String?, orderNumber: String)
    }

}