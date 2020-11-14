package com.miyin.zhenbaoqi.ui.shop.activity.invite

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.ArrayMap
import android.view.View
import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.ImageBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.SetWeChatContract
import com.miyin.zhenbaoqi.ui.shop.dialog.SetWXDialog
import com.miyin.zhenbaoqi.ui.shop.fragment.ManagerInviteFragment
import com.miyin.zhenbaoqi.ui.shop.presenter.SetWeChatPresenter
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_manager_invite.*
import kotlinx.android.synthetic.main.activity_manager_invite.view_pager

class ManagerInviteActivity : BaseMvpActivity<SetWeChatContract.IView, SetWeChatContract.IPresenter>(), SetWeChatContract.IView, OnDialogCallback {

    private var mTag = 0
    private var mDialog: SetWXDialog? = null

    override fun getContentView(): Int {
        mTag = intent.getIntExtra("tag", 0)
        return R.layout.activity_manager_invite
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("邀请管理")
        immersionBar { statusBarDarkFont(true) }

        val titleList = listOf("新秀", "专属粉丝", "普通粉丝")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEach {
            fragmentList.add(ManagerInviteFragment.newInstance(it))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.currentItem = mTag
        view_pager.offscreenPageLimit = titleList.size

        SpanUtils.with(tv_invite_code)
                .appendLine("我的邀请码").setFontSize(12, true)
                .setForegroundColor(ContextCompat.getColor(this, R.color.text_33))
                .append(SPUtils.getInt("user_id").toString()).setFontSize(25, true).setBold()
                .setForegroundColor(ContextCompat.getColor(this, R.color.text_33))
                .appendSpace(15)
                .appendLine("复制").setFontSize(12, true)
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        copyMsg(SPUtils.getInt("user_id").toString())

                        ToastUtils.showToast("复制成功")
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.run {
                            isUnderlineText = false
                            color = 0xFF1AA5D9.toInt()
                        }
                    }
                })
                .append("被邀请好友填写你的邀请码即可邀请成功").setFontSize(12, true)
                .setForegroundColor(0xFFB3B3B3.toInt())
                .create()
        tv_invite_code.highlightColor = Color.TRANSPARENT


        tv_setting.setOnClickListener {
            mDialog = SetWXDialog.newsInstance()
            mDialog?.show(supportFragmentManager, "setWX")
        }
    }

    override fun initData() {

    }

    override fun createPresenter() = SetWeChatPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            PictureSelector.obtainMultipleResult(data).forEach {
                if (it.isCompressed) {
                    val path = it.compressPath
                    mPresenter?.uploadImage(path)
                }
            }
        }
    }

    override fun uploadImageSuccess(bean: ImageBean) {
        mDialog?.setPath(bean.photo_url)
    }

    override fun inviteMerchantWeChatSuccess() {
        mDialog?.dismiss()
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            when (obj) {
                "wxQRCode" -> {
                    SelectPhotoDialog(this).builder().setSelectNum(1).setType(PictureMimeType.ofImage()).show()
                }
            }
        } else if (obj is ArrayMap<*, *>) {
            val map = obj as ArrayMap<String, String>
            mPresenter?.inviteMerchantWeChat(map["title"]!!, map["url"]!!)
        }
    }

}
