@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.ui.mine.activity

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BasePayActivity
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.contract.BecomeMerchantContract
import com.miyin.zhenbaoqi.ui.mine.dialog.OrderPayDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.BecomeMerchantPresenter
import com.miyin.zhenbaoqi.utils.GlideApp
import kotlinx.android.synthetic.main.activity_become_merchant.*

class BecomeMerchantActivity : BasePayActivity<BecomeMerchantContract.IView, BecomeMerchantContract.IPresenter>(),
        BecomeMerchantContract.IView, OnDialogCallback {

    override fun getContentView(): Int {
        return R.layout.activity_become_merchant
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("成为商家")
        immersionBar { statusBarDarkFont(true) }

        GlideApp.with(this).asBitmap().load("${Constant.BASE_URL}zhen_url/image/setUpStore.png")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val bitWidth = resource.width
                        val bitHeight = resource.height
                        val bitScaleW = bitHeight / bitWidth

                        val manager = this@BecomeMerchantActivity.windowManager
                        val outMetrics = DisplayMetrics()
                        manager.defaultDisplay.getMetrics(outMetrics)
                        val imgWidth = outMetrics.widthPixels
                        val params = iv_cover.layoutParams
                        params.width = imgWidth
                        params.height = (imgWidth * bitScaleW)

                        iv_cover.scaleType = ImageView.ScaleType.CENTER_CROP
                        iv_cover.adjustViewBounds = true
                        iv_cover.layoutParams = params
                        iv_cover.setImageBitmap(resource)
                    }
                })

        btn_commit.setOnClickListener {
            val dialog = OrderPayDialog.newInstance()
            dialog.show(supportFragmentManager, "orderPay")
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = BecomeMerchantPresenter()

    override fun applyMerchantSuccess(bean: PayResultBean, payType: Int) {
        if (payType == 1) {

        } else if (payType == 2) {
            onAliCallback(bean.alipayBody!!)
        } else if (payType == 3) {
            onWXCallback(bean)
        }
    }

    override fun onFailure() {
        startActivity<VerifiedActivity>()
    }

    override fun onAliPaySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onWXPaySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        mPresenter?.applyMerchant(flag)
    }

}
