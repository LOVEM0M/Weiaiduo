package com.miyin.zhenbaoqi.ui.sort.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.MerchantEvalBean
import com.miyin.zhenbaoqi.ui.sort.contract.ShopEvaluateContract
import com.miyin.zhenbaoqi.ui.sort.presenter.ShopEvaluatePresenter
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_shop_evaluate.*

class ShopEvaluateActivity : BaseMvpActivity<ShopEvaluateContract.IView, ShopEvaluateContract.IPresenter>(), ShopEvaluateContract.IView {

    private var mMerchantId = 0

    override fun getContentView(): Int {
        mMerchantId = intent.getIntExtra("merchant_id", 0)
        return R.layout.activity_shop_evaluate
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("店铺评价")
        immersionBar { statusBarDarkFont(true) }

        flow_layout_eval.setAdapter(object : TagAdapter(listOf("1", "2", "3")) {
            override fun getView(parent: FlowLayout, position: Int, data: Any) = getTextView(data.toString())
        })
    }

    override fun initData() {
        mPresenter?.getShopEvalList(mMerchantId)
    }

    override fun createPresenter() = ShopEvaluatePresenter()

    private fun getTextView(title: String) = TextView(applicationContext).apply {
        val params = FrameLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_110), resources.getDimensionPixelSize(R.dimen.dp_25)).apply {
            leftMargin = resources.getDimensionPixelSize(R.dimen.dp_10)
            bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_10)
        }
        layoutParams = params
        gravity = Gravity.CENTER
        text = title
        setTextColor(DrawableCreator.Builder()
                .setCheckedTextColor(0xFFC33A3A.toInt())
                .setUnCheckedTextColor(0xFF9E9E9E.toInt())
                .buildTextColor())
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_12))
        background = DrawableCreator.Builder()
                .setCheckedSolidColor(0xFFF9E7E3.toInt(), 0xFFF8F8F8.toInt())
                .setCornersRadius(resources.getDimension(R.dimen.dp_3))
                .build()
    }

    override fun getShopEvalListSuccess(bean: MerchantEvalBean) {
        bean.list?.run {
            flow_layout_most.setAdapter(object : TagAdapter(this) {
                override fun getView(parent: FlowLayout, position: Int, data: Any) = getTextView((data as MerchantEvalBean.ListBean).evaluation_label!! + data.label_size)
            })
        }
    }

}
