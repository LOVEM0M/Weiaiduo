package com.miyin.zhenbaoqi.ui.common

import android.content.Context
import android.graphics.BitmapFactory
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.BitmapUtils
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.dialog_special_activity.*

class SpecialActivityDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance() = SpecialActivityDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView() = R.layout.dialog_special_activity

    override fun initView(view: View) {
        val headImg = SPUtils.getString("head_img")
        val nickName = SPUtils.getString("nick_name")

        val url = "${Constant.SHARE_INVITE}?user_name=$nickName&user_id=${SPUtils.getInt("user_id")}&avatar=$headImg"
        val size = context!!.getDimensionPixelSize(R.dimen.dp_120)
        val bitmap = BitmapUtils.createQRCode(url, size, size, null)
        iv_qr_code.loadImg(bitmap)

        setOnClickListener(ll_container, fl_container, tv_share_friend, tv_share_friend_circle, tv_save_photo, tv_copy)
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
            R.id.tv_share_friend -> {
                mOnDialogCallback?.onDialog("", 1)
                dismiss()
            }
            R.id.tv_share_friend_circle -> {
                mOnDialogCallback?.onDialog("", 2)
                dismiss()
            }
            R.id.tv_save_photo -> {
                val bitmap = BitmapUtils.createBitmap(fl_container)
                savePicture(bitmap, "wuyi_activity.png")
                dismiss()
            }
            R.id.tv_copy -> {
                mOnDialogCallback?.onDialog("", 3)
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
