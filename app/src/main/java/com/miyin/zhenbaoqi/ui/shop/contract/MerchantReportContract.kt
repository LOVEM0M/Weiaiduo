package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ReportBean

class MerchantReportContract {

    interface IView : IBaseView {
        fun merchantsStatisticsSuccess(bean: ReportBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun merchantsStatistics(type: Int)
    }

}
