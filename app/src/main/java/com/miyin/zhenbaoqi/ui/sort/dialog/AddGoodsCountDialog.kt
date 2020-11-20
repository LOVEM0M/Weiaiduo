package com.miyin.zhenbaoqi.ui.sort.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.utils.FormatUtils
import com.miyin.zhenbaoqi.widget.AddedView
import kotlinx.android.synthetic.main.dialog_add_goods_count.*

@SuppressLint("SetTextI18n")
class AddGoodsCountDialog : BaseDialogFragment() {

    private var mGoodsCover: String? = null
    private var mGoodsName: String? = null
    private var mGoodsPrice = 0
    private var mNumber = 1
    private var mInventory = 0
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(goodsCover: String?, goodsName: String, goodsPrice: Int, inventory: Int) = AddGoodsCountDialog().apply {
            arguments = Bundle().apply {
                putString("goodsCover", goodsCover)
                putString("goodsName", goodsName)
                putInt("goodsPrice", goodsPrice)
                putInt("inventory", inventory)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mGoodsCover = getString("goodsCover")
            mGoodsName = getString("goodsName")
            mGoodsPrice = getInt("goodsPrice")
            mInventory = getInt("inventory")
        }
        return R.layout.dialog_add_goods_count
    }

    override fun initView(view: View) {
        val goodsImg = when {
            mGoodsCover.isNullOrEmpty() -> ""
            mGoodsCover!!.contains(",") -> mGoodsCover!!.split(",")[0]
            else -> mGoodsCover
        }
        iv_cover.loadImg(goodsImg)
        tv_goods_name.text = mGoodsName
        tv_price.text = "¥${FormatUtils.formatNumber(mGoodsPrice )}"

        add_view.setMaxNumber(mInventory)
        add_view.setOnNumberChangedListener(object : AddedView.OnNumberChangedListener {
            override fun onNumberChanged(number: Int) {
                mNumber = number
            }
        })

        btn_commit.setOnClickListener {//??
            mOnDialogCallback?.onDialog(mNumber, 0)
            dismiss()
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

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
