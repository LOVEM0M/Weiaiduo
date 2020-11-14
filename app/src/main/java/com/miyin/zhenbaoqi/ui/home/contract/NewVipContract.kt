package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.bean.RestoreBean
import com.miyin.zhenbaoqi.bean.SearchBean

class NewVipContract {

    interface IView : IBaseView {
        fun getRestoreListSuccess(bean: RestoreBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getRestoreList(currentPage: Int, pageSize: Int)
    }

}
