package com.miyin.weiaiduo.ui.vip.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.miyin.weiaiduo.ui.vip.contract.VipContract
import com.miyin.weiaiduo.ui.vip.presenter.VipPresenter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback

class VipFragment : BaseMvpFragment<VipContract.IView, VipContract.IPresenter>(), VipContract.IView {
    private var mOnTabSelectCallback: OnTabSelectCallback? = null
    private var mBannerList = mutableListOf<String>()

    companion object {
        fun newInstance() = VipFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback

    }

    override fun useImmersionBar() = true

    override fun getContentView() = R.layout.fragment_vip

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)

        setOnClickListener()
    }

    override fun initData() {
    }

    override fun createPresenter() = VipPresenter()


}

