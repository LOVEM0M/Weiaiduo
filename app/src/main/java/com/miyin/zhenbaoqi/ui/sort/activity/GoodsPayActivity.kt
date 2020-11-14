package com.miyin.zhenbaoqi.ui.sort.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BasePayActivity
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.bean.PayResultBean
import com.miyin.zhenbaoqi.bean.ServiceAmountBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.activity.address.AddressActivity
import com.miyin.zhenbaoqi.ui.sort.contract.GoodsPayContract
import com.miyin.zhenbaoqi.ui.sort.dialog.CouponDialog
import com.miyin.zhenbaoqi.ui.sort.dialog.PayDialog
import com.miyin.zhenbaoqi.ui.sort.presenter.GoodsPayPresenter
import com.miyin.zhenbaoqi.utils.DensityUtils
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_goods_pay.*

@SuppressLint("SetTextI18n")
class GoodsPayActivity : BasePayActivity<GoodsPayContract.IView, GoodsPayContract.IPresent>(), GoodsPayContract.IView, OnDialogCallback {

    private var mPrice = 0L
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mFrom = "general"
    private var mRemark: String? = null
    private var mFormatPrice = ""
    private var mCouponPrice = 0
    private var mAddressId = 0
    private var mGoodsId = 2
    private var mCouponId = 0
    private var mIsChecked = false
    private var mOrderNumber: String? = null
    private var mList = mutableListOf<CouponBean.ListBean>()
    private var mServiceAmount = 0
    private var mOriginAmount = 0
    private var mGoodsFreight = 0
    private var mCount = 1
    private var mShopName: String? = null
    private var mIsSeven = false

