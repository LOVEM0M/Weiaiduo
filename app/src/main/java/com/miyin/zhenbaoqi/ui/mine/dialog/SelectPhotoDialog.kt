package com.miyin.zhenbaoqi.ui.mine.dialog

import android.app.Activity
import android.app.Dialog
import android.view.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.GlideCacheEngine
import com.miyin.zhenbaoqi.utils.GlideEngine
import kotlinx.android.synthetic.main.dialog_select_photo.view.*

class SelectPhotoDialog(private val mContext: Activity) {

    private var mDialog: Dialog? = null
    private var mSelectNum = 1
    private var mType = PictureMimeType.ofImage()
    private var mRecordTime = 15

    fun builder(): SelectPhotoDialog {
        val view = View.inflate(mContext, R.layout.dialog_select_photo, null).apply {
            tv_take_photo.setOnClickListener {
                takePhoto()
                dismiss()
            }
            tv_open_album.setOnClickListener {
                openAlbum()
                dismiss()
            }
            tv_cancel.setOnClickListener { dismiss() }
        }

        mDialog = Dialog(mContext, R.style.SelectPhotoDialogStyle).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(view)
        }
        return this
    }

    fun setSelectNum(selectNum: Int): SelectPhotoDialog {
        mSelectNum = if (selectNum < 1) 1 else selectNum
        return this
    }

    fun setType(type: Int): SelectPhotoDialog {
        mType = type
        return this
    }

    fun setRecordTime(recordTime: Int): SelectPhotoDialog {
        mRecordTime = recordTime
        return this
    }

    fun show() {
        mDialog?.show()

        mDialog?.window?.run {
            setWindowAnimations(R.style.DialogAnimation)

            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
            attributes = params
        }
    }

    private fun dismiss() {
        mDialog?.dismiss()
    }

    private fun takePhoto() {
        PictureSelector.create(mContext)
                .openCamera(mType)
                .selectionMode(PictureConfig.SINGLE)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .compress(true)
                .videoQuality(0)
                .recordVideoSecond(mRecordTime)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun openAlbum() {
        PictureSelector.create(mContext)
                .openGallery(mType)
                .loadImageEngine(GlideEngine.createGlideEngine())
//                .theme(R.style.PictureWhiteStyle)
                .imageSpanCount(3)
                .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())
                .isAndroidQTransform(false)
                .selectionMode(if (mSelectNum > 1) PictureConfig.MULTIPLE else PictureConfig.SINGLE)
                .compress(true)
                .maxSelectNum(mSelectNum)
                .videoMaxSecond(mRecordTime)
                .recordVideoSecond(mRecordTime)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

}
