package com.miyin.zhenbaoqi.ui.shop.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.ArrayMap
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.adapter.OperateImageAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.OperateGoodsContract
import com.miyin.zhenbaoqi.ui.shop.dialog.DelayBidDialog
import com.miyin.zhenbaoqi.ui.shop.dialog.GoodsTypeDialog
import com.miyin.zhenbaoqi.ui.shop.presenter.OperateGoodsPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.JSONUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import com.miyin.zhenbaoqi.widget.AddedView
import com.miyin.zhenbaoqi.widget.SwitchButton
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.fragment_operate_goods.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class OperateGoodsFragment : BaseMvpFragment<OperateGoodsContract.IView, OperateGoodsContract.IPresenter>(), OperateGoodsContract.IView, OnDialogCallback {

    private var mTag: String? = null
    private var mGoodsId = 0
    private var mRoomId = 0
    private var mIsLive = true
    private var mBean: LiveGoodsBean.LiveGoodsDataBean? = null
    private var mGoodsName: String? = null
    private var mGoodsAmount = 0L
    private var mGoodsAddAmount = 0L
    private var mRemark: String? = null
    private var mGoodsFright = 0
    private var mIsSeven = 0
    private var mGoodsImg: String? = null
    private var mInventory = 1
    private var mStartTime = 0L
    private var mEndTime = 0L
    private val mList = mutableListOf<String>()
    private var mAdapter: OperateImageAdapter? = null
    private val mCompressPathList = mutableListOf<String>()
    private var mTimePickerView: TimePickerView? = null
    private var mTimeFlag = 0
    private var mExamplePrice = 0L
    private var mGoodsTypeDialog: GoodsTypeDialog? = null
    private var mCateId1 = 0
    private var mCate1Name: String? = null
    private var mCateId2 = 0
    private var mCate2Name: String? = null
    private var mCateId3 = 0
    private var mCate3Name: String? = null
    private var mIsRestriction = 1

    companion object {
        fun newInstance(tag: String, roomId: Int, bean: LiveGoodsBean.LiveGoodsDataBean?, isLive: Boolean) = OperateGoodsFragment().apply {
            arguments = Bundle().apply {
                putString("tag", tag)
                putInt("room_id", roomId)
                putBoolean("is_live", isLive)
                putSerializable("bean", bean)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mTag = getString("tag")
            mRoomId = getInt("room_id")
            mIsLive = getBoolean("is_live")
            mBean = getSerializable("bean") as LiveGoodsBean.LiveGoodsDataBean?
        }
        return R.layout.fragment_operate_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        if (TextUtils.equals("秒杀", mTag)) {
            tv_price.text = "价格"
            visible(fl_stock, fl_limit_buy)
            gone(fl_add_price, fl_start_time, fl_end_time, fl_example_price, fl_goods_type, ll_container)

            if (null != mBean) {
                mBean?.run {
                    mGoodsId = goods_id
                    mGoodsImg = goods_img
                    mGoodsName = goods_name
                    mGoodsAmount = goods_amount
                    mInventory = inventory
                    mIsSeven = is_seven
                    mGoodsFright = goods_freight
                    mIsRestriction = is_restriction
                    mRemark = goods_describe

                    mList.add(mGoodsImg!!)
                    et_name.setText(mGoodsName)
                    et_price.setText(FormatUtils.formatNumber(mGoodsAmount / 100f))
                    rb_seven.isChecked = mIsSeven == 0
                    rb_free_shipping.isChecked = mGoodsFright == 0
                    sb_limit_buy.isChecked = mIsRestriction == 0
                    et_remark.setText(mRemark)
                }
            }
        } else if (TextUtils.equals("拍卖", mTag)) {
            tv_price.text = "起拍价"
            gone(fl_stock, fl_goods_type, fl_start_time, ll_container, fl_limit_buy)
            visible(fl_add_price, fl_end_time, fl_example_price)
        }

        recycler_view.run {
            mAdapter = OperateImageAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            mAdapter?.onItemChildClickListener = mOnItemChildClickListener
        }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mGoodsName = editable.toString().trim { it <= ' ' }
            }
        })
        et_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                val price = editable.toString().trim { it <= ' ' }
                mGoodsAmount = if (price.isEmpty()) {
                    0L
                } else {
                    (price.toDouble().toLong()) * 100L
                }
            }
        })
        et_add_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                val addPrice = editable.toString().trim { it <= ' ' }
                mGoodsAddAmount = if (addPrice.isEmpty()) {
                    0L
                } else {
                    (addPrice.toDouble().toLong()) * 100L
                }
            }
        })
        et_example_price.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                val examplePrice = editable.toString().trim { it <= ' ' }
                mExamplePrice = if (examplePrice.isEmpty()) {
                    0L
                } else {
                    (examplePrice.toDouble()).toLong() * 100L
                }
            }
        })
        et_remark.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mRemark = editable.toString().trim { it <= ' ' }
            }
        })

        rb_seven.setOnCheckedChangeListener { _, checkedId ->
            mIsSeven = if (checkedId) 0 else 1
        }
        rb_free_shipping.setOnCheckedChangeListener { _, checkedId ->
            mGoodsFright = if (checkedId) 0 else 5
        }
        sb_limit_buy.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                mIsRestriction = if (isChecked) 0 else 1
            }
        })

        stock_count.run {
            setMaxNumber(1000 * 1000)
            setOnNumberChangedListener(object : AddedView.OnNumberChangedListener {
                override fun onNumberChanged(number: Int) {
                    mInventory = number
                }
            })
        }

        initTimeDialog()
        setOnClickListener(iv_cover, fl_start_time, fl_end_time, btn_commit, fl_goods_type)
    }

    private val mOnItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, _ ->
        when (view.id) {
            R.id.iv_cover_close -> {
                mList.clear()
                mGoodsImg = ""
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = OperateGoodsPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                PictureSelector.obtainMultipleResult(data).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath
                        mCompressPathList.add(path)
                    }
                }

                showDialog("图片正在上传...", false)
                uploadImg()
            }
        }
    }

    private fun uploadImg() {
        if (mCompressPathList.isEmpty()) {
            hideDialog()
            PictureFileUtils.deleteAllCacheDirFile(context)
            mAdapter?.notifyDataSetChanged()
            mGoodsImg = if (mList.isEmpty()) {
                ""
            } else {
                val sb = StringBuilder()
                mList.forEach {
                    sb.append(it).append(",")
                }
                sb.substring(0, sb.length - 1)
            }
            return
        }
        mPresenter?.uploadImage(mCompressPathList[0])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_cover -> {
                if (mList.size >= 1) {
                    showToast("最多只能选择1张照片")
                    return
                }
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(activity!!).builder().setSelectNum(3 - mList.size).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.fl_start_time -> {
                mTimeFlag = 0
                mTimePickerView?.show()
            }
            R.id.fl_end_time -> {
                val dialog = DelayBidDialog.newInstance()
                dialog.setOnDialogCallback(this)
                dialog.show(fragmentManager!!, "delayBid")
            }
            R.id.btn_commit -> {
                val remark = mRemark ?: ""
                if (TextUtils.equals("秒杀", mTag)) {
                    showDialog("正在发布秒杀商品...", false)
                    mPresenter?.liveGoodsSpikeInsert(mGoodsAmount, remark, mGoodsFright, mGoodsImg,
                            mGoodsName, mInventory, mIsSeven, mRoomId, mIsRestriction, mGoodsId)
                } else if (TextUtils.equals("拍卖", mTag)) {
                    if (!mIsLive) {
                        showToast("请进入直播间发布竞拍商品")
                        return
                    }
                    if (mGoodsName.isNullOrEmpty()) {
                        showToast("请填写商品名称")
                        return
                    }
                    if (mGoodsImg.isNullOrEmpty()) {
                        showToast("请上传商品图片")
                        return
                    }
                    if (mRemark.isNullOrEmpty()) {
                        showToast("请填写商品详情")
                        return
                    }
                    showDialog("正在发布拍卖商品...", false)
                    mStartTime = System.currentTimeMillis()
                    val endTime = mStartTime + mEndTime
                    mPresenter?.liveGoodsAuctionInsert(mGoodsAddAmount, mCateId1, mCateId2, mCateId3, 15, endTime, 0, mGoodsAmount,
                            mRemark!!, mGoodsFright, mGoodsImg!!, mGoodsName!!, mExamplePrice, "", mIsSeven, 2, mRoomId, mGoodsAmount, mStartTime)
                }
            }
            R.id.fl_goods_type -> {
                if (null != mGoodsTypeDialog) {
                    mGoodsTypeDialog?.show(fragmentManager!!, "goodsType")
                } else {
                    mPresenter?.getParentList("goods_category")
                }
            }
        }
    }

    private fun initTimeDialog() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.set(2050, 11, 31)

        mTimePickerView = TimePickerBuilder(activity) { date, _ ->
            if (mTimeFlag == 0) {
                mStartTime = date.time
                val startTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm")
                tv_start_time.text = startTime
            } else {
                mEndTime = date.time
                val endTime = TimeUtils.date2String(date, "yyyy-MM-dd HH:mm")
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

    override fun uploadImageSuccess(path: String) {
        mCompressPathList.removeAt(0)
        if (path.isNotEmpty()) {
            mList.add(path)
            mAdapter?.notifyDataSetChanged()
        }
        uploadImg()
    }

    override fun liveGoodsAuctionInsertSuccess(bean: GoodsDetailBean) {
        val arrayMap = ArrayMap<String, String>().apply {
            put("type", "auction")
            if (mIsLive) {
                bean.data?.goods?.is_live = "0"
            } else {
                bean.data?.goods?.is_live = "1"
            }
            put("data", JSONUtils.gson.toJson(bean.data?.goods))
        }
        EventBus.getDefault().post(arrayMap)
        activity?.finish()
    }

    override fun liveGoodsSpikeInsertSuccess() {
        if (null != mBean) {
            EventBus.getDefault().post("refreshCommonGoods")
        }
        activity?.finish()
    }

    override fun getParentListSuccess(list: List<CityBean.CityListBean>) {
        mGoodsTypeDialog = GoodsTypeDialog.newInstance(list)
        mGoodsTypeDialog!!.show(fragmentManager!!, "goodsType")
        mGoodsTypeDialog!!.setOnDialogCallback(this)
    }

    override fun getSonListSuccess(list: List<CityBean.CityListBean>, type: Int) {
        if (type == 2) {
            mGoodsTypeDialog?.setRightList(list)
        } else if (type == 3) {
            if (list.isNotEmpty()) {
                visible(ll_container)
                flow_layout.setAdapter(object : TagAdapter(list) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(context).apply {
                        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, context?.getDimensionPixelSize(R.dimen.dp_25)!!).apply {
                            rightMargin = context?.getDimensionPixelSize(R.dimen.dp_15)!!
                            bottomMargin = context?.getDimensionPixelSize(R.dimen.dp_12)!!
                        }
                        gravity = Gravity.CENTER
                        setPadding(context?.getDimensionPixelSize(R.dimen.dp_10)!!, 0, context?.getDimensionPixelSize(R.dimen.dp_10)!!, 0)
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, context?.getDimension(R.dimen.sp_12)!!)
                        setTextColor(DrawableCreator.Builder()
                                .setCheckedTextColor(Color.WHITE)
                                .setUnCheckedTextColor(Color.BLACK)
                                .buildTextColor())
                        text = (data as CityBean.CityListBean).code_name
                        background = DrawableCreator.Builder()
                                .setStrokeWidth(context?.getDimension(R.dimen.dp_1)!!)
                                .setCheckedStrokeColor(0xFFF1352F.toInt(), 0xFF5F5F5F.toInt())
                                .setCheckedSolidColor(0xFFF1352F.toInt(), Color.WHITE)
                                .setCornersRadius(context?.getDimension(R.dimen.dp_3)!!)
                                .build()
                    }

                    override fun onSelected(position: Int, view: View?) {
                        val data = list[position]
                        mCateId3 = data.dict_id
                        mCate3Name = data.code_name
                    }
                })
            } else {
                gone(ll_container)
            }
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is CityBean.CityListBean) {
            if (flag == 0) {
                mCateId1 = obj.dict_id
                mCate1Name = obj.code_name

                mPresenter?.getSonList(mCateId1, 2)

                tv_goods_type.text = ""
            } else if (flag == 1) {
                mCateId2 = obj.dict_id
                mCate2Name = obj.code_name

                mPresenter?.getSonList(mCateId2, 3)

                tv_goods_type.text = "$mCate1Name-$mCate2Name"
            }
        } else if (obj is String) {
            if (obj == "seconds") {
                mEndTime = flag * 60 * 1000L
                tv_end_time.text = "${flag}分钟"
            }
        }
    }

}
