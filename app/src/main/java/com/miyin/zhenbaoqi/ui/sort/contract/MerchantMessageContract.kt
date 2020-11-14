package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.ResponseBean

class MerchantMessageContract {

    interface IView : IBaseView {
        fun getMerchantInfoSuccess(bean: ResponseBean)

        fun updateMerchantStateSuccess(focusState: Int)

        fun liveRoomEntrySuccess(bean: LiveEntryRoomBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMerchantInfo(merchantId: Int)

        fun updateMerchantState(focusState: Int, merchantId: Int)

        fun liveRoomEntry(roomId: Int)
    }

}
