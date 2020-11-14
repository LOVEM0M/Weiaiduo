package com.miyin.zhenbaoqi.ui.shop.dialog

import android.content.Context
import android.text.Editable
import android.util.ArrayMap
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_set_wx.*

class SetWXDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mContent: String? = null
    private var mPath: String? = null

    companion object {
        fun newsInstance() = SetWXDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        return R.layout.dialog_set_wx
    }

    override fun initView(view: View) {
        et_content.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mContent = editable.toString().trim { it <= ' ' }
            }
        })

        setOnClickListener(iv_close, iv_wx_qr_code, tv_commit)
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            R.id.iv_wx_qr_code -> {
                mOnDialogCallback?.onDialog("wxQRCode", 0)
            }
            R.id.tv_commit -> {
                if (mContent.isNullOrEmpty()) {
                    ToastUtils.showToast("请填写微信号")
                    return
                }
                if (mPath.isNullOrEmpty()) {
                    ToastUtils.showToast("请上传微信二维码图片")
                    return
                }

                val map = ArrayMap<String, String>().apply {
                    put("title", mContent)
                    put("url", mPath)
                }
                mOnDialogCallback?.onDialog(map, 0)
            }
        }
    }

    override fun setAnimation() = R.style.CenterDialogAnimation

    fun setPath(path: String?) {
        mPath = path
        iv_wx_qr_code.loadImg(path)
    }

}
