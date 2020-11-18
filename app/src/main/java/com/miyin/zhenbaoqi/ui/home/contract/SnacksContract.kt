package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.FirstCategorySecondBean
import com.miyin.zhenbaoqi.bean.GoodsSearchBean
import com.miyin.zhenbaoqi.bean.SearchBean
import com.miyin.zhenbaoqi.bean.SecondCategoryGoodsBean

class SnacksContract {

    interface IView : IBaseView {
        fun getFirstCategorySecondListSuccess(bean: FirstCategorySecondBean)
        fun getSecondCategoryGoodsListSuccess(bean: SecondCategoryGoodsBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getFirstCategorySecondList(cateId1: Int)
        fun getSecondCategoryGoodsList(cateId2: Int ,page: Int ,rows: Int )
    }

}
