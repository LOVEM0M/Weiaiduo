package com.miyin.zhenbaoqi.ui.sort.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.ArrayMap
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.LiveEntryRoomBean
import com.miyin.zhenbaoqi.bean.MerchantInfoBean
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.ui.sort.contract.MerchantMessageContract
import com.miyin.zhenbaoqi.ui.sort.presenter.MerchantMessagePresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_merchant_auth.*
import org.greenrobot.eventbus.EventBus

class MerchantAuthActivity : BaseMvpActivity<MerchantMessageContract.IView, MerchantMessageContract.IPresenter>(), MerchantMessageContract.IView {

    private var mBean: MerchantInfoBean.DataBean? = null
    private var mMerchantState = 1
    private var mMerchantId = 0

    override fun getContentView(): Int {
        with(intent) {
            mBean = getSerializableExtra("bean") as MerchantInfoBean.DataBean
            mMerchantId = getIntExtra("merchant_id", 0)
            mMerchantState = getIntExtra("merchant_state", 1)
        }
        return R.layout.activity_merchant_auth
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("认证信息")
        immersionBar { statusBarDarkFont(true) }

        mBean?.run {
            val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
            iv_cover.transform(head_img, transform)
            tv_name.text = merchants_name
            tv_desc.text = merchants_subtitle
            val formatNumber = FormatUtils.formatNumber(quality_retention_money / 100f)
            tv_info.text = "个人认证\u3000|\u3000质保金：$formatNumber\u3000|\u3000关注数：${focus_number}"
            if (quality_retention_money > 0) {
                visible(iv_vip)
            } else {
                invisible(iv_vip)
            }

            tv_shop_evaluate.text = SpanUtils()
                    .append("店铺综合评分：")
                    .append(merchants_grade ?: "0.0")
                    .setForegroundColor(Color.parseColor("#D8494B"))
                    .create()
            val describeGrade = describe_grade?.toFloat()!!
            tv_shop_desc_scope.text = describe_grade
            pb_shop_desc.progress = (describeGrade * 100).toInt()
            tv_shop_desc_state.text = when {
                describeGrade <= 3 -> "一般"
                describeGrade > 3 && describeGrade <= 4 -> "好"
                describeGrade > 4 && describeGrade <= 4.5 -> "极好"
                else -> "非常好"
            }

            val serviceGrade = service_grade?.toFloat()!!
            tv_service_scope.text = service_grade
            pb_service.progress = (serviceGrade * 100).toInt()
            tv_service_state.text = when {
                describeGrade <= 3 -> "一般"
                describeGrade > 3 && describeGrade <= 4 -> "好"
                describeGrade > 4 && describeGrade <= 4.5 -> "极好"
                else -> "非常好"
            }

            val logisticsGrade = logistics_grade?.toFloat()!!
            tv_logistics_scope.text = logistics_grade
            pb_logistics.progress = (logisticsGrade * 100).toInt()
            tv_logistics_state.text = when {
                describeGrade <= 3 -> "一般"
                describeGrade > 3 && describeGrade <= 4 -> "好"
                describeGrade > 4 && describeGrade <= 4.5 -> "极好"
                else -> "非常好"
            }

            tv_price.text = "该商家已缴纳品质保证金${formatNumber}元，该资金将会用来保障平台用户利益"

            updateMerchantStateSuccess(mMerchantState)
        }

        tv_shop_attention.setOnClickListener {
            mPresenter?.updateMerchantState(mMerchantState, mMerchantId)
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = MerchantMessagePresenter()

    override fun getMerchantInfoSuccess(bean: ResponseBean) {

    }

    override fun updateMerchantStateSuccess(focusState: Int) {
        mMerchantState = focusState
        if (focusState == 0) {
            tv_shop_attention.text = "已关注"
            tv_shop_attention.isSelected = true
        } else {
            tv_shop_attention.text = "+ 关注"
            tv_shop_attention.isSelected = false
        }
        val arrayMap = ArrayMap<String, Any>().apply {
            put("title", "update_merchant_state")
            put("merchant_state", focusState)
        }
        EventBus.getDefault().post(arrayMap)
    }

    override fun liveRoomEntrySuccess(bean: LiveEntryRoomBean) {

    }

}
