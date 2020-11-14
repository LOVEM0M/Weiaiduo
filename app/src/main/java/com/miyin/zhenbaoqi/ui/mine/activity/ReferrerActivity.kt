package com.miyin.zhenbaoqi.ui.mine.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.ReferrerBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.message.activity.OnlineCustomerActivity
import com.miyin.zhenbaoqi.utils.MaxImageView
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_referrer.*

@SuppressLint("SetTextI18n")
class ReferrerActivity : BaseActivity() {

    private var mBean: ReferrerBean? = null

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as ReferrerBean?
        return R.layout.activity_referrer
    }

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("我的推荐人")
        immersionBar { statusBarDarkFont(true) }

        mBean?.data?.run {
            tv_merchant.text = "商家id：$merchants_id"
            tv_merchant_name.text = "店铺的名字：$merchants_name"
            tv_merchant_wx.text = "微信号：$wechat_id"
            iv_wx_qr_code.loadImg(wechat_image)
        }

        setOnClickListener(tv_private_message, iv_wx_qr_code)
    }

    override fun initData() {

    }

    override fun onDestroy() {
        MaxImageView.clear()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_private_message -> {
                mBean?.data?.run {
                    val title = if (merchants_name.isNullOrEmpty()) user_id.toString() else merchants_name!!
                    startActivity<OnlineCustomerActivity>("user_id" to user_id, "title" to title)
                }
            }
            R.id.iv_wx_qr_code -> {
                val wechatImage = mBean?.data?.wechat_image
                if (wechatImage.isNullOrEmpty()) {
                    ToastUtils.showToast("暂无微信二维码图片")
                    return
                }
                MaxImageView.singleImageView(this@ReferrerActivity, wechatImage)
            }
        }
    }

}
