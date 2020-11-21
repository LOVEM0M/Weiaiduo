package com.miyin.zhenbaoqi.ui.shopcart.presenter

import androidx.collection.ArrayMap
import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.CartGoodsListBean
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.home.contract.BargainContract
import com.miyin.zhenbaoqi.ui.home.contract.SignInContract
import com.miyin.zhenbaoqi.ui.home.contract.SnacksContract
import com.miyin.zhenbaoqi.ui.shopcart.contract.ShopCartContract

class ShopCartPresenter : BasePresenter<ShopCartContract.IView>(), ShopCartContract.IPresenter {
    override fun getShopCartList(page: Int, rows: Int) {
        val map = ArrayMap<String, Any>().apply {
            put("page", page)
            put("rows", rows)
        }
        request(RetrofitUtils.mApiService.cartGoodsList(map), object : BaseSingleObserver<CartGoodsListBean>() {
            override fun doOnSuccess(list: CartGoodsListBean) {
                getView()?.showNormal()
                getView()?.getShopCartListSuccess(list)
            }

            override fun doOnFailure(list: CartGoodsListBean) {
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


}
