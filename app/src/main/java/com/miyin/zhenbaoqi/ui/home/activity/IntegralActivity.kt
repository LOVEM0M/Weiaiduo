package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.home.contract.IntegralStoreContract
import com.miyin.zhenbaoqi.ui.home.presenter.IntegralStorePresenter
import kotlinx.android.synthetic.main.activity_integral_store.*


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class IntegralActivity: com.miyin.zhenbaoqi.base.activity.BaseMvpActivity<IntegralStoreContract.IView, IntegralStoreContract.IPresenter>(), IntegralStoreContract.IView{

    companion object {
        fun newInstance() = IntegralActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        setOnClickListener(ll_goback)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_goback -> finish()
        }
    }
    override fun getContentView(): Int {
        return R.layout.activity_integral_store
    }

    override fun initData() {

    }

    override fun createPresenter() = IntegralStorePresenter()

}