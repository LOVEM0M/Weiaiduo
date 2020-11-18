package com.miyin.zhenbaoqi.ui.home.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.*

class HomeContract {

    interface IView : IBaseView {
        fun getHomeBannerSuccess(bean: HomeBannerBean)
        fun getBannerCategorySuccess(bean: BannerCategoryBean)

        fun getVipFirstFreegoodsListSuccess(bean: VipFirstFreegoodsBean)
        fun getFirstCategoryListSuccess(bean: FirstCategoryBean)
//        fun bannerClickSuccess(position: Int)

        fun getFirstCategoryGoodsListSuccess(bean: FirstCategoryGoodsBean)
        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun homeBanner()
        fun getBannerCategory()
//        fun bannerClick(bannerId: Int, position: Int)
        fun getFirstCategoryList()
        fun getVipFirstFreegoodsList(page: Int, rows: Int)
        fun getFirstCategoryGoodsList(cateId1: Int, page :Int , rows :Int )
    }

}
