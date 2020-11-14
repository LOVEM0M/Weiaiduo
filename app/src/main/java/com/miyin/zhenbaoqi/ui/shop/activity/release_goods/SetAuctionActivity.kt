package com.miyin.zhenbaoqi.ui.shop.activity.release_goods

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
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
import com.miyin.zhenbaoqi.bean.DraftAuctionGoodsBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.shop.contract.SetAuctionContract
import com.miyin.zhenbaoqi.ui.shop.dialog.AuctionOptionDialog
import com.miyin.zhenbaoqi.ui.shop.presenter.SetAuctionPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_set_auction.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class SetAuctionActivity : BaseMvpActivity<SetAuctionContract.IView, SetAuctionContract.IPresenter>(),
        SetAuctionContract.IView, OnDialogCallback, AuctionOptionDialog.OnSelectTimeCallback {

    private var mCateId1 = 0
    private var mCate1Name: String? = null
    private var mCateId2 = 0
    private var mCate2Name: String? = null
    private var mCateId3 = 0
    private var mCate3Name: String? = null
    private var mCommissionRatio = 15
    private var mStartTime = 0L
    private var mEndTime = 0L
    private var mGoodsDescribe: String? = null
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mGoodsVideo: String? = null
    private var mGoodsFreight = 5
    private var mIsSeven = 0
    private var mTimePickerView: TimePickerView? = null
    private var mInitPrice: String = "0"
    private var mAddPrice: String = "0"
    private var mExamplePrice: String = "0"
    private var mEnsureAmount = 0
    private var mTimeFlag = 0
    private var mDraftAuctionGoodsBean: DraftAuctionGoodsBean? = null
    private var mSize: String? = null
    private var mMaterial: String? = null
    private var mOriginPlace: String? = null
    private var mWeight: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mGoodsName = getStringExtra("name")
            mGoodsDescribe = getStringExtra("content")
            mCateId1 = getIntExtra("cate_id1", 0)
            mCate1Name = getStringExtra("cate_name1")
            mCateId2 = getIntExtra("cate_id2", 0)
            mCate2Name = getStringExtra("cate_name2")
            mCateId3 = getIntExtra("cate_id3", 0)
            mCate3Name = getStringExtra("cate_name3")
            mGoodsImg = getStringExtra("photo_url")
            mGoodsVideo = getStringExtra("video_url")
            mDraftAuctionGoodsBean = getSerializableExtra("bean") as DraftAuctionGoodsBean?
            mSize = getStringExtra("size")
            mMaterial = getStringExtra("material")
            mOriginPlace = getStringExtra("origin_place")
            mWeight = getStringExtra("weight")
        }
        return R.layout.activity_set_auction
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        if (null == mDraftAuctionGoodsBean) {
            initTitleBar("拍品设置", rightTitle = "保存草稿")
        } else {
            initTitleBar("拍品设置")
        }
        immersionBar { statusBarDarkFont(true) }

        initTimeDialog()

        mDraftAuctionGoodsBean?.run {
            mGoodsFreight = goodsFreight
            mIsSeven = isSeven
            mInitPrice = FormatUtils.formatNumber(initPrice / 100f)
            mAddPrice = FormatUtils.formatNumber(addPrice / 100f)
            mExamplePrice = FormatUtils.formatNumber(referencePrice / 100f)
            mEnsureAmount = ensurePrice.toInt() / 100

            cb_country.isChecked = (goodsFreight == 0)
            cb_return.isChecked = (isSeven == 0)
            et_init_price.setText(FormatUtils.formatNumber(initPrice / 100f))
            et_add_price.setText(FormatUtils.formatNumber(addPrice / 100f))
            et_example_price.setText(FormatUtils.formatNumber(referencePrice / 100f))
            tv_security_deposit.text = mEnsureAmount.toString()
        }

        cb_country.setOnCheckedChangeListener { _, isChecked ->
            mGoodsFreight = if (isChecked) {
                0
            } else {
                5
            }
        }
        cb_return.setOnCheckedChangeListener { _, isChecked ->
            mIsSeven = if (isChecked) {
                0
            } else {
                1
            }
        }

        et_init_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mInitPrice = editable.toString().trim { it <= ' ' }
            }
        })
        et_add_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mAddPrice = editable.toString().trim { it <= ' ' }
            }
        })
        et_example_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mExamplePrice = editable.toString().trim { it <= ' ' }
            }
        })

        setOnClickListener(fl_start_time, fl_end_time, fl_security_deposit, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = SetAuctionPresenter()

    override fun onRightClick() {
        val bean = DraftAuctionGoodsBean().apply {
            goodsName = mGoodsName
            goodsDesc = mGoodsDescribe
            cate1Id = mCateId1
            cate1Name = mCate1Name
            cate2Id = mCateId2
            cate2Name = mCate2Name
            cate3Id = mCateId3
            cate3Name = mCate3Name
            goodsImg = mGoodsImg
            goodsVideo = mGoodsVideo
            initPrice = (mInitPrice.toDouble() * 100).toLong()
            addPrice = (mAddPrice.toDouble() * 100).toLong()
            referencePrice = (mExamplePrice.toDouble() * 100).toLong()
            goodsFreight = mGoodsFreight
            isSeven = mIsSeven
            ensurePrice = mEnsureAmount * 100L
        }
        val json = JSONUtils.gson.toJson(bean)
        Logger.d("json == $json")
        mPresenter?.saveAuctionGoods(json)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_start_time -> {
                mTimeFlag = 0
                mTimePickerView?.show()
            }
            R.id.fl_end_time -> {
                mTimeFlag = 1
                mTimePickerView?.show()
//                val dialog = AuctionOptionDialog.newInstance("end_time")
//                dialog.show(supportFragmentManager, "endTime")
            }
            R.id.fl_security_deposit -> {
                val dialog = AuctionOptionDialog.newInstance("security_deposit")
                dialog.show(supportFragmentManager, "securityDeposit")
            }
            R.id.btn_commit -> {
                val addPrice = if (mAddPrice.isNullOrEmpty()) {
                    0L
                } else {
                    (mAddPrice.toDouble() * 100L).toLong()
                }
                val initPrice = if (mInitPrice.isNullOrEmpty()) {
                    0L
                } else {
                    (mInitPrice.toDouble() * 100L).toLong()
                }
                val examplePrice = if (mExamplePrice.isNullOrEmpty()) {
                    0L
                } else {
                    (mExamplePrice.toDouble() * 100L).toLong()
                }
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

                showDialog("正在发布拍品...")
                mPresenter?.addAuctionGoods(addPrice, mCateId1, mCateId2, mCateId3, mCommissionRatio, mEndTime,
                        mEnsureAmount * 100, initPrice, mGoodsDescribe, mGoodsFreight, mGoodsImg,
                        mGoodsName, examplePrice, mGoodsVideo, mIsSeven, initPrice, mStartTime, mSize!!, mMaterial!!,
                        mOriginPlace!!, mWeight!!)
            }
        }
    }

    private fun initTimeDialog() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.set(2050, 11, 31)

        mTimePickerView = TimePickerBuilder(this) { date, _ ->
            if (mTimeFlag == 0) {
                mStartTime = date.time
                val startTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm")
                tv_start_time.text = startTime
            } else {
                mEndTime = date.time
                val endTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm")
                if (mEndTime - mStartTime > 48 * 60 * 60 * 1000 || mEndTime <= mStartTime) {
                    AlertDialog.Builder(this@SetAuctionActivity)
                            .setTitle("温馨提示")
                            .setMessage("截止时间应在开始时间之后48小时内")
                            .setPositiveButton(getString(R.string.confirm), null)
                            .show()
                    return@TimePickerBuilder
                }

                tv_end_time.text = endTime
            }
        }.setType(booleanArrayOf(true, true, true, true, true, false))
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

    override fun addAuctionGoodsSuccess() {
        showToast("发布成功")

        if (null != mDraftAuctionGoodsBean) {
            EventBus.getDefault().post("deleteDraft")
        }

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun saveAuctionGoodsSuccess() {
        showToast("保存成功")

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onFailure(msg: String, flag: Int) {
        if (null == mDraftAuctionGoodsBean) {
            val bean = DraftAuctionGoodsBean().apply {
                goodsName = mGoodsName
                goodsDesc = mGoodsDescribe
                cate1Id = mCateId1
                cate1Name = mCate1Name
                cate2Id = mCateId2
                cate2Name = mCate2Name
                cate3Id = mCateId3
                cate3Name = mCate3Name
                goodsImg = mGoodsImg
                goodsVideo = mGoodsVideo
                initPrice = (mInitPrice.toDouble() * 100).toLong()
                addPrice = (mAddPrice.toDouble() * 100).toLong()
                referencePrice = (mExamplePrice.toDouble() * 100).toLong()
                goodsFreight = mGoodsFreight
                isSeven = mIsSeven
                ensurePrice = mEnsureAmount * 100L
                size = mSize
                material = mMaterial
                originPlace = mOriginPlace
                weight = mWeight
            }
            val json = JSONUtils.gson.toJson(bean)
            mPresenter?.saveAuctionGoods(json)
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            when (obj) {
                "security_deposit" -> {
                    mEnsureAmount = flag
                    tv_security_deposit.text = "$flag"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSelectTime(timestamp: Int, type: Int) {
        when (type) {
            1 -> {
                mEndTime = mStartTime + timestamp
                val minute = timestamp / 1000 / 60
                tv_end_time.text = "${minute}分钟"
            }
            2 -> {
                mEndTime = mStartTime + timestamp
                val hour = timestamp / 1000 / 60 / 60
                tv_end_time.text = "${hour}小时"
            }
            else -> {

            }
        }
    }

}
