package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class ManagerVideoContract {

    interface IView : IBaseView {
        fun getVideoListSuccess(bean: HomeVideoBean)

        fun deleteVideoSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getVideoList(currentPage: Int, pageSize: Int, type: Int)

        fun deleteVideo(videoId: Int)
    }

}
