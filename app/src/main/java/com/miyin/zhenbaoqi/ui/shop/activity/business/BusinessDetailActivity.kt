package com.miyin.zhenbaoqi.ui.shop.activity.business

import android.app.Activity
import android.os.Bundle
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.ArticleBean
import com.miyin.zhenbaoqi.ui.shop.contract.BusinessDetailContract
import com.miyin.zhenbaoqi.ui.shop.presenter.BusinessDetailPresenter
import kotlinx.android.synthetic.main.activity_business_detail.*

class BusinessDetailActivity : BaseMvpActivity<BusinessDetailContract.IView, BusinessDetailContract.IPresenter>(), BusinessDetailContract.IView {

    private var mBean: ArticleBean.ArticleListBean? = null

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as ArticleBean.ArticleListBean?
        return R.layout.activity_business_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("活动详情")
        immersionBar { statusBarDarkFont(true) }

        mBean?.run {
            tv_name.text = arti_name
            tv_date_time.text = mod_date
            tv_content.text = arti_profile

            val isApply = mBean?.is_apply!!
            if (isApply) {
                tv_commit.text = "取消报名"
            } else {
                tv_commit.text = "立即报名"
            }
        }

        tv_commit.setOnClickListener {
            val isApply = mBean?.is_apply!!
            if (isApply) {
                mPresenter?.cancelActivity(mBean?.activity_id!!)
            } else {
                mPresenter?.joinActivity(mBean?.activity_id!!)
            }
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = BusinessDetailPresenter()

    override fun joinActivitySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun cancelActivitySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
