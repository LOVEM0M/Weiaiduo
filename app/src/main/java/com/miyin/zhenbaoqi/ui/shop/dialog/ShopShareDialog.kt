package com.miyin.zhenbaoqi.ui.shop.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.MerchantInfoBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.*
import com.miyin.zhenbaoqi.utils.BitmapUtils
import com.miyin.zhenbaoqi.utils.RoundCornersTransform
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_shop_share.*
import java.net.URLEncoder

class ShopShareDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mBean: MerchantInfoBean.DataBean? = null
    private var mMerchantId = 0
    private var mShareUrl: String? = null

    companion object {
        fun newInstance(bean: MerchantInfoBean.DataBean, merchantId: Int) = ShopShareDialog().apply {
            arguments = Bundle().apply {
                putSerializable("bean", bean)
                putInt("merchant_id", merchantId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mBean = getSerializable("bean") as MerchantInfoBean.DataBean
            mMerchantId = getInt("merchant_id")
        }
        return R.layout.dialog_shop_share
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        mBean?.run {
            val transform = RoundCornersTransform(context?.getDimension(R.dimen.dp_5)!!, RoundCornersTransform.CornerType.TOP)
            iv_cover.loadImgAll(merchants_back, R.drawable.ic_merchant_share_bg, transform)
            iv_avatar.loadImg(head_img)

            tv_shop_name.text = if (merchants_name.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_name
            tv_desc.text = if (merchants_subtitle.isNullOrEmpty()) "商家很懒，什么信息也没有留下" else merchants_subtitle

            val headImg = SPUtils.getString("head_img")
            val nickName = SPUtils.getString("nick_name")
            val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
            mShareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
            val size = context!!.getDimensionPixelSize(R.dimen.dp_85)
            val bitmap = BitmapUtils.createQRCode(mShareUrl!!, size, size, logoBitmap)
            iv_qr_code.loadImg(bitmap)

            if (quality_retention_money > 0) {
                iv_quality_shop.visibility = View.VISIBLE
            } else {
                iv_quality_shop.visibility = View.INVISIBLE
            }

            iv_wx_avatar.loadImg(headImg)
            tv_name.text = "$nickName 向你推荐"
        }

        setOnClickListener(ll_container, cl_container, tv_copy, tv_share_friend, tv_share_friend_circle, tv_save_photo)
    }

    override fun setGravity() = Gravity.BOTTOM

    override fun setWidth() = MATCH_PARENT

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
                mOnDialogCallback?.onDialog("shareFriendCircle", 4)
                dismiss()
            }
            R.id.tv_save_photo -> {
                mBean?.run {
                    val bitmap = BitmapUtils.createBitmap(cl_container)
                    savePicture(bitmap, "${mMerchantId}_$merchants_name.png")
                }
                dismiss()
            }
        }
    }

}
