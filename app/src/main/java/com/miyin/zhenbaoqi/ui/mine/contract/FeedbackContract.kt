package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

class FeedbackContract {

    interface IView : IBaseView {
        fun uploadImgSuccess(path: String)

        fun feedbackSuccess()
    }

    interface IPresenter : IBasePresenter<IView> {
        fun uploadImage(path: String)

        fun feedback(proposalContent: String?, repairPicture: String)
    }

}