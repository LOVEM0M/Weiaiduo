package com.miyin.zhenbaoqi.ui.home.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.*
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.HomeContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class HomePresenter : BasePresenter<HomeContract.IView>(), HomeContract.IPresenter {
    override fun homeBanner() {
        request(RetrofitUtils.mApiService.homeBanner(), object : BaseSingleObserver<HomeBannerBean>() {
            override fun doOnSuccess(data:HomeBannerBean) {
                getView()?.getHomeBannerSuccess(data)
            }

            override fun doOnFailure(data:HomeBannerBean) {
                getView()?.showToast(data.msg )
            }
        })
    }

    override fun getBannerCategory() {
        request(RetrofitUtils.mApiService.bannerCategory(), object : BaseSingleObserver<BannerCategoryBean>() {
            override fun doOnSuccess(list: BannerCategoryBean) {
                getView()?.showNormal()
                getView()?.getBannerCategorySuccess(list)
            }

            override fun doOnFailure(list: BannerCategoryBean) {
                getView()?.showToast(list.msg )
                    super.doOnFailure(list)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }

//    override fun bannerClick(bannerId: Int, position: Int) {
//        request((RetrofitUtils.mApiService.bannerClick(bannerId)), object : BaseSingleObserver<ResponseBean>() {
//            override fun doOnSuccess(data: ResponseBean) {
//                getView()?.bannerClickSuccess(position)
//            }
//
//            override fun doOnFailure(data:ResponseBean) {
//                getView()?.onFailure("", 3)
//            }
//
//            override fun onError(e: Throwable) {
//                getView()?.onFailure("", 3)
//                super.onError(e)
//            }
//        })
//    }

    override fun getFirstCategoryList() {
        request(RetrofitUtils.mApiService.firstCategoryList(), object : BaseSingleObserver<FirstCategoryBean>() {
            override fun doOnSuccess(data: FirstCategoryBean) {
                getView()?.showNormal()
                getView()?.getFirstCategoryListSuccess(data)
            }

            override fun doOnFailure(data: FirstCategoryBean) {
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
                getView()?.showError()
            }
        })
    }

    override fun getVipFirstFreegoodsList(page: Int, rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.vipFirstFreegoodsList(map), object : BaseSingleObserver<VipFirstFreegoodsBean>() {
            override fun doOnSuccess(list: VipFirstFreegoodsBean) {
                getView()?.showNormal()
                getView()?.getVipFirstFreegoodsListSuccess(list)
            }

            override fun doOnFailure(list: VipFirstFreegoodsBean) {
                if (page == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (page == 1) {
                    getView()?.showError()
                }
            }
        })
    }



    override fun getFirstCategoryGoodsList(cateId1: Int, page :Int , rows :Int ) {
        val map = ArrayMap<String, Any>().apply {
            put("cateId1", cateId1)
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.firstcategorygoods(map), object : BaseSingleObserver<FirstCategoryGoodsBean>() {
            override fun doOnSuccess(list: FirstCategoryGoodsBean) {
                getView()?.showNormal()
                getView()?.getFirstCategoryGoodsListSuccess(list)
            }

            override fun doOnFailure(list: FirstCategoryGoodsBean) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                    super.doOnFailure(list)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                    getView()?.showError()
            }
        })
    }


}
