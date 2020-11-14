package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BankCardBean

class BankCardContract {

    interface IView : IBaseView {
        fun getBankCardListSuccess(list: List<BankCardBean.ListBean>)

        fun deleteBankCardSuccess(position: Int)

        fun onFailure(msg: String, flag: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBankCardList()

        fun deleteBankCard(userBankId: Int, position: Int)
    }

}