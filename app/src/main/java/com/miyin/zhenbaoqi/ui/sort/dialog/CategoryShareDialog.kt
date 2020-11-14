package com.miyin.zhenbaoqi.ui.sort.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
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
import kotlinx.android.synthetic.main.dialog_category_share.*
import java.net.URLEncoder

class CategoryShareDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mCate1Id = 0
    private var mCate2Id = 0
    private var mCate3Id = 0
    private var mCate2Name: String? = null
    private var mShareUrl: String? = null

    companion object {
        fun newInstance(cate1Id: Int, cate2Id: Int, cate3Id: Int, cate2Name: String) = CategoryShareDialog().apply {
            arguments = Bundle().apply {
                putInt("cate1_id", cate1Id)
                putInt("cate2_id", cate2Id)
                putInt("cate3_id", cate3Id)
                putString("cate2_name", cate2Name)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mCate1Id = getInt("cate1_id")
            mCate2Id = getInt("cate2_id")
            mCate3Id = getInt("cate3_id")
            mCate2Name = getString("cate2_name")
        }
        return R.layout.dialog_category_share
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        val headImg = SPUtils.getString("head_img")
        iv_avatar.loadImg(headImg)
        val nickName = SPUtils.getString("nick_name")
        tv_name.text = nickName
        tv_cate_name.text = "找${mCate2Name}，上唯爱多"

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
                mOnDialogCallback?.onDialog("shareFriendCircle", 0)
                dismiss()
            }
            R.id.tv_save_photo -> {
                val bitmap = BitmapUtils.createBitmap(cl_container)
                savePicture(bitmap, "${mCate1Id}_${mCate2Id}_$mCate3Id.png")
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
