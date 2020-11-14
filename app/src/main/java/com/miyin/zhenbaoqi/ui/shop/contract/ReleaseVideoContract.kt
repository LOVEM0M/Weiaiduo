package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class ReleaseVideoContract {

    interface IView : IBaseView {
        fun getMainCategorySuccess(bean: CityBean)

        fun uploadVideoSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getMainCategory()

        fun editVideo(videoId: Int, desc: String?, type: Int, path: String?)

        fun uploadVideo(path: String?, desc: String?, type: Int)
    }

}