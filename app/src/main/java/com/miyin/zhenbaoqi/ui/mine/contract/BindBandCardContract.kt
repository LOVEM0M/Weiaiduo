package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.BankBean
import com.miyin.zhenbaoqi.bean.RealNameBean

class BindBandCardContract {

    interface IView : IBaseView {
        fun getBankListSuccess(list: List<BankBean.BankListBean>)

        fun showRealNameSuccess(bean: RealNameBean)

        fun addBankCardSuccess()

        fun updateBankCardSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getBankList()

        fun showRealName()

        fun addBankCard(bankCardNum: String?, bankBranch: String?, bankId: Int)

        fun updateBankCard(bankCardNum: String?, bankBranch: String?, bankId: Int, userBankId: Int)
    }

}