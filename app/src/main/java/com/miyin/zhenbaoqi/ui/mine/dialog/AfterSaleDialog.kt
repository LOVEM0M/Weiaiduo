package com.miyin.zhenbaoqi.ui.mine.dialog

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.mine.adapter.AfterSaleAdapter
import kotlinx.android.synthetic.main.dialog_after_sale.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class AfterSaleDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mList: List<String>? = null
    private lateinit var mAdapter: AfterSaleAdapter
    private var mReason: String? = null

    companion object {
        fun newInstance(list: List<String>) = AfterSaleDialog().apply {
            val bundle = Bundle()
            bundle.putSerializable("list", list as Serializable)
            arguments = bundle
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mList = getSerializable("list") as List<String>
        }
        return R.layout.dialog_after_sale
    }

    override fun initView(view: View) {
        mList?.run {
            mReason = this[0]
            mAdapter = AfterSaleAdapter(this)
            recycler_view.adapter = mAdapter
            recycler_view.layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_title) {
                    mReason = this[position]
                    mAdapter.setPosition(position)
                }
            }
        }

        setOnClickListener(iv_close, btn_commit)
    }

    override fun onDetach() {
        mOnDialogCallback = null
        super.onDetach()
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> dismiss()
            R.id.btn_commit -> {
                mOnDialogCallback?.onDialog(mReason ?: "", 0)
                dismiss()
            }
        }
    }

}