    override fun getContentView(): Int {
        with(intent) {
            mPrice = getLongExtra("price", 0)
            mGoodsImg = getStringExtra("goods_img")
            mGoodsName = getStringExtra("goods_name")
            mGoodsId = getIntExtra("goods_id", 0)
            mFrom = getStringExtra("from") ?: ""
            mOrderNumber = getStringExtra("order_number")
            mGoodsFreight = getIntExtra("goods_freight", 0)
            mCount = getIntExtra("count", 1)
            mShopName = getStringExtra("shop_name")
            mIsSeven = getBooleanExtra("is_seven", true)
        }
        return R.layout.activity_goods_pay
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("订单详情")
        immersionBar {
            statusBarDarkFont(true)
            keyboardEnable(true)
        }

        if (!mOrderNumber.isNullOrEmpty() && mOrderNumber != "0") {
            gone(ll_container)
        }

        mFormatPrice = FormatUtils.formatNumber((mPrice * mCount + mServiceAmount - mCouponPrice) / 100f + mGoodsFreight)
        setBottomTotalPrice()

        tv_shop_name.text = mShopName
        val goodsImg = when {
            mGoodsImg.isNullOrEmpty() -> ""
            mGoodsImg!!.contains(",") -> mGoodsImg!!.split(",")[0]
            else -> mGoodsImg
        }
        tv_count.text = "X$mCount"

        val goodsPrice = FormatUtils.formatNumber(mPrice * mCount / 100f)
        iv_cover.loadImg(goodsImg)
        tv_goods_name.text = mGoodsName
        tv_price.text = "¥${FormatUtils.formatNumber(mPrice / 100f)}"

        tv_goods_total_price.text = SpanUtils()
                .append("共${mCount}件\u3000")
                .append("小计\u3000").setForegroundColor(Color.BLACK).setFontSize(12, true).setBold()
                .append("¥$goodsPrice").setForegroundColor(Color.parseColor("#D3371B")).setFontSize(16, true).setBold()
                .create()
        tv_goods_price.text = "¥$goodsPrice"
        tv_coupon_price.text = "¥${FormatUtils.formatNumber(mCouponPrice / 100f)}"
        tv_freight.text = "¥${FormatUtils.formatNumber(mGoodsFreight / 1f)}"
        tv_extra_price.text = "¥${FormatUtils.formatNumber(mServiceAmount / 100f)}"

        tv_label_first.text = (if (mIsSeven) "" else "不") + "支持7天无理由退货"
        tv_label_second.text = if (mGoodsFreight == 0) "包邮" else "邮费5元"

        et_remark.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mRemark = editable.toString().trim { it <= ' ' }
            }
        })
        switch_button.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                mIsChecked = isChecked
                if (!isChecked) {
                    AlertDialog.Builder(this@GoodsPayActivity)
                            .setTitle("温馨提示")
                            .setCancelable(false)
                            .setMessage("取消鉴宝服务，平台将不再提供假一赔三担保服务。")
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                mFormatPrice = FormatUtils.formatNumber((mPrice * mCount - mCouponPrice) / 100f + mGoodsFreight)

                                tv_extra_price.text = "¥0.00"
                                setBottomTotalPrice()

                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                                view?.isChecked = true
                                dialog.dismiss()
                            }
                            .show()
                } else {
                    mFormatPrice = FormatUtils.formatNumber((mPrice * mCount + mServiceAmount - mCouponPrice) / 100f + mGoodsFreight)

                    tv_extra_price.text = "¥${FormatUtils.formatNumber(mServiceAmount / 100f)}"
                    setBottomTotalPrice()
                }
            }
        })

        if (null != Constant.VIDEO_VIEW) {
            visible(fl_container)

            val effectiveHeight = DensityUtils.getScreenHeight() - ImmersionBar.getNavigationBarHeight(this)
            val containerWidth = DensityUtils.getScreenWidth()
            val containerHeight = effectiveHeight - ImmersionBar.getStatusBarHeight(this) - getDimensionPixelSize(R.dimen.dp_52)

            val videoWidth = containerWidth / 3
            val videoHeight = containerHeight / 3
            fl_container.layoutParams = FrameLayout.LayoutParams(videoWidth, videoHeight)

            val dragWidth = containerWidth - videoWidth
            val dragHeight = containerHeight - videoHeight

            var downX = 0f
            var downY = 0f
            fl_container.setOnTouchListener { v, event ->
                val x = event.x
                val y = event.y
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = x
                        downY = y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dragX = v.x + x - downX
                        val dragY = v.y + y - downY

                        val moveX = if (dragX < 0) 0f else if (dragX > dragWidth) dragWidth.toFloat() else dragX
                        val moveY = if (dragY < 0) 0f else if (dragY > dragHeight) dragHeight.toFloat() else dragY

                        v.translationX = moveX
                        v.translationY = moveY
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    }
                }
                true
            }
        }

        setOnClickListener(cl_container, fl_coupon, tv_pay)
    }

    override fun initData() {
        mPresenter?.getAddressList()
        mPresenter?.getCouponAvailableList(mGoodsId)
        mPresenter?.getAmountRule()
    }

    override fun createPresenter() = GoodsPayPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.run {
                val bean = getSerializableExtra("bean") as AddressBean.UserAddressBean
                with(bean) {
                    gone(tv_add_address)

                    mAddressId = ads_id
                    tv_user_info.text = consignee
                    tv_user_phone.text = phone_no
                    tv_address.text = "$pcc_name$address"
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_container -> startActivityForResult<AddressActivity>(Constant.INTENT_REQUEST_CODE)
            R.id.fl_coupon -> {
                val dialog = CouponDialog.newInstance(mList)
                dialog.show(supportFragmentManager, "coupon")
            }
            R.id.tv_pay -> {
                val dialog = PayDialog.newInstance(mFormatPrice)
                dialog.show(supportFragmentManager, "pay")
            }
        }
    }

    override fun onAliPaySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onWXPaySuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun setBottomTotalPrice() {
        tv_pay_price.text = SpanUtils()
                .append("共${mCount}件，\u3000")
                .append("合计:\u3000").setFontSize(16, true).setForegroundColor(Color.BLACK).setBold()
                .append("¥${mFormatPrice}").setForegroundColor(ContextCompat.getColor(this, R.color.theme_dark_purple))
                .setFontSize(16, true).setBold()
                .create()
    }

    override fun getCouponAvailableListSuccess(bean: CouponBean) {
        mList = bean.list!!.toMutableList()

        if (mList.isEmpty()) {
            tv_coupon.hint = "暂无可用优惠券"
            fl_coupon.isEnabled = false
        }
    }

    override fun getAddressListSuccess(bean: AddressBean) {
        gone(tv_add_address)

        bean.userAddress?.forEach {
            if (it.is_default == 0) {
                mAddressId = it.ads_id

                tv_user_info.text = it.consignee
                tv_user_phone.text = it.phone_no
                tv_address.text = "${it.pcc_name}${it.address}"
            }
        }
        if (mAddressId == 0) {
            val addressBean = bean.userAddress?.get(0)!!
            mAddressId = addressBean.ads_id

            tv_user_info.text = addressBean.consignee
            tv_user_phone.text = addressBean.phone_no
            tv_address.text = "${addressBean.pcc_name}${addressBean.address}"
        }
    }

    override fun getAmountRuleSuccess(bean: ServiceAmountBean) {
        bean.data?.run {
            mServiceAmount = if (special_price.isNullOrEmpty()) {
                0
            } else {
                special_price!!.toInt()
            }
            mOriginAmount = if (original_price.isNullOrEmpty()) {
                0
            } else {
                original_price!!.toInt()
            }

            tv_special_price.text = "${mServiceAmount / 100}"
            tv_origin_price.text = "¥" + mOriginAmount / 100
            tv_origin_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun goodsPaySuccess(bean: PayResultBean, payType: Int) {
        when (payType) {
            1 -> {

            }
            2 -> {
                val orderString = bean.alipayBody
                if (orderString.isNullOrEmpty()) {
                    showToast("支付宝错误")
                    return
                }
                onAliCallback(orderString)
            }
            3 -> {
                onWXCallback(bean)
            }
        }
    }

    override fun onFailure(flag: Int) {
        if (flag == 0) {
            AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("您还没有添加过收货地址，请去添加。")
                    .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        startActivityForResult<AddressActivity>(Constant.INTENT_REQUEST_CODE)
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
        } else if (flag == 1) {
            tv_coupon.hint = "暂无可用优惠券"
            fl_coupon.isEnabled = false
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            if (obj == "pay") {
                if (mFrom == "general") {
                    mPresenter?.goodsPay(mAddressId, mCouponId, mGoodsId, flag, mRemark, if (switch_button.isChecked) mServiceAmount else 0, mCount)
                } else if (mFrom == "auction") {
                    if (mOrderNumber.isNullOrEmpty() || mOrderNumber == "0") {
                        mPresenter?.auctionGoodsPay(mAddressId, mCouponId, mGoodsId, flag, mRemark, if (switch_button.isChecked) mServiceAmount else 0)
                    } else {
                        mPresenter?.auctionGoodsWaitPay(mOrderNumber!!, flag)
                    }
                }
            }
        } else if (obj is CouponBean.ListBean) {
            mCouponId = obj.coupons_id
            mCouponPrice = obj.coupons_amount.toInt()
            tv_coupon.text = SpanUtils()
                    .append(obj.coupons_name ?: "")
                    .append("  -" + FormatUtils.formatNumber(mCouponPrice / 100f) + " 元")
                    .setForegroundColor(ContextCompat.getColor(this, R.color.theme_dark_purple))
                    .create()

            val serviceAmount = if (switch_button.isChecked) mServiceAmount else 0
            mFormatPrice = FormatUtils.formatNumber((mPrice * mCount - mCouponPrice + serviceAmount) / 100f + mGoodsFreight)
            tv_coupon_price.text = "¥${FormatUtils.formatNumber(obj.coupons_amount / 100f)}"
            setBottomTotalPrice()
        }
    }

}