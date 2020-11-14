package com.miyin.zhenbaoqi.ui.shopcart.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.shopcart.contract.ShopCartContract
import com.miyin.zhenbaoqi.ui.shopcart.presenter.ShopCartPresenter
import kotlinx.android.synthetic.main.fragment_shop_cart.*

class ShopCartFragment : BaseListFragment<ShopCartContract.IView, ShopCartContract.IPresenter>(), ShopCartContract.IView {

    private var mOnTabSelectCallback:OnTabSelectCallback? = null
    companion object {
        fun newInstance() = ShopCartFragment()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback

    }
    override fun useImmersionBar() = true


    override fun getContentView() = R.layout.fragment_shop_cart

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mImmersionBar.titleBar(title_bar).init()

    }

    override fun initData() {
    }

    override fun createPresenter() = ShopCartPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override   fun reload() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }





}
