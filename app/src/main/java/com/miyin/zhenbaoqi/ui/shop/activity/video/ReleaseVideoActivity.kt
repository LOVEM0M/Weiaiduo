package com.miyin.zhenbaoqi.ui.shop.activity.video

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import android.text.Editable
import android.util.ArrayMap
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.hw.videoprocessor.VideoProcessor
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.bean.HomeVideoBean
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.ReleaseVideoContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ReleaseVideoPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.miyin.zhenbaoqi.widget.flow_layout.TagFlowLayout
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.activity_release_video.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import kotlin.concurrent.thread

class ReleaseVideoActivity : BaseMvpActivity<ReleaseVideoContract.IView, ReleaseVideoContract.IPresenter>(), ReleaseVideoContract.IView {

    private var mPath: String? = null
    private var mContent: String? = null
    private var mType = 0
    private var mBean: HomeVideoBean.DataBean? = null
    private var mVideoId = 0

    override fun getContentView(): Int {
        mBean = intent.getSerializableExtra("bean") as HomeVideoBean.DataBean?
        return R.layout.activity_release_video
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("发布视频")
        immersionBar { statusBarDarkFont(true) }

        et_content.addTextChangedListener((object : EditWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }
                if (mContent.isNullOrEmpty()) {
                    tv_count.text = "0/30"
                } else {
                    tv_count.text = "${mContent!!.length}/30"
                }
            }
        }))

        setOnClickListener(iv_add_video, btn_commit)

        mBean?.run {
            mVideoId = id
            mContent = video_describe
            mType = type
            mPath = media_url

            et_content.setText(video_describe)
            iv_add_video.loadImg(mPath)
        }
    }

    override fun initData() {
        mPresenter?.getMainCategory()
    }

    override fun createPresenter() = ReleaseVideoPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach {
                    showDialog("正在压缩视频...")
                    val folder = File("${Constant.CACHE_PATH}/compress_video")
                    if (!folder.exists()) {
                        folder.mkdirs()
                    }
                    val file = File(folder, "${System.currentTimeMillis()}.mp4")
                    if (!file.exists()) {
                        file.createNewFile()
                    }

                    thread {
                        mPath = file.path
                        val retriever = MediaMetadataRetriever()
                        val realPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            getRealPathFromUri(Uri.parse(it.path))
                        } else {
                            it.path
                        }
                        retriever.setDataSource(realPath)
                        val originWidth: Int = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
                        val originHeight: Int = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
                        val rotationValue = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
                        val bitrate: Int = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toInt()

                        val outWidth = if (rotationValue == 90 || rotationValue == 270) {
                            640
                        } else {
                            360
                        }
                        val outHeight = if (rotationValue == 90 || rotationValue == 270) {
                            360
                        } else {
                            640
                        }

                        VideoProcessor.processor(App.context)
                                .input(realPath)
                                .output(mPath)
                                .outWidth(outWidth)
                                .outHeight(outHeight)
                                .bitrate(bitrate / 2)
                                .progressListener {
                                    if (it == 1f) {
                                        runOnUiThread {
                                            iv_add_video.loadImg(mPath)
                                            hideDialog()
                                        }
                                    }
                                }
                                .process()
                    }
                }
            }
        }
    }

    private fun getRealPathFromUri(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val data = arrayOf(MediaStore.Video.Media.DATA)
            cursor = contentResolver.query(contentUri, data, null, null, null)
            if (cursor != null && cursor.columnCount > 0) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
        return ""
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_add_video -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@ReleaseVideoActivity).builder()
                                .setSelectNum(1)
                                .setType(PictureMimeType.ofVideo())
                                .setRecordTime(60)
                                .show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.btn_commit -> {
                if (mVideoId == 0) {
                    mPresenter?.uploadVideo(mPath, mContent, mType)
                } else {
                    mPresenter?.editVideo(mVideoId, mContent, mType, mPath)
                }
            }
        }
    }

    override fun getMainCategorySuccess(bean: CityBean) {
        bean.data?.let {
            flow_layout.run {
                setAdapter(object : TagAdapter(it) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                        layoutParams = FrameLayout.LayoutParams(getDimensionPixelSize(R.dimen.dp_75), getDimensionPixelSize(R.dimen.dp_30)).apply {
                            leftMargin = getDimensionPixelSize(R.dimen.dp_15)
                            topMargin = getDimensionPixelSize(R.dimen.dp_10)
                        }
                        gravity = Gravity.CENTER
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.sp_12))
                        text = (data as CityBean.DataBean).codeName
                        setTextColor(DrawableCreator.Builder()
                                .setCheckedTextColor(0xFFEA2D2D.toInt())
                                .setUnCheckedTextColor(0xFF666666.toInt())
                                .buildTextColor())
                        background = DrawableCreator.Builder()
                                .setStrokeWidth(getDimension(R.dimen.dp_1))
                                .setCheckedStrokeColor(0xFFEA2D2D.toInt(), ContextCompat.getColor(context, R.color.line_eb))
                                .setCornersRadius(getDimension(R.dimen.dp_3))
                                .build()
                        if (mType == data.dictId) {
                            isSelected = true
                        }
                    }
                })
                setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
                    override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                        mType = it[position].dictId
                    }
                })

            }
        }
    }

    override fun uploadVideoSuccess() {
        val arrayMap = ArrayMap<String, Any>().apply {
            put("title", "upload_video")
            put("state", 1)
        }
        EventBus.getDefault().post(arrayMap)
        finish()
    }

}
