package com.miyin.zhenbaoqi.ui.mine.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ui.mine.adapter.PhotoAdapter
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_feedback.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.tools.PictureFileUtils
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ui.mine.contract.FeedbackContract
import com.miyin.zhenbaoqi.ui.mine.presenter.FeedbackPresenter
import kotlinx.android.synthetic.main.layout_add_photo.*
import java.lang.StringBuilder

class FeedbackActivity : BaseMvpActivity<FeedbackContract.IView, FeedbackContract.IPresenter>(), FeedbackContract.IView {

    private var mContent: String? = null
    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: PhotoAdapter
    private val mCompressPathList = mutableListOf<String>()

    override fun getContentView() = R.layout.activity_feedback

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("意见反馈")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            mAdapter = PhotoAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(applicationContext, 3)

            mAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.iv_delete) {
                    mList.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        et_content.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }

                btn_commit.isEnabled = !TextUtils.isEmpty(mContent)
            }
        })
        setOnClickListener(iv_add_photo, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = FeedbackPresenter()

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
            PictureFileUtils.deleteAllCacheDirFile(this)
            mAdapter.notifyDataSetChanged()
            return
        }
        mPresenter?.uploadImage(mCompressPathList[0])
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_add_photo -> {
                if (mList.size >= 3) {
                    showToast("最多只能选择3张照片")
                    return
                }
                requestPermission("请开启相机和文件读写权限", object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@FeedbackActivity).builder().setSelectNum(3 - mList.size).show()
                    }

                    override fun onDenied() {
                        showToast("请开启相机和文件读写权限")
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.btn_commit -> {
                val path = if (mList.isEmpty()) {
                    ""
                } else {
                    val sb = StringBuilder()
                    mList.forEach {
                        sb.append(it).append(",")
                    }
                    sb.substring(0, sb.length - 1)
                }

                mPresenter?.feedback(mContent, path)
            }
        }
    }

    override fun uploadImgSuccess(path: String) {
        mCompressPathList.removeAt(0)
        if (path.isNotEmpty()) {
            mList.add(path)
        }
        uploadImg()
    }

    override fun feedbackSuccess() {
        showToast("提交成功")
        finish()
    }

}
