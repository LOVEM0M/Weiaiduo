package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class VideoListContract {

    interface IView : IBaseView {
        fun getVideoListSuccess(bean:HomeVideoBean)
    }

    interface IPresenter :IBasePresenter<IView> {
        fun getVideoList(currentPage: Int, pageSize: Int)
    }

}
