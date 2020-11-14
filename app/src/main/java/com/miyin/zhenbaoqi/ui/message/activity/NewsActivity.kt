package com.miyin.zhenbaoqi.ui.message.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.home.presenter.BargainPresenter
import com.miyin.zhenbaoqi.ui.message.contract.NewsContract
import com.miyin.zhenbaoqi.ui.message.presenter.NewsPresenter
import kotlinx.android.synthetic.main.activity_news.*


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class NewsActivity:BaseMvpActivity<NewsContract.IView, NewsContract.IPresenter>(), NewsContract.IView{
    private var mName: String? = null

    companion object {
        fun newInstance() = NewsActivity()
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
        return R.layout.activity_news
    }

    override fun initData() {

    }

    override fun createPresenter() = NewsPresenter()

}