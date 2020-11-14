package com.miyin.zhenbaoqi.ui.shop.activity.order

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ui.shop.adapter.BulkShipAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.BulkShipContract
import com.miyin.zhenbaoqi.ui.shop.presenter.BulkShipPresenter
import com.miyin.zhenbaoqi.utils.ShadowUtils
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_bulk_ship.*
import kotlinx.android.synthetic.main.layout_refresh.*
import kotlinx.android.synthetic.main.popup_bulk_ship.view.*
import kotlinx.android.synthetic.main.title_bar.*

class BulkShipActivity : BaseListActivity<BulkShipContract.IView, BulkShipContract.IPresenter>(), BulkShipContract.IView {

    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: BulkShipAdapter

    override fun getContentView() = R.layout.activity_bulk_ship

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("批量发货", rightTitle = "按日期")
        immersionBar { statusBarDarkFont(true) }
        showLoading()

        tv_name.text = SpanUtils()
                .append("共有")
                .append("13").setForegroundColor(0xFFDF2626.toInt())
                .append("个用户，合计")
                .append("30").setForegroundColor(0xFFDF2626.toInt())
                .appendLine("笔订单")
                .append("同一个用户可使用批量发货功能")
                .create()

        recycler_view.run {
            repeat((0..6).count()) { mList.add("") }
            mAdapter = BulkShipAdapter(mList)
            adapter = mAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }

        tv_right_title.tag = 0
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
    }

    override fun onRightClick() {
        val view = View.inflate(this, R.layout.popup_bulk_ship, null)
        ShadowUtils.apply(view, ShadowUtils.Config()
                .setShadowColor(0x40040000)
                .setShadowRadius(getDimension(R.dimen.dp_3))
                .setShadowSize(getDimensionPixelSize(R.dimen.dp_4)))
        val popupWindow = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(0x00000000))
            showAsDropDown(tv_right_title, 0, 0, Gravity.END)
        }
        view.run {
            tv_date_time.setOnClickListener {
                if (tv_right_title.tag != "0") {
                    tv_right_title.tag = 0
                    tv_right_title.text = "按日期"
                }
                popupWindow.dismiss()
            }
            tv_user_info.setOnClickListener {
                if (tv_right_title.tag != "1") {
                    tv_right_title.tag = "1"
                    tv_right_title.text = "按用户"
                }
                popupWindow.dismiss()
            }
        }
    }

    override fun createPresenter() = BulkShipPresenter()

}
