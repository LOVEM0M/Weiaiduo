package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.ui.mine.contract.OrderSearchContract

class OrderSearchPresenter : BasePresenter<OrderSearchContract.IView>(), OrderSearchContract.IPresenter {

    override fun searchGoods(currentPage: Int, pageSize: Int, keyWord: String?) {
        if (keyWord.isNullOrEmpty()) {
            getView()?.showToast("请输入搜索关键词")
            return
        }


    }

}
