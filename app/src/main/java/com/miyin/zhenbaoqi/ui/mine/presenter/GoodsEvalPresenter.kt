package com.miyin.zhenbaoqi.ui.mine.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.OrderEvalBean
import com.miyin.zhenbaoqi.bean.OrderEvalListBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.mine.contract.GoodsEvalContract
import com.miyin.zhenbaoqi.utils.JSONUtils

class GoodsEvalPresenter : BasePresenter<GoodsEvalContract.IView>(), GoodsEvalContract.IPresenter {

    override fun getGoodsEval(orderNumber: String) {
        val requestBody = JSONUtils.createJSON(arrayOf("order_number"), arrayOf(orderNumber))
        request(RetrofitUtils.mApiService.orderEvalList(requestBody), object : BaseSingleObserver<OrderEvalListBean>() {
            override fun doOnSuccess(data: OrderEvalListBean) {
                getView()?.getGoodsEvalSuccess(data)
            }
        })
    }

    override fun commitGoodsEval(evaluationContent: String?, gradeIds: String?, orderNumber: String) {
        if (evaluationContent.isNullOrEmpty() or gradeIds.isNullOrEmpty()) {
            getView()?.showToast("请对该商品进行评价")
            return
        }

        val keyArray = arrayOf("comment_names", "grade_ids", "order_number")
        val valueArray = arrayOf<Any>(evaluationContent!!, gradeIds!!, orderNumber)
        val requestBody = JSONUtils.createJSON(keyArray, valueArray)
        request(RetrofitUtils.mApiService.orderEval(requestBody), object : BaseSingleObserver<OrderEvalBean>() {
            override fun doOnSuccess(data: OrderEvalBean) {
                getView()?.showToast(data.msg)
                getView()?.commitGoodsEvalSuccess(data)
            }
        })
    }

}
