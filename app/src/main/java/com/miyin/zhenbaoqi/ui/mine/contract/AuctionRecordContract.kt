package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.AuctionRecoredBean

class AuctionRecordContract {

    interface IView : IBaseView {
        fun getAuctionRecordSuccess(bean: AuctionRecoredBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getAuctionRecord(currentPage: Int, pageSize: Int)
    }

}
