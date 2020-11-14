package com.miyin.zhenbaoqi.ui.shop.activity.sub_account

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.SubAccountInfoBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.shop.contract.SubAccountManagerContract
import com.miyin.zhenbaoqi.ui.shop.presenter.SubAccountManagerPresenter
import kotlinx.android.synthetic.main.activity_sub_account_manager.*

class SubAccountManagerActivity : BaseMvpActivity<SubAccountManagerContract.IView, SubAccountManagerContract.IPresenter>(), SubAccountManagerContract.IView {

    private val mList = mutableListOf<SubAccountInfoBean.DataBean>()

    override fun getContentView() = R.layout.activity_sub_account_manager

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("子账号管理")
        immersionBar { statusBarDarkFont(true) }

        setOnClickListener(cl_chat_container, cl_ship_container, cl_goods_container)
    }

    override fun initData() {
        mPresenter?.getSubAccountInfo()
    }

    override fun createPresenter() = SubAccountManagerPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            initData()
        }
    }

    override fun onClick(v: View?) {
        var state = 0
        when (v?.id) {
            R.id.cl_chat_container -> {
                state = 0
                startActivityForResult<SubAccountSettingActivity>(Constant.INTENT_REQUEST_CODE, "bean" to mList.firstOrNull { it.type == 0 }, "state" to state)
            }
            R.id.cl_ship_container -> {
                state = 1
                startActivityForResult<SubAccountSettingActivity>(Constant.INTENT_REQUEST_CODE, "bean" to mList.firstOrNull { it.type == 1 }, "state" to state)
            }
            R.id.cl_goods_container -> {
                state = 2
                startActivityForResult<SubAccountSettingActivity>(Constant.INTENT_REQUEST_CODE, "bean" to mList.firstOrNull { it.type == 2 }, "state" to state)
            }
        }
    }

    override fun getSubAccountInfoSuccess(bean: SubAccountInfoBean) {
        bean.data?.let { it ->
            mList.clear()
            mList.addAll(it)

            it.forEach {
                when (it.type) {
                    0 -> {
                        tv_chat_account.text = it.account_name
                        tv_chat_account.setTextColor(Color.parseColor("#878B96"))
                    }
                    1 -> {
                        tv_ship_account.text = it.account_name
                        tv_ship_account.setTextColor(Color.parseColor("#878B96"))
                    }
                    2 -> {
                        tv_goods_account.text = it.account_name
                        tv_goods_account.setTextColor(Color.parseColor("#878B96"))
                    }
                }
            }
        }
    }

}
