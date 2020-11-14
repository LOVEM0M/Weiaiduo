package com.miyin.zhenbaoqi.ui.message.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.NoticeBean

class OfficialNotificationContract {

    interface IView : IBaseView {
        fun getNoticeListSuccess(bean: NoticeBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getNoticeList(cateId: Int, currentPage: Int, pageSize: Int)
    }

}
