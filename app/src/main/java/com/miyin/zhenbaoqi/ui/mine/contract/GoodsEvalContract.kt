package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.OrderEvalBean
import com.miyin.zhenbaoqi.bean.OrderEvalListBean

class GoodsEvalContract {

    interface IView : IBaseView {
        fun getGoodsEvalSuccess(bean: OrderEvalListBean)

        fun commitGoodsEvalSuccess(bean: OrderEvalBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getGoodsEval(orderNumber: String)

        fun commitGoodsEval(evaluationContent: String?, gradeIds: String?, orderNumber: String)
    }

}
