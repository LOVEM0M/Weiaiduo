package com.miyin.zhenbaoqi.ui.home.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.BitmapUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_video_share.*
import java.net.URLEncoder

class VideoShareDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mMerchantId = 0
    private var mShareUrl: String? = null
    private var mAvatar: String? = null
    private var mShopName: String? = null
    private var mShopDesc: String? = null
    private var mCover: String? = null
    private var mVideoDesc: String? = null

    companion object {
        fun newInstance(merchantId: Int, avatar: String, shopName: String, shopDesc: String, cover: String, videoDesc: String) = VideoShareDialog().apply {
            arguments = Bundle().apply {
                putInt("merchant_id", merchantId)
                putString("avatar", avatar)
                putString("shop_name", shopName)
                putString("shop_desc", shopDesc)
                putString("cover", cover)
                putString("video_desc", videoDesc)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mMerchantId = getInt("merchant_id")
            mAvatar = getString("avatar")
            mShopName = getString("shop_name")
            mShopDesc = getString("shop_desc")
            mCover = getString("cover")
            mVideoDesc = getString("video_desc")
        }
        return R.layout.dialog_video_share
    }

    override fun initView(view: View) {
        iv_avatar.loadImg(mAvatar)
        tv_shop_name.text = mShopName
        tv_desc.text = mShopDesc
        iv_cover.loadImg(mCover)
        tv_video_desc.text = mVideoDesc

        val headImg = SPUtils.getString("head_img")
        val nickName = SPUtils.getString("nick_name")
        mShareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
        val size = context!!.getDimensionPixelSize(R.dimen.dp_85)
        val bitmap = BitmapUtils.createQRCode(mShareUrl!!, size, size, null)
        iv_qr_code.loadImg(bitmap)

        setOnClickListener(ll_container, cl_container, tv_copy, tv_share_friend, tv_share_friend_circle, tv_save_photo)
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_container -> dismiss()
            R.id.tv_copy -> {
                context?.copyMsg(mShareUrl!!)

                ToastUtils.showToast("复制成功")
                dismiss()
            }
            R.id.tv_share_friend -> {
                mOnDialogCallback?.onDialog("shareFriend", 3)
                dismiss()
            }
            R.id.tv_share_friend_circle -> {
                mOnDialogCallback?.onDialog("shareFriendCircle", 3)
                dismiss()
            }
            R.id.tv_save_photo -> {
                val bitmap = BitmapUtils.createBitmap(cl_container)
                savePicture(bitmap, "${mMerchantId}_$mShopName.png")
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
