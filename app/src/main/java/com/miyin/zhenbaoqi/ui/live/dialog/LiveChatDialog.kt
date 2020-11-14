package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.DiaLogResultDataBean
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils
import kotlinx.android.synthetic.main.dialog_live_chat.*
import org.greenrobot.eventbus.EventBus

class LiveChatDialog : BaseDialogFragment() {

    private var mContent: String? = null
    private val mHandler = Handler()

    companion object {
        fun newInstance() = LiveChatDialog()
    }

    override fun getContentView(): Int = R.layout.dialog_live_chat

    override fun initView(view: View) {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        et_content.run {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            if (!TextUtils.isEmpty(mContent)) {
                mContent = null
                text = mContent
            }

            addTextChangedListener(object : EditWatcher() {
                override fun afterTextChanged(editable: Editable?) {
                    mContent = editable.toString().trim { it <= ' ' }
                }
            })
        }

        tv_send.setOnClickListener {
            mContent?.run {
                val bean: DiaLogResultDataBean = DiaLogResultDataBean(mContent)
                EventBus.getDefault().post(bean)
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setDimAmount(0f)
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed({
            // 调用系统输入法
            KeyboardUtils.toggleSoftInput(et_content)
        }, 200)
    }

    override fun onDestroyView() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
