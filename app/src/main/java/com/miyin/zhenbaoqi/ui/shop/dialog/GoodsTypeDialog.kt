package com.miyin.zhenbaoqi.ui.shop.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.shop.adapter.GoodsTypeAdapter
import kotlinx.android.synthetic.main.dialog_goods_type.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class GoodsTypeDialog : BaseDialogFragment() {

    private var mLeftList = mutableListOf<CityBean.CityListBean>()
    private lateinit var mLeftAdapter: GoodsTypeAdapter
    private var mRightList = mutableListOf<CityBean.CityListBean>()
    private lateinit var mRightAdapter: GoodsTypeAdapter
    private var mRightPosition = 0
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(list: List<CityBean.CityListBean>) = GoodsTypeDialog().apply {
            arguments = Bundle().apply {
                putSerializable("list", list as Serializable)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mLeftList = (getSerializable("list") as List<CityBean.CityListBean>).toMutableList()
        }
        return R.layout.dialog_goods_type
    }

    override fun initView(view: View) {
        rv_left.run {
            mLeftAdapter = GoodsTypeAdapter(mLeftList, 0)
            adapter = mLeftAdapter
            layoutManager = LinearLayoutManager(context)

            mLeftAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_title) {
                    mOnDialogCallback?.onDialog(mLeftList[position], 0)
                    mLeftAdapter.setPosition(position)
                }
            }
        }

        rv_right.run {
            mRightAdapter = GoodsTypeAdapter(mRightList, 1)
            adapter = mRightAdapter
            layoutManager = LinearLayoutManager(context)

            mRightAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_title) {
                    mRightPosition = position
                    mRightAdapter.setPosition(position)
                }
            }
        }

        if (mLeftList.isNotEmpty()) {
            mOnDialogCallback?.onDialog(mLeftList[0], 0)
        }

        setOnClickListener(tv_cancel, tv_confirm)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_confirm -> {
                mOnDialogCallback?.onDialog(mRightList[mRightPosition], 1)
                dismiss()
            }
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    fun setRightList(list: List<CityBean.CityListBean>) {
        mRightList.clear()
        mRightList.addAll(list)
        mRightAdapter.setNewData(mRightList)

        mRightPosition = 0
        mRightAdapter.setPosition(mRightPosition)
    }

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
