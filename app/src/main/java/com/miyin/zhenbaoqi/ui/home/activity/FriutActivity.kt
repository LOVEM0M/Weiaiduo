package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.home.contract.FriutContract
import com.miyin.zhenbaoqi.ui.home.presenter.FriutPresenter
import kotlinx.android.synthetic.main.activity_search.title_bar


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class FriutActivity:BaseMvpActivity<FriutContract.IView, FriutContract.IPresenter>(), FriutContract.IView{
    private var mName: String? = null

    companion object {
        fun newInstance() = FriutActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()

//        setOnClickListener(ll_goback)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.ll_goback -> finish()
        }
    }
    override fun getContentView(): Int {
        return R.layout.activity_friut
    }

    override fun initData() {

    }

    override fun createPresenter() = FriutPresenter()

}