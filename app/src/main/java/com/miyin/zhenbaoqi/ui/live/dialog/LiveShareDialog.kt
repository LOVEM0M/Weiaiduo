package com.miyin.zhenbaoqi.ui.live.dialog

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.LiveShareDataBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.BitmapUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.dialog_live_share.*
import java.net.URLEncoder

@SuppressLint("SetTextI18n")
class LiveShareDialog : BaseDialogFragment() {

    private var mBean: LiveShareDataBean? = null
    private var mOnDialogCallback: OnDialogCallback? = null
    private var mShareUrl: String? = null

    companion object {
        fun newInstance(bean: LiveShareDataBean) = LiveShareDialog().apply {
            arguments = Bundle().apply {
                putSerializable("bean", bean)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mBean = getSerializable("bean") as LiveShareDataBean
        }
        return R.layout.dialog_live_share
    }

    override fun initView(view: View) {
        val nickName = SPUtils.getString("nick_name")
        val headImg = SPUtils.getString("head_img")

        mBean?.run {
            iv_avatar.loadImg(icon)
            tv_fans_counts.text = "${fans_count}粉丝"

            val goodsImg = when {
                cover.isNullOrEmpty() -> ""
                cover!!.contains(",") -> cover!!.split(",")[0]
                else -> cover
            }
            iv_cover.loadImg(goodsImg)
            tv_shop_name.text = name
            Logger.d("name == $name")

            iv_wx_avatar.loadImg(headImg)
            tv_name_command.text = "$nickName 向你推荐"
        }

        mShareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
        val size = context!!.getDimensionPixelSize(R.dimen.dp_85)
        val logoBm = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
        iv_qrcode.loadImg(BitmapUtils.createQRCode(mShareUrl ?: "", size, size, logoBm))

        setOnClickListener(ll_container, ll_linear, tv_share_friend, tv_copy, tv_share_friend_circle, tv_save_photo)
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_container -> dismiss()
            R.id.tv_share_friend -> {
                mOnDialogCallback?.onDialog("shareFriend", 0)
                dismiss()
            }
            R.id.tv_share_friend_circle -> {
                mOnDialogCallback?.onDialog("shareFriendCircle", 0)
                dismiss()
            }
            R.id.tv_copy -> {
                context?.copyMsg(mShareUrl ?: "")
                ToastUtils.showToast("复制成功")
                dismiss()
            }
            R.id.tv_save_photo -> {
                mBean?.run {
                    val bitmap = BitmapUtils.createBitmap(ll_linear)
                    savePicture(bitmap, "${name}.png")
                }
                dismiss()
            }
        }
    }

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
