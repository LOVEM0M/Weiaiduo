package com.miyin.zhenbaoqi.ui.shop.activity.release_goods

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.hw.videoprocessor.VideoProcessor
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import com.miyin.zhenbaoqi.App
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.DraftAuctionGoodsBean
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ext.transform
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.ReleaseGoodsContract
import com.miyin.zhenbaoqi.ui.shop.dialog.GoodsTypeDialog
import com.miyin.zhenbaoqi.ui.shop.dialog.TypeTipDialog
import com.miyin.zhenbaoqi.ui.shop.presenter.ReleaseGoodsPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.noober.background.drawable.DrawableCreator
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_release_goods.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class ReleaseGoodsActivity : BaseMvpActivity<ReleaseGoodsContract.IView, ReleaseGoodsContract.IPresenter>(), ReleaseGoodsContract.IView, OnDialogCallback {

    private var mName: String? = null
    private var mContent: String? = null
    private val mPhotoUrlList = mutableListOf<String>()
    private var mVideoPath: String? = null
    private var mCateId1 = 0
    private var mCate1Name: String? = null
    private var mCateId2 = 0
    private var mCate2Name: String? = null
    private var mCateId3 = 0
    private var mCate3Name: String? = null
    private var mGoodsTypeDialog: GoodsTypeDialog? = null
    private var mHasUpdateCate = true
    private var mIsAuctionGoods = false
    private var mDraftAuctionGoodsBean: DraftAuctionGoodsBean? = null
    private var mGoodsDetailBean: GoodsDetailBean? = null
    private var mRoomId = 0
    private var mSize: String? = null
    private var mMaterial: String? = null
    private var mOriginPlace: String? = null
    private var mWeight: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mIsAuctionGoods = getBooleanExtra("auction_goods", false)
            mGoodsDetailBean = getSerializableExtra("goods_detail_bean") as GoodsDetailBean?
            mDraftAuctionGoodsBean = getSerializableExtra("bean") as DraftAuctionGoodsBean?
            mRoomId = getIntExtra("room_id", 0)
        }
        return R.layout.activity_release_goods
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).keyboardEnable(true).init()

        mGoodsDetailBean?.data?.run {
            mName = goodsName
            mContent = goodsDescribe
            mCateId1 = cateId1
            mCateId2 = cateId2
//            mCateId3 = cate_id3
//            mVideoPath = goods_video

            et_name.setText(goodsName)
            et_content.setText(goodsDescribe)
            if (!mVideoPath.isNullOrEmpty()) {
                iv_add_video.transform(mVideoPath, RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
                visible(iv_delete_video)
            }
            if (!goodsImg.isNullOrEmpty()) {
                if (goodsImg!!.contains(",")) {
                    goodsImg!!.split(",").forEach {
                        mPhotoUrlList.add(it)
                    }
                } else {
                    mPhotoUrlList.add(goodsImg!!)
                }
                iv_add_photo.transform(mPhotoUrlList[0], RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
            }

//            mPresenter?.getCategoryName(cateId1, cateId2, cate_id3)
        }
        mDraftAuctionGoodsBean?.run {
            mName = goodsName
            mContent = goodsDesc
            mCateId1 = cate1Id
            mCateId2 = cate2Id
            mCateId3 = cate3Id
            mCate1Name = cate1Name
            mCate2Name = cate2Name
            mCate3Name = cate3Name
            mVideoPath = goodsVideo
            mSize = size
            mMaterial = material
            mOriginPlace = originPlace
            mWeight = weight

            tv_goods_type.text = "$cate1Name-$cate2Name-$cate3Name"
            et_name.setText(goodsName)
            et_content.setText(goodsDesc)
            et_size.setText(mSize)
            et_material.setText(mMaterial)
            et_origin_place.setText(mOriginPlace)
            et_weight.setText(mWeight)

            if (!mVideoPath.isNullOrEmpty()) {
                iv_add_video.transform(mVideoPath, RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
                visible(iv_delete_video)
            }
            if (!goodsImg.isNullOrEmpty()) {
                if (goodsImg!!.contains(",")) {
                    goodsImg!!.split(",").forEach {
                        mPhotoUrlList.add(it)
                    }
                } else {
                    mPhotoUrlList.add(goodsImg!!)
                }
                iv_add_photo.transform(mPhotoUrlList[0], RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
            }
        }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
            }
        })
        et_size.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mSize = editable.toString().trim { it <= ' ' }
            }
        })
        et_material.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mMaterial = editable.toString().trim { it <= ' ' }
            }
        })
        et_origin_place.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mOriginPlace = editable.toString().trim { it <= ' ' }
            }
        })
        et_weight.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mWeight = editable.toString().trim { it <= ' ' }
            }
        })
        et_content.addTextChangedListener(object : EditWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }

                if (TextUtils.isEmpty(mContent)) {
                    tv_count.text = "0/500"
                } else {
                    tv_count.text = "${mContent?.length}/500"
                }
            }
        })

        thread {
            val folder = File("${Constant.CACHE_PATH}/compress_video")
            if (folder.exists()) {
                folder.listFiles()?.forEach {
                    it.delete()
                }
            }
        }

        setOnClickListener(iv_back, tv_right_title, fl_goods_type, iv_add_photo, iv_add_video, iv_delete_video, btn_commit)
    }

    override fun initData() {
        mPresenter?.getMerchantMainCateState()
    }

    override fun createPresenter() = ReleaseGoodsPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach { it ->
                    showDialog("正在上传...", false)

                    val folder = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                    if (!folder?.exists()!!) {
                        folder.mkdirs()
                    }

                    val file = File(folder, "${System.currentTimeMillis()}.mp4")
                    if (!file.exists()) {
                        file.createNewFile()
                    }

                    thread {
                        val realPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            getRealPathFromUri(Uri.parse(it.path))
                        } else {
                            it.path
                        }
                        if (File(realPath).length() / 1024 / 1024 > 15) {
                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(realPath)
                            val originWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
                            val originHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
                            val rotationValue = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
                            val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toInt()
                            Logger.d("width == $originWidth, height == $originHeight, rotation == $rotationValue")

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
                                    .output(file.path)
                                    .outWidth(outWidth)
                                    .outHeight(outHeight)
                                    .bitrate(bitrate / 2)
                                    .progressListener {
                                        Logger.d("progress == $it")
                                        if (it == 1f) {
                                            if (file.renameTo(file)) {
                                                mPresenter?.uploadGoodsVideo(file.path)
                                            }
                                        }
                                    }
                                    .process()
                        } else {
                            mPresenter?.uploadGoodsVideo(realPath)
                        }
                    }
                }
            }
        } else if (requestCode == PictureConfig.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            data?.run {
                val localMedia = PictureSelector.obtainMultipleResult(this)[0]
                mPresenter?.uploadGoodsVideo(localMedia.path)
            }
        } else if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (null != mGoodsDetailBean) {
                EventBus.getDefault().post("refreshTakeOff")
                EventBus.getDefault().post("refreshShelves")
            }
            finish()
        } else if (requestCode == 700 && resultCode == Activity.RESULT_OK) {
            data?.run {
                val list = getStringArrayListExtra("list") ?: mutableListOf<String>()
                mPhotoUrlList.clear()
                mPhotoUrlList.addAll(list)

                if (list.isNotEmpty()) {
                    iv_add_photo.transform(list[0], RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
                }
            }
        }
    }

    override fun onDestroy() {
        if (null != mGoodsTypeDialog) {
            mGoodsTypeDialog = null
        }
        super.onDestroy()
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
            R.id.iv_back -> finish()
            R.id.tv_right_title -> WebActivity.openActivity(applicationContext, "分类说明", Constant.GOODS_CATEGORY)
            R.id.fl_goods_type -> {
                if (null != mGoodsTypeDialog) {
                    mGoodsTypeDialog?.show(supportFragmentManager, "goodsType")
                } else {
                    mPresenter?.getParentList(1)//TODO 暂不确定传值
                }
            }
            R.id.iv_add_photo -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        startActivityForResult<UploadPhotoActivity>(700, "list" to mPhotoUrlList)
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.iv_add_video -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@ReleaseGoodsActivity).builder()
                                .setSelectNum(1)
                                .setRecordTime(15)
                                .setType(PictureMimeType.ofVideo())
                                .show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.iv_delete_video -> {
                AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否删除此视频？")
                        .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                            mVideoPath = null
                            Glide.with(this).clear(iv_add_video)
                            gone(iv_delete_video)

                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
            }
            R.id.btn_commit -> {
                if (mName.isNullOrEmpty()) {
                    showToast("请填写商品名称")
                    return
                }
                if (mContent.isNullOrEmpty()) {
                    showToast("请填写商品描述信息")
                    return
                }
                if (mCateId3 == 0) {
                    showToast("请选择商品分类")
                    return
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

                if (mHasUpdateCate) {
                    updateMerchantMainCateStateSuccess()
                } else {
                    mPresenter?.updateMerchantMainCateState(mCateId1)
                }
            }
        }
    }

    override fun getCategoryNameSuccess(cateName1: String, cateName2: String, cateName3: String) {
        mCate1Name = cateName1
        mCate2Name = cateName2
        mCate3Name = cateName3

        tv_goods_type.text = "$cateName1-$cateName2-$cateName3"
    }

    override fun getParentListSuccess(list: List<CityBean.DataBean>) {
        mGoodsTypeDialog = GoodsTypeDialog.newInstance(list)
        mGoodsTypeDialog!!.show(supportFragmentManager, "goodsType")
        mGoodsTypeDialog!!.setOnDialogCallback(this)
    }

    override fun getSonListSuccess(list: List<CityBean.DataBean>, type: Int) {
        if (type == 2) {
            mGoodsTypeDialog?.setRightList(list)
        } else if (type == 3) {
            if (list.isNotEmpty()) {
                visible(ll_container)
                flow_layout.setAdapter(object : TagAdapter(list) {
                    override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, getDimensionPixelSize(R.dimen.dp_25)).apply {
                            rightMargin = getDimensionPixelSize(R.dimen.dp_15)
                            bottomMargin = getDimensionPixelSize(R.dimen.dp_12)
                        }
                        gravity = Gravity.CENTER
                        setPadding(getDimensionPixelSize(R.dimen.dp_10), 0, getDimensionPixelSize(R.dimen.dp_10), 0)
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.sp_12))
                        setTextColor(DrawableCreator.Builder()
                                .setCheckedTextColor(Color.WHITE)
                                .setUnCheckedTextColor(Color.parseColor("#A1A1A1"))
                                .buildTextColor())
                        text = (data as CityBean.DataBean).codeName
                        background = DrawableCreator.Builder()
                                .setStrokeWidth(getDimension(R.dimen.dp_1))
                                .setCheckedStrokeColor(ContextCompat.getColor(context, R.color.theme_dark_purple), Color.parseColor("#A1A1A1"))
                                .setCheckedSolidColor(ContextCompat.getColor(context, R.color.theme_dark_purple), Color.WHITE)
                                .setCornersRadius(getDimension(R.dimen.dp_3))
                                .build()
                    }

                    override fun onSelected(position: Int, view: View?) {
                        val data = list[position]
                        mCateId3 = data.dictId
                        mCate3Name = data.codeName
                    }
                })
            } else {
                gone(ll_container)
            }
        }
    }

    override fun uploadGoodsImgSuccess(url: String) {

    }

    override fun uploadGoodsVideoSuccess(url: String) {
        PictureFileUtils.deleteAllCacheDirFile(this)

        mVideoPath = url
        iv_add_video.transform(url, RoundCornersTransform(getDimension(R.dimen.dp_6), RoundCornersTransform.CornerType.ALL))
        visible(iv_delete_video)
    }

    override fun updateMerchantMainCateStateSuccess() {
        mHasUpdateCate = true

        val photoUrl = if (mPhotoUrlList.isEmpty()) {
            ""
        } else {
            val sb = StringBuilder()
            mPhotoUrlList.forEach {
                sb.append(it).append(",")
            }
            sb.substring(0, sb.length - 1)
        }

        if (photoUrl.isNullOrEmpty()) {
            showToast("请选择图片上传")
            return
        }

        if (mIsAuctionGoods) {
            startActivityForResult<SetAuctionActivity>(Constant.INTENT_REQUEST_CODE, "name" to mName,
                    "content" to mContent, "photo_url" to photoUrl, "video_url" to mVideoPath, "cate_id1" to mCateId1,
                    "cate_id2" to mCateId2, "cate_id3" to mCateId3, "auction_goods" to mIsAuctionGoods, "cate_name1" to mCate1Name,
                    "cate_name2" to mCate2Name, "cate_name3" to mCate3Name, "size" to mSize, "material" to mMaterial,
                    "origin_place" to mOriginPlace, "weight" to mWeight, "bean" to mDraftAuctionGoodsBean)
        } else {
            startActivityForResult<SetReleaseActivity>(Constant.INTENT_REQUEST_CODE, "name" to mName,
                    "content" to mContent, "photo_url" to photoUrl, "video_url" to mVideoPath, "cate_id1" to mCateId1,
                    "cate_id2" to mCateId2, "cate_id3" to mCateId3, "size" to mSize, "material" to mMaterial,
                    "origin_place" to mOriginPlace, "weight" to mWeight, "bean" to mGoodsDetailBean, "room_id" to mRoomId)
        }
    }

    override fun onFailure(msg: String, type: Int) {
        if (type == 1) {
            mHasUpdateCate = false
            val dialog = TypeTipDialog.newInstance()
            dialog.show(supportFragmentManager, "typeTip")
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is CityBean.DataBean) {
            if (flag == 0) {
                mCateId1 = obj.dictId
                mCate1Name = obj.codeName

                mPresenter?.getSonList(mCateId1, 2)

                tv_goods_type.text = ""
            } else if (flag == 1) {
                mCateId2 = obj.dictId
                mCate2Name = obj.codeName

                mPresenter?.getSonList(mCateId2, 3)

                tv_goods_type.text = "$mCate1Name-$mCate2Name"
            }
        }
    }

}
