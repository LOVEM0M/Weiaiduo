package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*

class MineContract {

    interface IView : IBaseView {
        fun getUserInfoSuccess(bean: UserInfoBean)

        fun getUserTypeSuccess(userType: Int)

        fun getUserLevelSuccess(bean: UserGradeBean)

        fun getMerchantInfoSuccess(bean: MerchantBean)

        fun getMerchantBaseInfoSuccess(bean: MerchantBaseInfoBean)

        fun getMerchantShopDataSuccess(bean: MerchantShopDataBean)

        fun getSystemCustomerSuccess(chatId: String)

        fun getHomeGoodsHotListSuccess(bean:HomeGoodsHotBean)

        fun getFootprintSizeSuccess(size: Int)

        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getUserType()

        fun getMerchantInfo()

        fun getSystemCustomer()

        fun getFootprintSize()

        fun getHomeGoodsHotList()
    }

}
