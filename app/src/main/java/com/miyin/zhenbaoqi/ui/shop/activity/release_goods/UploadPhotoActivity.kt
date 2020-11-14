package com.miyin.zhenbaoqi.ui.shop.activity.release_goods

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.adapter.UploadPhotoAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ReleaseGoodsContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ReleaseGoodsPresenter
import com.miyin.zhenbaoqi.utils.MaxImageView
import kotlinx.android.synthetic.main.activity_upload_photo.*
import java.util.*

class UploadPhotoActivity : BaseMvpActivity<ReleaseGoodsContract.IView, ReleaseGoodsContract.IPresenter>(), ReleaseGoodsContract.IView {

    private var mList = ArrayList<String>()
    private lateinit var mAdapter: UploadPhotoAdapter
    private var mFooterView: View? = null
    private var mIsShowDelete = false
    private var mItemTouchHelper: ItemTouchHelper? = null
    private val mCompressList = ArrayList<String>()

    override fun getContentView(): Int {
        mList = intent.getStringArrayListExtra("list") ?: ArrayList()
        return R.layout.activity_upload_photo
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()

        recycler_view.run {
            mAdapter = UploadPhotoAdapter(mList)
            adapter = mAdapter
            val gridLayoutManager = GridLayoutManager(context, 3)
            layoutManager = gridLayoutManager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            if (mList.isNullOrEmpty() || mList.size < 9) {
                val dimension = getDimensionPixelSize(R.dimen.dp_104)
                mFooterView = ImageView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(dimension, dimension).apply {
                        topMargin = getDimensionPixelSize(R.dimen.dp_10)
                        leftMargin = getDimensionPixelSize(R.dimen.dp_10)
                        setBackgroundResource(R.drawable.ic_default_add_photo)

                        setOnClickListener {
                            if (mIsShowDelete) {
                                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                                    override fun onGranted() {
                                        SelectPhotoDialog(this@UploadPhotoActivity).builder()
                                                .setSelectNum(9 - mList.size)
                                                .show()
                                    }

                                    override fun onDenied() {
                                        showToast(getString(R.string.permission_camera_storage))
                                    }
                                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                    }
                }
                mAdapter.addFooterView(mFooterView)
            }

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.iv_delete) {
                    mList.removeAt(position)
                    mAdapter.notifyItemRemoved(position)
                    mAdapter.notifyItemRangeChanged(position, mList.size)

                    if (mAdapter.footerLayoutCount == 0 && null != mFooterView) {
                        mAdapter.addFooterView(mFooterView)
                    }
                } else if (view.id == R.id.iv_cover) {
                    MaxImageView.maxImageView(this@UploadPhotoActivity, mList, position)
                }
            }

            val onItemDragListener = object : OnItemDragListener {
                override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, position: Int) {

                }

                override fun onItemDragMoving(source: RecyclerView.ViewHolder?, from: Int, target: RecyclerView.ViewHolder?, to: Int) {

                }

                override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, position: Int) {

                }
            }
            val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter)
            mItemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
            mItemTouchHelper?.attachToRecyclerView(this)

            mAdapter.setOnItemDragListener(onItemDragListener)

        }

        setOnClickListener(tv_cancel, tv_operate)
    }

    override fun initData() {

    }

    override fun createPresenter() = ReleaseGoodsPresenter()

    override fun onDestroy() {
        MaxImageView.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            PictureSelector.obtainMultipleResult(data).forEach {
                if (it.isCompressed) {
                    val path = it.compressPath
                    mCompressList.add(path)
                }
            }
            showDialog("正在上传图片...")
            uploadImg()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> {
                finish()
            }
            R.id.tv_operate -> {
                mIsShowDelete = !mIsShowDelete
                tv_operate.text = if (mIsShowDelete) "完成" else "编辑"
                mAdapter.setDeleteState(mIsShowDelete)

                if (mIsShowDelete) {
                    mAdapter.enableDragItem(mItemTouchHelper!!)
                } else {
                    mAdapter.disableDragItem()

                    val intent = Intent()
                    intent.putStringArrayListExtra("list", mList)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun uploadImg() {
        if (mCompressList.isEmpty()) {
            hideDialog()

            if (mList.size == 9) {
                mAdapter.removeAllFooterView()
            }

            PictureFileUtils.deleteAllCacheDirFile(this)
            mAdapter.setNewData(mList)
            return
        }
        mPresenter?.uploadGoodsImg(mCompressList[0])
    }

    override fun getCategoryNameSuccess(cateName1: String, cateName2: String, cateName3: String) {

    }

    override fun getParentListSuccess(list: List<CityBean.CityListBean>) {

    }

    override fun getSonListSuccess(list: List<CityBean.CityListBean>, type: Int) {

    }

    override fun uploadGoodsImgSuccess(url: String) {
        mCompressList.removeAt(0)
        mList.add(url)

        uploadImg()
    }

    override fun uploadGoodsVideoSuccess(url: String) {

    }

    override fun updateMerchantMainCateStateSuccess() {

    }

    override fun onFailure(msg: String, type: Int) {
        if (type == 0) {
            mCompressList.removeAt(0)
            uploadImg()
        }
    }

}
