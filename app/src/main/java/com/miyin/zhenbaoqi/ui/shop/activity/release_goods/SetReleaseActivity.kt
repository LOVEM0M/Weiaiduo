package com.miyin.zhenbaoqi.ui.shop.activity.release_goods

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.common.SingleDataDialog
import com.miyin.zhenbaoqi.ui.shop.contract.SetReleaseContract
import com.miyin.zhenbaoqi.ui.shop.presenter.SetReleasePresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.miyin.zhenbaoqi.widget.AddedView
import com.miyin.zhenbaoqi.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_set_release.*
import java.util.*

class SetReleaseActivity : BaseMvpActivity<SetReleaseContract.IView, SetReleaseContract.IPresenter>(),
        SetReleaseContract.IView, OnDialogCallback {

    private var mGoodsId = 0
    private var mCateId1 = 0
    private var mCateId2 = 0
    private var mCateId3 = 0
    private var mCommissionRatio = 15
    private var mEndTime = 0L
    private var mOriginalPrice: String? = null
    private var mPrice: String? = null
    private var mGoodsDescribe: String? = null
    private var mGoodsFreight = 5
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mGoodsType = 1
    private var mGoodsVideo: String? = null
    private var mInventory = 1
    private var mIsRestriction = 1
    private var mIsSeven = 0
    private var mBean: GoodsDetailBean? = null
    private var mRoomId = 0
    private val mCommissionRatioList = mutableListOf<String>()
    private var mTimePickerView: TimePickerView? = null
    private var mSize: String? = null
    private var mMaterial: String? = null
    private var mOriginPlace: String? = null
    private var mWeight: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mGoodsName = getStringExtra("name")
            mGoodsDescribe = getStringExtra("content")
            mCateId1 = getIntExtra("cate_id1", 0)
            mCateId2 = getIntExtra("cate_id2", 0)
            mCateId3 = getIntExtra("cate_id3", 0)
            mGoodsImg = getStringExtra("photo_url")
            mGoodsVideo = getStringExtra("video_url")
            mBean = getSerializableExtra("bean") as GoodsDetailBean?
            mRoomId = getIntExtra("room_id", 0)
            mSize = getStringExtra("size")
            mMaterial = getStringExtra("material")
            mOriginPlace = getStringExtra("origin_place")
            mWeight = getStringExtra("weight")
        }
        return R.layout.activity_set_release
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("发布设置")
        immersionBar { statusBarDarkFont(true) }

        mBean?.data?.goods?.run {
            mGoodsId = goods_id
            mOriginalPrice = FormatUtils.formatNumber(goods_original_amount / 100f)
            mPrice = FormatUtils.formatNumber(goods_amount / 100f)
            mGoodsFreight = goods_freight
            mIsSeven = is_seven

            et_original_price.setText(mOriginalPrice)
            et_price.setText(mPrice)
            rb_country.isChecked = (goods_freight == 0)
            rb_return.isChecked = (is_seven == 0)
        }

        (3..20).forEach { mCommissionRatioList.add("$it%") }

        et_original_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mOriginalPrice = editable.toString().trim { it <= ' ' }
            }
        })
        et_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPrice = editable.toString().trim { it <= ' ' }
            }
        })

        rb_country.setOnCheckedChangeListener { _, isChecked ->
            mGoodsFreight = if (isChecked) {
                0
            } else {
                5
            }
        }
        rb_return.setOnCheckedChangeListener { _, isChecked ->
            mIsSeven = if (isChecked) {
                0
            } else {
                1
            }
        }
        rg_type.setOnCheckedChangeListener { _, i ->
            if (i == R.id.rb_forever) {
                mEndTime = 0
                tv_limit_time.text = ""
                gone(fl_limit_time)
                mGoodsType = 1
            } else if (i == R.id.rb_limit) {
                mGoodsType = 2
                visible(fl_limit_time)
            }
        }

        ll_stock.run {
            setMaxNumber(1000 * 1000)
            setOnNumberChangedListener(object : AddedView.OnNumberChangedListener {
                override fun onNumberChanged(number: Int) {
                    mInventory = number
                }
            })
        }
        switch_button.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                mIsRestriction = if (isChecked) 0 else 1
            }
        })
        setOnClickListener(fl_limit_time, btn_commit)

        initTimeDialog()
    }

    override fun initData() {

    }

    override fun createPresenter() = SetReleasePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_brokerage_price -> {
                val dialog = SingleDataDialog.newInstance(mCommissionRatioList)
                dialog.show(supportFragmentManager, "commissionRatio")
            }
            R.id.fl_limit_time -> {
                mTimePickerView?.show()
            }
            R.id.btn_commit -> {
                if (mSize.isNullOrEmpty()) {
                    showToast("请输入尺寸")
                    return
                }
                if (mMaterial.isNullOrEmpty()) {
                    showToast("请输入材质")
                    return
                }
                if (mOriginPlace.isNullOrEmpty()) {
                    showToast("请输入产地")
                    return
                }
                if (mWeight.isNullOrEmpty()) {
                    showToast("请输入重量")
                    return
                }

                mPresenter?.addMerchantGoods(mCateId1, mCateId2, mCateId3, mCommissionRatio, mEndTime, mPrice,
                        mGoodsDescribe, mGoodsFreight, mGoodsImg, mGoodsName, mOriginalPrice, mGoodsType,
                        mGoodsVideo, mInventory, mIsRestriction, mIsSeven, mGoodsId, mSize!!, mMaterial!!,
                        mOriginPlace!!, mWeight!!)
            }
        }
    }

    override fun addMerchantGoodsSuccess() {
        showToast("发布成功")

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun initTimeDialog() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.set(2050, 11, 31)

        mTimePickerView = TimePickerBuilder(this) { date, _ ->
            mEndTime = date.time
            val endTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm:ss")
            tv_limit_time.text = endTime
        }.setType(booleanArrayOf(true, true, true, true, true, true))
                .setCancelText(getString(R.string.cancel))
                .setSubmitText(getString(R.string.confirm))
                .setTitleSize(15)
                .setOutSideCancelable(true)
                .setSubmitColor(Color.parseColor("#333333"))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setBgColor(Color.WHITE)
                .setDate(startDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .isDialog(true)
                .build()

        mTimePickerView?.dialog?.let {
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)

            params.leftMargin = 0
            params.rightMargin = 0
            mTimePickerView!!.dialogContainerLayout.layoutParams = params

            it.window
        }?.let {
            val params = it.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = params

            it.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            it.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        mCommissionRatio = obj.toString().substring(0, obj.toString().length - 1).toInt()
        tv_brokerage_price.text = obj.toString()

        if (mCommissionRatio < 10) {
            AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("因为分佣比例小于10%，发布商品不能在平台显示。")
                    .setPositiveButton(getString(R.string.confirm)) { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
        }
    }

}
