package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.OrderEvalBean
import com.miyin.zhenbaoqi.bean.OrderEvalListBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.loadImgAll
import com.miyin.zhenbaoqi.ui.mine.contract.GoodsEvalContract
import com.miyin.zhenbaoqi.ui.mine.presenter.GoodsEvalPresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_goods_eval.*

@SuppressLint("SetTextI18n")
class GoodsEvalActivity : BaseMvpActivity<GoodsEvalContract.IView, GoodsEvalContract.IPresenter>(), GoodsEvalContract.IView {

    private var mOrderNumber: String? = null
    private var mEvalList = mutableListOf<OrderEvalListBean.DictBean>()
    private var mStarDesc = 0
    private var mStarService = 0
    private var mStarLogistics = 0

    override fun getContentView(): Int {
        mOrderNumber = intent.getStringExtra("order_number")
        return R.layout.activity_goods_eval
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("用户评价")
        immersionBar { statusBarDarkFont(true) }

        btn_commit.setOnClickListener {
            val sb = StringBuilder()
            flow_layout.selectedList.forEach {
                sb.append(mEvalList[it].code_name).append(",")
            }
            var content = ""
            if (sb.isNotEmpty()) {
                content = sb.substring(0, sb.length - 1)
            }
            val id = "描述相符_$mStarDesc,服务态度_$mStarService,物流服务_$mStarLogistics"
            mPresenter?.commitGoodsEval(content, id, mOrderNumber!!)
        }

        star_desc.setEnables(true)
        star_desc.setIntegerMark(true)
        star_desc.setOnStarChangeListener {
            mStarDesc = it.toInt()
            tv_desc_score.text = "${it.toInt()}分"
        }
        star_service.setEnables(true)
        star_service.setIntegerMark(true)
        star_service.setOnStarChangeListener {
            mStarService = it.toInt()
            tv_service.text = "${it.toInt()}分"
        }
        star_logistics.setEnables(true)
        star_logistics.setIntegerMark(true)
        star_logistics.setOnStarChangeListener {
            mStarLogistics = it.toInt()
            tv_logistics.text = "${it.toInt()}分"
        }
    }

    override fun initData() {
        mPresenter?.getGoodsEval(mOrderNumber!!)
    }

    override fun createPresenter() = GoodsEvalPresenter()

    override fun getGoodsEvalSuccess(bean: OrderEvalListBean) {
        with(bean) {
            val transform = RoundCornersTransform(getDimension(R.dimen.dp_5), RoundCornersTransform.CornerType.ALL)
            iv_avatar.loadImgAll(head_img, R.drawable.ic_merchant_header_default, transform)
            tv_name.text = if (merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_name
            tv_desc.text = if (merchants_subtitle.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_subtitle

            iv_cover.loadImg(if (goods_img!!.contains(",")) goods_img!!.split(",")[0] else goods_img)
            tv_goods_name.text = goods_name
            tv_price.text = "¥${FormatUtils.formatNumber(order_amount / 100f)}"
            tv_count.text = "数量x$pay_number"

            dicts?.run {
                mEvalList.addAll(this)
                flow_layout.setAdapter(object : TagAdapter(this) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any): View {
                        return TextView(applicationContext).apply {
                            layoutParams = FrameLayout.LayoutParams(context.getDimensionPixelSize(R.dimen.dp_110), context.getDimensionPixelSize(R.dimen.dp_25)).apply {
                                leftMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                                topMargin = context.getDimensionPixelSize(R.dimen.dp_10)
                            }
                            gravity = Gravity.CENTER
                            setTextColor(DrawableCreator.Builder()
                                    .setCheckedTextColor(0xFFC33A3A.toInt())
                                    .setUnCheckedTextColor(0xFF9E9E9E.toInt())
                                    .buildTextColor())
                            text = (data as OrderEvalListBean.DictBean).code_name
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_13))
                            background = DrawableCreator.Builder()
                                    .setCheckedSolidColor(0xFFF9E7E3.toInt(), 0xFFF8F8F8.toInt())
                                    .setCornersRadius(context.getDimension(R.dimen.dp_3))
                                    .build()
                        }
                    }
                })
            }
        }
    }

    override fun commitGoodsEvalSuccess(bean: OrderEvalBean) {
        finish()
    }

}
