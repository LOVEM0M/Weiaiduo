package com.miyin.zhenbaoqi.ui.home.presenter

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
            }
        })
    }

    override fun bannerClick(bannerId: Int, position: Int) {
        request((RetrofitUtils.mApiService.bannerClick(bannerId)), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.bannerClickSuccess(position)
            }

            override fun doOnFailure(data:ResponseBean) {
                getView()?.onFailure("", 3)
            }

            override fun onError(e: Throwable) {
                getView()?.onFailure("", 3)
                super.onError(e)
            }
        })
    }

    override fun getCategoryList(codeType: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("code_type"), arrayOf(codeType))
        request(RetrofitUtils.mApiService.parentList(requestBody), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.showNormal()
                getView()?.getCategoryListSuccess(data)
            }

            override fun doOnFailure(data: CityBean) {
                getView()?.showError()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getView()?.showError()
                getView()?.showError()
            }
        })
    }

    override fun getRestoreList(currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("current_page", "page_size")
        val valueArray = arrayOf<Any>(currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.restoreList(requestBody), object : BaseSingleObserver<RestoreBean>() {
            override fun doOnSuccess(list: RestoreBean) {
                getView()?.showNormal()
                getView()?.getRestoreListSuccess(list)
            }

            override fun doOnFailure(list: RestoreBean) {
                if (currentPage == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }

    override fun getSelectFirstGoodsList(cateId1: Int, currentPage: Int, pageSize: Int) {
        val keyArray = arrayOf("cate_id1","current_page", "page_size")
        val valueArray = arrayOf<Any>(cateId1,currentPage, pageSize)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.selectFirstGoodsList(requestBody), object : BaseSingleObserver<SelectFirstGoodsBean>() {
            override fun doOnSuccess(list: SelectFirstGoodsBean) {
                getView()?.showNormal()
                getView()?.getSelectFirstGoodsListSuccess(list)
            }

            override fun doOnFailure(list: SelectFirstGoodsBean) {
                if (currentPage == 1) {
                    getView()?.showNormal()
                    getView()?.onFailure("", 0)
                } else {
                    super.doOnFailure(list)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (currentPage == 1) {
                    getView()?.showError()
                }
            }
        })
    }


}
