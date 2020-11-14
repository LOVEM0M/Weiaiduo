package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BankCardBean

class MerchantWithdrawContact {

    interface IView : IBaseView {
        fun getBankCardListSuccess(bean: List<BankCardBean.ListBean>)

        fun walletMerchantWithdrawSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBankCardList()

        fun walletMerchantWithdraw(amount: Long, type: Int, bankId: Int)
    }

}
