package com.miyin.zhenbaoqi.ui.sort.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.SearchGoodsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.sort.contract.CategoryContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class CategoryPresenter : BasePresenter<CategoryContract.IView>(), CategoryContract.IPresenter {

    override fun getThirdLevel(parentId: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("parentId", parentId)
        }
        request(RetrofitUtils.mApiService.sonList(map), object : BaseSingleObserver<CityBean>() {
            override fun doOnSuccess(data: CityBean) {
                getView()?.getThirdLevelSuccess(data)
            }
        })
    }

    override fun getSearchGoodsList(cateId1: Int, cateId2: Int, cateId3: Int, currentPage: Int, pageSize: Int,
                                    searchParam: String?, state: Int, amountMinSection: Long, amountMaxSection: Long) {
        val searchContent = searchParam ?: ""
        val keyArray = arrayOf("cate_id1", "cate_id2", "cate_id3", "current_page", "page_size",
                "search_param", "state", "amount_min_section", "amount_max_section")
        val valueArray = arrayOf<Any>(cateId1, cateId2, cateId3, currentPage, pageSize, searchContent,
                state, amountMinSection, amountMaxSection)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.searchGoodsList(requestBody), object : BaseSingleObserver<SearchGoodsBean>() {
            override fun doOnSuccess(data: SearchGoodsBean) {
                getView()?.showNormal()
                getView()?.getSearchGoodsListSuccess(data)
            }

            override fun doOnFailure(data: SearchGoodsBean) {
                if (currentPage == 1) {
                    getView()?.showEmpty()
                } else {
                    getView()?.showToast(data.msg)
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
