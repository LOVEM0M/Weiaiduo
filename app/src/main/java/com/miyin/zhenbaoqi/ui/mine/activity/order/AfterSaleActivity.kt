package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.mine.adapter.PhotoAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.AfterSaleContract
import com.miyin.zhenbaoqi.ui.mine.dialog.AfterSaleDialog
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.AfterSalePresenter
import com.miyin.zhenbaoqi.utils.FormatUtils
import kotlinx.android.synthetic.main.activity_after_sale.*
import kotlinx.android.synthetic.main.layout_add_photo.*
import kotlinx.android.synthetic.main.layout_order_goods_info.*

class AfterSaleActivity : BaseMvpActivity<AfterSaleContract.IView, AfterSaleContract.IPresenter>(), AfterSaleContract.IView, OnDialogCallback {

    private var mList = mutableListOf<String>()
    private var mCompressList = mutableListOf<String>()
    private lateinit var mAdapter: PhotoAdapter
    private var mSelectType = 1
    private var mReason: String? = null
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
            mSelectType = getIntExtra("type", 1)
            mGoodsImg = getStringExtra("goods_img")
            mGoodsName = getStringExtra("goods_name")
            mGoodsPrice = getLongExtra("goods_price", 0)
            mCount = getIntExtra("count", 1)
            mGoodsDesc = getStringExtra("goods_desc")
            mIsSeven = getIntExtra("is_seven", 0)
            mGoodsFreight = getIntExtra("goods_freight", 0)
        }
        return R.layout.activity_after_sale
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("申请售后")
        immersionBar { statusBarDarkFont(true) }

        tv_name.text = mName
        tv_phone.text = mPhone

        iv_cover.loadImg(mGoodsImg)
        tv_goods_name.text = mGoodsName
        tv_goods_price.text = "¥${FormatUtils.formatNumber(mGoodsPrice  )}"
        tv_goods_desc.text = mGoodsDesc
        tv_label_first.text = if (mIsSeven == 0) "支持7天无理由退货" else "不支持7天无理由退货"
        tv_label_second.text = if (mGoodsFreight == 0) "包邮" else "邮费5元"
        tv_count.text = "x$mCount"

        recycler_view.run {
            mAdapter = PhotoAdapter(mList)
            adapter = mAdapter
            layoutManager = GridLayoutManager(applicationContext, 3)

            mAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.iv_delete) {
                    mList.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        setOnClickListener( fl_after_sale_reason, iv_add_photo, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = AfterSalePresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath

                        mCompressList.add(path)
                    }
                }
                uploadImage()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_after_sale_reason -> {
                val dialog = AfterSaleDialog.newInstance(listOf("7天无理由退货", "商品质量问题", "商品错发", "收到商品破损", "收到商品与实际不符", "缺乏鉴别证书", "商品瑕疵", "其他"))
                dialog.show(supportFragmentManager, "afterSale")
            }
            R.id.iv_add_photo -> {
                if (mList.size >= 3) {
                    showToast("最多选择3张图片")
                    return
                }
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@AfterSaleActivity).builder().setSelectNum(3 - mList.size).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.btn_commit -> {
                val sb = StringBuilder()
                if (mList.isNotEmpty()) {
                    mList.forEach {
                        sb.append(it).append(",")
                    }
                }
                var image = ""
                if (sb.isNotEmpty()) {
                    image = sb.substring(0, sb.length - 1)
                }
                mPresenter?.afterSaleLaunch(image, mSelectType, mReason, mOrderNumber!!)
            }
        }
    }

    private fun uploadImage() {
        showDialog("图片正在上传...")
        if (mCompressList.isEmpty()) {
            hideDialog()
            mAdapter.notifyDataSetChanged()
            PictureFileUtils.deleteAllCacheDirFile(this)
            return
        }
        mPresenter?.uploadImage(mCompressList[0])
    }

    override fun uploadImgSuccess(url: String) {
        mCompressList.removeAt(0)
        mList.add(url)

        uploadImage()
    }

    override fun afterSaleLaunchSuccess() {
        finish()
    }

    override fun onFailure(msg: String, type: Int) {
        showToast(msg)
        if (type == 0) {
            mCompressList.removeAt(0)
            uploadImage()
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        mReason = obj.toString()
        tv_after_sale_reason.text = mReason
    }

}
