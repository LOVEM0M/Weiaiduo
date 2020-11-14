package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import kotlinx.android.synthetic.main.dialog_live_auction_success.*

class LiveAuctionSuccessDialog : BaseDialogFragment() {

    private var mGoodsId = 0
    private var mGoodsImg: String? = null
    private var mGoodsName: String? = null
    private var mGoodsPrice: String? = null
    private var mGoodsFreight = 0
    private var mOrderNumber: String? = null
    private var mOnDialogCallback: OnDialogCallback? = null

    companion object {
        fun newInstance(goodsId: Int, goodsImg: String, goodsName: String, goodsPrice: String, goodsFreight: Int, orderNumber: String) = LiveAuctionSuccessDialog().apply {
            arguments = Bundle().apply {
                putInt("goods_id", goodsId)
                putString("goods_img", goodsImg)
                putString("goods_name", goodsName)
                putString("goods_price", goodsPrice)
                putInt("goods_freight", goodsFreight)
                putString("order_number", orderNumber)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mGoodsId = getInt("goods_id")
            mGoodsImg = getString("goods_img")
            mGoodsName = getString("goods_name")
            mGoodsPrice = getString("goods_price")
            mGoodsFreight = getInt("goods_freight")
            mOrderNumber = getString("order_number")
        }
        return R.layout.dialog_live_auction_success
    }

    override fun initView(view: View) {
        iv_cover.loadImg(mGoodsImg)
        tv_auction_title.text = mGoodsName
        tv_auction_price.text = mGoodsPrice

        iv_auction_close.setOnClickListener { dismiss() }
        btn_commit.setOnClickListener {
            val map = ArrayMap<String, Any>().apply {
                put("type", "success")
                put("goods_id", mGoodsId)
                put("goods_img", mGoodsImg)
                put("goods_name", mGoodsName)
                put("goods_price", mGoodsPrice)
                put("goods_freight", mGoodsFreight)
                put("order_number", mOrderNumber)
            }
            mOnDialogCallback?.onDialog(map, 0)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback) {
        mOnDialogCallback = onDialogCallback
    }

}
