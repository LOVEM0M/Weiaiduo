package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CollectBean

class CollectContract {

    interface IView : IBaseView {
        fun getCollectListSuccess(bean: CollectBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCollectList(currentPage: Int, pageSize: Int)
    }

}
