package com.miyin.zhenbaoqi.ui.sort.dialog

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import kotlinx.android.synthetic.main.dialog_cate_list_share.*
import kotlinx.android.synthetic.main.dialog_cate_list_share.cl_container
import kotlinx.android.synthetic.main.dialog_cate_list_share.iv_avatar
import kotlinx.android.synthetic.main.dialog_cate_list_share.iv_qr_code
import kotlinx.android.synthetic.main.dialog_cate_list_share.iv_quality_shop
import kotlinx.android.synthetic.main.dialog_cate_list_share.ll_container
import kotlinx.android.synthetic.main.dialog_cate_list_share.tv_copy
import kotlinx.android.synthetic.main.dialog_cate_list_share.tv_name
import kotlinx.android.synthetic.main.dialog_cate_list_share.tv_save_photo
import kotlinx.android.synthetic.main.dialog_cate_list_share.tv_share_friend
import kotlinx.android.synthetic.main.dialog_cate_list_share.tv_share_friend_circle
import kotlinx.android.synthetic.main.dialog_category_share.*
import java.net.URLEncoder

class CateListShareDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mShareUrl: String? = null

    companion object {
        fun newInstance() = CateListShareDialog()
    }

    override fun getContentView() = R.layout.dialog_cate_list_share

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        val headImg = SPUtils.getString("head_img")
        iv_avatar.loadImg(headImg)
        iv_wx_avatar.loadImg(headImg)
        val nickName = SPUtils.getString("nick_name")
        tv_user_name.text = nickName
        tv_name.text = "$nickName 向您推荐"

        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo)
        mShareUrl = "${Constant.SHARE_INVITE}?user_name=${URLEncoder.encode(nickName, "utf-8")}&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
        val size = context!!.getDimensionPixelSize(R.dimen.dp_85)
        val bitmap = BitmapUtils.createQRCode(mShareUrl!!, size, size, logoBitmap)
        iv_qr_code.loadImg(bitmap)

        val qualityShop = SPUtils.getInt("quality_shop")
        if (qualityShop > 0) {
            iv_quality_shop.visibility = View.VISIBLE
        } else {
            iv_quality_shop.visibility = View.INVISIBLE
        }

        setOnClickListener(ll_container, cl_container, tv_copy, tv_share_friend, tv_share_friend_circle, tv_save_photo)
    }

    override fun onDetach() {
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
        super.onDetach()
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
                mOnDialogCallback?.onDialog("shareFriend", 0)
                dismiss()
            }
            R.id.tv_share_friend_circle -> {
                mOnDialogCallback?.onDialog("shareFriendCircle", 1)
                dismiss()
            }
            R.id.tv_save_photo -> {
                val bitmap = BitmapUtils.createBitmap(cl_container)
                savePicture(bitmap, "category_list.png")
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
