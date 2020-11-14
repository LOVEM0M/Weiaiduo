package com.miyin.zhenbaoqi.ui.message.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.message.contract.ReportContract
import com.miyin.zhenbaoqi.ui.message.presenter.ReportPresenter
import com.miyin.zhenbaoqi.ui.mine.dialog.AfterSaleDialog
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : BaseMvpActivity<ReportContract.IView, ReportContract.IPresenter>(), ReportContract.IView, OnDialogCallback {

    private var mContent: String? = null
    private var mCate: String? = null
    private var mMerchantId = 0
    private var mPath: String? = null

    override fun getContentView(): Int {
        mMerchantId = intent.getIntExtra("merchant_id", 0)
        return R.layout.activity_report
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("举报商家")
        immersionBar { statusBarDarkFont(true) }

        et_content.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }
                setBtnEnabled()
            }
        })

        setOnClickListener(fl_type, iv_cover, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = ReportPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                val list = PictureSelector.obtainMultipleResult(this)
                list.forEach {
                    if (it.isCompressed) {
                        mPresenter?.uploadImage(it.compressPath)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_type -> {
                val list = listOf("商家客服态度差", "售卖假货", "售卖违禁品", "成交不卖", "虚假发货", "超时发货", "运费到付", "涉嫌引导添加微信", "宣传与平台无关广告信息")
                val dialog = AfterSaleDialog.newInstance(list)
                dialog.show(supportFragmentManager, "complaint")
            }
            R.id.iv_cover -> {
                SelectPhotoDialog(this).builder().setSelectNum(1).setType(PictureMimeType.ofImage()).show()
            }
            R.id.btn_commit -> {
                mPresenter?.report(mMerchantId, mContent, mPath ?: "", mCate)
            }
        }
    }

    private fun setBtnEnabled() {
        val enabled = (!mContent.isNullOrEmpty() && !mCate.isNullOrEmpty())
        btn_commit.isEnabled = enabled
    }

    override fun uploadImageSuccess(url: String) {
        mPath = url
        iv_cover.loadImg(url)
    }

    override fun reportSuccess() {
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            mCate = obj
            tv_type.text = obj
            setBtnEnabled()
        }
    }

}
