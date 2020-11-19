package com.miyin.zhenbaoqi.ui.shop.contract

import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.bean.CityBean

class ReleaseGoodsContract {

    interface IView : IBaseView {
        fun getCategoryNameSuccess(cateName1: String, cateName2: String, cateName3: String)

        fun getParentListSuccess(list: List<CityBean.DataBean>)

        fun getSonListSuccess(list: List<CityBean.DataBean>, type: Int)

        fun uploadGoodsImgSuccess(url: String)

        fun uploadGoodsVideoSuccess(url: String)

        fun updateMerchantMainCateStateSuccess()

        fun onFailure(msg: String, type: Int)
    }

    interface IPresenter : IBasePresenter<IView> {
        fun getCategoryName(cateId1: Int, cateId2: Int, cateId3: Int)

        fun getMerchantMainCateState()

        fun getParentList(type: Int)

        fun getSonList(parentId: Int, type: Int)

        fun uploadGoodsImg(path: String)

        fun uploadGoodsVideo(path: String)

        fun updateMerchantMainCateState(mainCate: Int)
    }

}
