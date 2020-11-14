package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BillPayBean
import com.miyin.zhenbaoqi.bean.BillWithdrawBean

class BillDetailContract {

    interface IView : IBaseView {
        fun withdrawDetailSuccess(bean: BillWithdrawBean)

        fun incomeDetailSuccess()

        fun payDetailSuccess(bean: BillPayBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun withdrawDetail(changeNumber: String)

        fun incomeDetail(changeNumber: String)

        fun payDetail(changeNumber: String)
    }

}