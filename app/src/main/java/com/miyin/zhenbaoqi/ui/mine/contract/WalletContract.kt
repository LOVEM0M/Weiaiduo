package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BillBean

class WalletContract {

    interface IView : IBaseView {
        fun getBillListSuccess(bean: BillBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBillList(changeType: Int, currentPage: Int, pageSize: Int)
    }

}
