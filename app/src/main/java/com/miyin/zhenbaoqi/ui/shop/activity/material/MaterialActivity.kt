package com.miyin.zhenbaoqi.ui.shop.activity.material

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jiguang.dy.Protocol.mContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.MaterialBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.getVersionCode
import com.miyin.zhenbaoqi.ui.shop.adapter.MaterialAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.MaterialContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MaterialPresenter
import com.miyin.zhenbaoqi.utils.MaxImageView
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_refresh.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread

class MaterialActivity : BaseListActivity<MaterialContract.IView, MaterialContract.IPresenter>(), MaterialContract.IView {

    private var mList = mutableListOf<MaterialBean.DataBean>()
    private lateinit var mAdapter: MaterialAdapter
    private val mDisposable = CompositeDisposable()

    override fun getContentView() = R.layout.activity_material

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("素材中心")
        immersionBar { statusBarDarkFont(true) }

        showLoading()

        recycler_view.run {
            mAdapter = MaterialAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                when (view.id) {
                    R.id.iv_cover -> {
                        val img = when {
                            bean.material_img.isNullOrEmpty() -> ""
                            bean.material_img!!.contains(",") -> bean.material_img!!.split(",")[0]
                            else -> bean.material_img
                        }
                        img?.let {
                            MaxImageView.singleImageView(this@MaterialActivity, it)
                        }
                    }
                    R.id.tv_forward -> {
                        // 图片地址 List
                        if (bean.material_img.isNullOrEmpty()) return@setOnItemChildClickListener

                        val url = when {
                            bean.material_img!!.contains(",") -> bean.material_img!!.split(",")[0]
                            else -> bean.material_img!!
                        }
                        // 网络图片保存到本地
                        saveImageToSdCard(context, url, bean.id, bean.content ?: "", true)
                    }
                    R.id.tv_save_photo -> {
                        if (bean.material_img.isNullOrEmpty()) return@setOnItemChildClickListener

                        val url = when {
                            bean.material_img!!.contains(",") -> bean.material_img!!.split(",")[0]
                            else -> bean.material_img!!
                        }
                        // 网络图片保存到本地
                        saveImageToSdCard(context, url, bean.id, bean.content ?: "", false)
                    }
                    R.id.tv_copy -> {
                        copyMsg(bean.content ?: "")
                        showToast("复制成功")
                    }
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }

        thread {
            val folder = File("${Constant.CACHE_PATH}/material")
            if (folder.exists()) {
                folder.listFiles()?.forEach {
                    it.delete()
                }
            }
        }
    }

    override fun initData() {
        mPresenter?.getMerchantMaterialList(mPage, mCount)
    }

    override fun createPresenter() = MaterialPresenter()

    override fun onDestroy() {
        mDisposable.dispose()
        MaxImageView.clear()
        super.onDestroy()
    }

    private fun saveImageToSdCard(context: Context, image: String, id: Int, content: String, isShare: Boolean) {
        val disposable = Observable.create(ObservableOnSubscribe<File> { e ->
            e.onNext(Glide.with(mContext)
                    .load(image)
                    .downloadOnly(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .get())
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    val folder = File("${Constant.CACHE_PATH}/material")
                    if (!folder.exists()) {
                        folder.mkdirs()
                    }

                    val file = File(folder, "material_${id}.png")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    // 把 Glide 下载得到图片复制到定义好的目录中去
                    copy(it, file)

                    // 最后通知图库更新
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(file.path))))

                    if (isShare) {
                        // 分享到微信朋友圈
                        val weChatIntent = Intent()
                        weChatIntent.component = ComponentName(PACKAGE_NAME_WEI_XIN, "com.tencent.mm.ui.tools.ShareToTimeLineUI")

                        weChatIntent.type = "image/*"
                        weChatIntent.putExtra("Kdescription", content)
                        if (getVersionCode(PACKAGE_NAME_WEI_XIN) < VERSION_CODE_FOR_WEI_XIN_VER7) {
                            // 分享的图片集合
                            val imageUris = ArrayList<Uri>()
                            imageUris.add(Uri.fromFile(file))

                            weChatIntent.action = Intent.ACTION_SEND_MULTIPLE
                            weChatIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
                        } else {
                            weChatIntent.action = Intent.ACTION_SEND
                            weChatIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                        }
                        context.startActivity(weChatIntent)
                    } else {
                        ToastUtils.showToast("保存成功")
                    }
                }, {
                    Logger.d("error_msg == ${it.message}")
                })
        mDisposable.add(disposable)
    }

    private fun copy(source: File, target: File) {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileInputStream = FileInputStream(source)
            fileOutputStream = FileOutputStream(target)
            val buffer = ByteArray(1024)
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream?.close()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getMerchantMaterialListSuccess(bean: MaterialBean) {
        with(bean) {
            if (current_page == 1) {
                mList = data!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(data!!)
            }

            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

    companion object {
        /**
         * 微信7.0版本号，兼容处理微信7.0版本分享到朋友圈不支持多图片的问题
         */
        private const val VERSION_CODE_FOR_WEI_XIN_VER7 = 1380

        /**
         * 微信包名
         */
        private const val PACKAGE_NAME_WEI_XIN = "com.tencent.mm"
    }

}
