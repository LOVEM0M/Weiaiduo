package com.miyin.zhenbaoqi.ui.sort.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.GoodsDetailBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.BitmapUtils
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.dialog_goods_share.*
import java.net.URLEncoder

class GoodsShareDialog : BaseDialogFragment() {

    private var mBean: GoodsDetailBean? = null
    private var mOnDialogCallback: OnDialogCallback? = null
    private var mDrawable: Drawable? = null
    private var mShareUrl: String? = null

    companion object {
        fun newInstance(bean: GoodsDetailBean) = GoodsShareDialog().apply {
            arguments = Bundle().apply {
                putSerializable("bean", bean)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mBean = getSerializable("bean") as GoodsDetailBean
        }
        return R.layout.dialog_goods_share
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        mBean?.data?.goods?.run {
            val goodsImg = when {
                goods_img.isNullOrEmpty() -> ""
                goods_img!!.contains(",") -> goods_img!!.split(",")[0]
                else -> goods_img
            }
            iv_cover.loadImg(goodsImg)
            tv_price.text = SpanUtils()
                    .append("当前价 ¥")
                    .append(FormatUtils.formatNumber(goods_amount / 100f)).setFontSize(21, true)
                    .create()
            tv_goods_name.text = goods_name

            iv_wx_avatar.loadImg(SPUtils.getString("head_img"))
            tv_name.text = "${SPUtils.getString("nick_name")} 向你推荐"
        }
        mBean?.data?.merchants?.run {
            iv_avatar.loadImg(head_img)
            tv_shop_name.text = merchants_name
            tv_fans_count.text = "${focus_size}粉丝"

            if (quality_balance > 0) {
                iv_quality_shop.visibility = View.VISIBLE
            } else {
                iv_quality_shop.visibility = View.INVISIBLE
            }
        }

        val headImg = SPUtils.getString("head_img")
        val nickName = SPUtils.getString("nick_name")
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
        mShareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
        val size = context!!.getDimensionPixelSize(R.dimen.dp_85)
        val bitmap = BitmapUtils.createQRCode(mShareUrl!!, size, size, logoBitmap)
        iv_qr_code.loadImg(bitmap)

        setOnClickListener(ll_container, cl_container, tv_copy, tv_share_friend, tv_share_friend_circle, tv_save_photo)
    }

    override fun onDetach() {
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
        super.onDetach()
    }

    fun setDrawable(drawable: Drawable) {
        mDrawable = drawable
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_container -> dismiss()
            R.id.tv_copy -> {
                context?.copyMsg(mShareUrl!!)
                dismiss()
            }
            R.id.tv_share_friend -> {
                mOnDialogCallback?.onDialog("shareFriend", 0)
                dismiss()
            }
            R.id.tv_share_friend_circle -> {
                mOnDialogCallback?.onDialog("shareFriendCircle", 1)
                dismiss()
            }
            R.id.tv_save_photo -> {
                mBean?.data?.goods?.run {
                    val bitmap = BitmapUtils.createBitmap(cl_container)
                    savePicture(bitmap, "${goods_id}_${goods_name}.png")
                }
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
