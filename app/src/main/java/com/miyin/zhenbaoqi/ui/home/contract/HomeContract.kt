package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.HomeBannerBean
import com.miyin.zhenbaoqi.bean.RestoreBean
import com.miyin.zhenbaoqi.bean.SelectFirstGoodsBean

class HomeContract {

    interface IView : IBaseView {
        fun getSelectFirstGoodsListSuccess(bean: SelectFirstGoodsBean)
        fun getRestoreListSuccess(bean: RestoreBean)
        fun getCategoryListSuccess(bean: CityBean)
        fun getHomeBannerSuccess(bean: HomeBannerBean)
        fun bannerClickSuccess(position: Int)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun homeBanner()
        fun bannerClick(bannerId: Int, position: Int)
        fun getCategoryList(codeType: String)
        fun getRestoreList(currentPage: Int, pageSize: Int)
        fun getSelectFirstGoodsList(cateId1: Int, currentPage: Int, pageSize: Int)
    }

}
