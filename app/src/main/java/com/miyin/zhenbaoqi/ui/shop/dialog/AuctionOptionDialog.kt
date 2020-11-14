package com.miyin.zhenbaoqi.ui.shop.dialog

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.AuctionOptionMultiBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ui.shop.adapter.AuctionOptionAdapter
import kotlinx.android.synthetic.main.dialog_auction_option.*

class AuctionOptionDialog : BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mOnSelectTimeCallback: OnSelectTimeCallback? = null
    private var mType: String? = null
    private val mSecondList = listOf(10, 15, 30, 45)
    private val mHourList = listOf(1, 2, 3, 5, 10, 24, 36, 48)
    private val mPointList = listOf("今天12:00", "今天16:00", "今天17:00", "今天19:00", "今天20:00", "今天21:00", "今天22:00", "今天23:00",
            "明天12:00", "明天16:00", "明天17:00", "明天19:00", "明天20:00", "明天21:00", "明天22:00", "明天23:00")
    private val mPriceList = listOf(0, 1, 10, 30, 50, 100, 200, 300, 500, 800, 1000, 1500, 2000)
    private lateinit var mAdapter: AuctionOptionAdapter
    private val mList = mutableListOf<AuctionOptionMultiBean>()
    private var mPosition = 0

    companion object {
        fun newInstance(type: String) = AuctionOptionDialog().apply {
            arguments = Bundle().apply {
                putString("type", type)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
        mOnSelectTimeCallback = context as OnSelectTimeCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mType = getString("type")
        }
        return R.layout.dialog_auction_option
    }

    override fun initView(view: View) {
        if (mType == "security_deposit") {
            tv_title.text = "缴纳保证金"
            tv_desc.text = "设置保证金后，买卖双方均需缴纳，正常交易后原路全额退还。如一方违约，将赔付对方"

            val bean = AuctionOptionMultiBean().apply {
                type = 0
                title = "缴纳保证金"
                parent_id = 0
            }
            mList.add(bean)
            mPriceList.forEachIndexed { index, it ->
                val data = AuctionOptionMultiBean().apply {
                    type = 1
                    title = "${it}元"
                    parent_id = index
                }
                mList.add(data)
            }
        } else {
            tv_title.text = "延时竞价"
            tv_desc.text = "所有拍卖最后1分钟有新出价，结束时间自动延迟5分钟。"

            val secondBean = AuctionOptionMultiBean().apply {
                type = 0
                title = "秒拍"
            }
            mList.add(secondBean)
            mSecondList.forEachIndexed { index, it ->
                val data = AuctionOptionMultiBean().apply {
                    type = 1
                    title = "${it}分钟"
                    parent_id = index
                    belong = 1
                }
                mList.add(data)
            }

            val hourBean = AuctionOptionMultiBean().apply {
                type = 0
                title = "分钟拍"
            }
            mList.add(hourBean)
            mHourList.forEachIndexed { index, it ->
                val data = AuctionOptionMultiBean().apply {
                    type = 1
                    title = "${it}小时"
                    parent_id = index
                    belong = 2
                }
                mList.add(data)
            }

            val pointBean = AuctionOptionMultiBean().apply {
                type = 0
                title = "到点拍"
            }
            mList.add(pointBean)
            mPointList.forEachIndexed { index, it ->
                val data = AuctionOptionMultiBean().apply {
                    type = 1
                    title = it
                    parent_id = index
                    belong = 3
                }
                mList.add(data)
            }
        }

        recycler_view.run {
            mAdapter = AuctionOptionAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 4).apply {
                spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = if (mList[position].itemType == 0) 4 else 1
                }
            }
            mAdapter.setOnItemChildClickListener { _, view, position ->
                mPosition = position
                if (view.id == R.id.tv_title) {
                    mAdapter.setPosition(position)
                }
            }
        }

        tv_confirm.setOnClickListener {
            val bean = mList[mPosition]
            if (mType == "security_deposit") {
                mOnDialogCallback?.onDialog("security_deposit", mPriceList[bean.parent_id])
            } else {
                val timestamp = when {
                    bean.belong == 1 -> mSecondList[bean.parent_id] * 1000 * 60
                    bean.belong == 2 -> mHourList[bean.parent_id] * 1000 * 60 * 60
                    else -> 0
                }
                mOnSelectTimeCallback?.onSelectTime(timestamp, bean.belong)
            }
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
        if (null != mOnSelectTimeCallback) {
            mOnSelectTimeCallback = null
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    interface OnSelectTimeCallback {
        fun onSelectTime(timestamp: Int, type: Int)
    }

}
