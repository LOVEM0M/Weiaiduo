package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_select_after_sale_type.*
import kotlinx.android.synthetic.main.layout_order_goods_info.*

class SelectAfterSaleTypeActivity : BaseActivity() {

    private var mOrderNumber: String? = null
    private var mName: String? = null
    private var mPhone: String? = null
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mGoodsPrice = 0L
    private var mGoodsDesc: String? = null
    private var mCount = 1
    private var mIsSeven = 0
    private var mGoodsFreight = 0

    override fun getContentView(): Int {
        with(intent) {
            mName = getStringExtra("name")
            mPhone = getStringExtra("phone")
            mOrderNumber = getStringExtra("order_number")
            mGoodsImg = getStringExtra("goods_img")
            mGoodsName = getStringExtra("goods_name")
            mGoodsPrice = getLongExtra("goods_price", 0)
            mCount = getIntExtra("count", 1)
            mGoodsDesc = getStringExtra("goods_desc")
            mIsSeven = getIntExtra("is_seven", 0)
            mGoodsFreight = getIntExtra("goods_freight", 0)
        }
        return R.layout.activity_select_after_sale_type
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("选择服务类型")
        immersionBar { statusBarDarkFont(true) }

        iv_cover.loadImg(mGoodsImg)
        tv_goods_name.text = mGoodsName
        tv_goods_price.text = "¥${FormatUtils.formatNumber(mGoodsPrice  )}"
        tv_goods_desc.text = mGoodsDesc
        tv_label_first.text = if (mIsSeven == 0) "支持7天无理由退货" else "不支持7天无理由退货"
        tv_label_second.text = if (mGoodsFreight == 0) "包邮" else "邮费5元"
        tv_count.text = "x$mCount"

        setOnClickListener(fl_refund, fl_return_goods)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        var type = 1
        when (v?.id) {
            R.id.fl_refund -> {
                type = 2
            }
            R.id.fl_return_goods -> {
                type = 1
            }
        }
        startActivityForResult<AfterSaleActivity>(Constant.INTENT_REQUEST_CODE, "type" to type, "name" to mName,
                "phone" to mPhone, "order_number" to mOrderNumber, "goods_img" to mGoodsImg, "goods_name" to mGoodsName,
                "goods_desc" to mGoodsDesc, "goods_price" to mGoodsPrice, "count" to mCount, "is_seven" to mIsSeven,
                "goods_freight" to mGoodsFreight)
    }

}
