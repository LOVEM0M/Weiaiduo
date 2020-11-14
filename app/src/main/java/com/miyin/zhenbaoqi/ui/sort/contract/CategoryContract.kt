package com.miyin.zhenbaoqi.ui.sort.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.SearchGoodsBean

class CategoryContract {

    interface IView : IBaseView {
        fun getThirdLevelSuccess(bean: CityBean)

        fun getSearchGoodsListSuccess(bean: SearchGoodsBean)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getThirdLevel(parentId: Int)

        fun getSearchGoodsList(cateId1: Int, cateId2: Int, cateId3: Int, currentPage: Int,
                               pageSize: Int, searchParam: String?, state: Int, amountMinSection: Long,
                               amountMaxSection: Long)
    }

}
