package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.ui.home.contract.SearchContract
import com.miyin.zhenbaoqi.ui.home.contract.SnacksContract
import com.miyin.zhenbaoqi.ui.home.fragment.HomeFragment
import com.miyin.zhenbaoqi.ui.home.presenter.SnacksPresenter
import com.tencent.qcloud.tim.uikit.base.BaseActvity
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_popular_snacks.*
import kotlinx.android.synthetic.main.fragment_popular_snacks.ll_search_home
import kotlinx.android.synthetic.main.item_home_classify4.*

@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class SnacksActivity: com.miyin.zhenbaoqi.base.activity.BaseMvpActivity<SnacksContract.IView, SnacksContract.IPresenter>(), SnacksContract.IView{
    private var mName: String? = null

    companion object {
        fun newInstance() = SnacksActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        tv_top_title.setText(mName)
        setOnClickListener(ll_goback)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_goback -> finish()
        }
    }
    override fun getContentView(): Int {
        mName = intent.getStringExtra("title")
        return R.layout.fragment_popular_snacks
    }

    override fun initData() {

    }

    override fun createPresenter() = SnacksPresenter()

}