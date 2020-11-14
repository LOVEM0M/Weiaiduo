package com.miyin.zhenbaoqi.ui.sort.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.CouponBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.mine.adapter.CouponAdapter
import kotlinx.android.synthetic.main.dialog_coupon.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class CouponDialog : BaseDialogFragment() {

    private var mList = mutableListOf<CouponBean.ListBean>()
    private lateinit var mAdapter: CouponAdapter
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(list: List<CouponBean.ListBean>) = CouponDialog().apply {
            arguments = Bundle().apply {
                putSerializable("list", list as Serializable)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mList = (getSerializable("list") as List<CouponBean.ListBean>).toMutableList()
        }
        return R.layout.dialog_coupon
    }

    override fun initView(view: View) {
        recycler_view.run {
            mAdapter = CouponAdapter(mList, 0)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_use) {
                    mOnDialogCallback?.onDialog(mList[position], 0)
                    dismiss()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
