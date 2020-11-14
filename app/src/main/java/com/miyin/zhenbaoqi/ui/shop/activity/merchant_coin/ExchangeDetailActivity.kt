package com.miyin.zhenbaoqi.ui.shop.activity.merchant_coin

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_exchange_detail.*

class ExchangeDetailActivity : BaseActivity() {

    private var mTag: String? = null
    private var mTopContent: String? = null
    private var mBottomContent: String? = null

    override fun getContentView(): Int {
        mTag = intent.getStringExtra("tag")
        return R.layout.activity_exchange_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("兑换详情")
        immersionBar { statusBarDarkFont(true) }

        iv_cover.loadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3854897203,3668738056&fm=26&gp=0.jpg")
        tv_name.text = "免费的卖家名额"
        tv_price.text = "5000"
        tv_desc.text = "1、免费卖家名额仅兑换给自己的普通粉丝或者专属粉丝；\n" +
                "2、输入对方的ID和手机号，系统验证匹配之后方可兑换；\n" +
                "3、兑换成功后，会成功绑定成为自己底下的卖家，但兑换者不会获得邀请代金券和成长值\n" +
                "4.有疑问咨询客服电话0571-86503352。"

        et_top.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mTopContent = editable.toString().trim { it <= ' ' }
            }
        })
        et_bottom.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mBottomContent = editable.toString().trim { it <= ' ' }
            }
        })

        setOnClickListener(btn_commit)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_commit -> {
            }
        }
    }

}
