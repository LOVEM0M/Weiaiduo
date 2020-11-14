package com.miyin.zhenbaoqi.ui.common

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.contrarywind.adapter.WheelAdapter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.BankBean
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import kotlinx.android.synthetic.main.dialog_single_data.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class SingleDataDialog : BaseDialogFragment() {

    private var mList = mutableListOf<Any>()
    private lateinit var mObj: Any
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(list: List<Any>) = SingleDataDialog().apply {
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
            mList = getSerializable("list") as MutableList<Any>
        }
        return R.layout.dialog_single_data
    }

    override fun initView(view: View) {
        setOnClickListener(tv_cancel, tv_confirm)
        mObj = mList[0]

        wheel_view.run {
            wheel_view.currentItem = 0
            wheel_view.setCyclic(false)
            setOnItemSelectedListener {
                mObj = mList[it]
            }
            adapter = object : WheelAdapter<Any> {
                override fun indexOf(o: Any) = mList.indexOf(o)

                override fun getItemsCount() = mList.size

                override fun getItem(index: Int): Any {
                    val obj = mList[index]
                    return when (obj) {
                        is CityBean.CityListBean -> obj.code_name ?: ""
                        is BankBean.BankListBean -> obj.bank_name ?: ""
                        is String -> obj.toString()
                        else -> ""
                    }
                }
            }
        }
    }

    override fun setGravity() = Gravity.BOTTOM

    override fun setWidth() = MATCH_PARENT

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_confirm -> {
                mOnDialogCallback?.onDialog(mObj, 0)
                dismiss()
            }
        }
    }

}
