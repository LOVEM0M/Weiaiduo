package com.miyin.zhenbaoqi.ui.mine.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.ShopAttentionBean

class ShopContract {

    interface IView : IBaseView {
        fun merchantListSuccess(bean: ShopAttentionBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun merchantList(currentPage: Int, pageSize: Int)
    }

}
