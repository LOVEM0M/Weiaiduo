package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BankCardBean

class WithdrawContract {

    interface IView : IBaseView {
        fun withdrawSuccess()

        fun getBankCardListSuccess(list: List<BankCardBean.ListBean>)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun withdraw(amount: String?, userBankId: Int)

        fun getBankCardList()
    }

}
