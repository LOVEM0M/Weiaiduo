package com.miyin.zhenbaoqi.ui.mine.presenter

import androidx.collection.ArrayMap
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

    override fun commitGoodsEval(evaluationContent: String?, evaluationImg: String?, orderNumber: String) {
        if (evaluationContent.isNullOrEmpty() or evaluationImg.isNullOrEmpty()) {
            getView()?.showToast("请对该商品进行评价")
            return
        }
        val map = ArrayMap<String, Any>().apply {
            put("evaluationContent", evaluationContent)
            put("evaluationImg", evaluationImg)
            put("orderNumber", orderNumber)
        }
        request(RetrofitUtils.mApiService.orderEval(map), object : BaseSingleObserver<OrderEvalBean>() {//暂无返回数据，没有修改实体类
            override fun doOnSuccess(data: OrderEvalBean) {
                getView()?.showToast(data.msg)
                getView()?.commitGoodsEvalSuccess(data)
            }
        })
    }

}
