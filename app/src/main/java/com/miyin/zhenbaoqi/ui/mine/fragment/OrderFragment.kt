package com.miyin.zhenbaoqi.ui.mine.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.OrderBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.activity.order.*
import com.miyin.zhenbaoqi.ui.mine.adapter.OrderAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.OrderContract
import com.miyin.zhenbaoqi.ui.mine.presenter.OrderPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderFragment : BaseListFragment<OrderContract.IView, OrderContract.IPresenter>(), OrderContract.IView {

    private var mState = -1
    private var mList = mutableListOf<OrderBean.ListBean>()
    private lateinit var mAdapter: OrderAdapter
    private var mSearchVal: String? = null

    companion object {
        fun newInstance(state: Int) = OrderFragment().apply {
            val bundle = Bundle()
            bundle.putInt("state", state)
            arguments = bundle
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_order
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        if (mState != -1) {
            showLoading()
        }

        recycler_view.run {
            mAdapter = OrderAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                startActivityForResult<OrderDetailActivity>(Constant.INTENT_REQUEST_CODE, "state" to bean.state,
                        "order_number" to bean.order_number, "remark" to bean.remark)
            }
            mAdapter.onItemChildClickListener = mOnItemChildClickListener
        }
        refreshAndLoadMore(smart_refresh_layout) {
            if (mState != -1) {
                initData()
            } else {
                setSearchVal(mSearchVal)
            }
        }
    }

    override fun initData() {
        if (mState != -1) {
            mPresenter?.getOrderList(mPage, mCount, mState)
        }
    }

    private val mOnItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        val bean = mList[position]
        when (view?.id) {
            R.id.tv_left_title -> {
                when (bean.state) {
                    3 -> startActivity<OrderDetailActivity>("state" to bean.state, "order_number" to bean.order_number, "remark" to bean.remark)
                    4 -> startActivity<LogisticsActivity>("order_number" to bean.order_number)
                }
            }
            R.id.tv_middle_title -> {
                when (bean.state) {
                    2 -> {
                        // mPresenter?.orderCancel(bean.order_number!!)
                    }
                    3 -> {
                        startActivity<LogisticsActivity>("order_number" to bean.order_number)
                    }
                    4 -> {
                        startActivity<OrderDetailActivity>("order_number" to bean.order_number, "state" to bean.state, "remark" to bean.remark)
                    }
                    5 -> mPresenter?.deleteOrder(bean.order_number ?: "")
                }
            }
            R.id.tv_right_title -> {
                when (bean.state) {
                    1 -> {
                        startActivity<OrderDetailActivity>("state" to bean.state, "order_number" to bean.order_number, "remark" to bean.remark)
                    }
                    2 -> {
                        AlertDialog.Builder(activity)
                                .setTitle("温馨提示")
                                .setMessage("提供商家发货已通知")
                                .setPositiveButton(getString(R.string.confirm), null)
                                .show()
                    }
                    3 -> {
                        mPresenter?.confirmReceive(bean.order_number ?: "")
                    }
                    4 -> {
                        startActivity<GoodsEvalActivity>("order_number" to bean.order_number)
                    }
                    5 -> {
                        startActivity<GoodsDetailActivity>("goods_id" to bean.goods_id)
                        activity?.finish()
                    }
                    6, 7, 8 -> {
                        startActivity<AfterSaleStateActivity>("order_number" to bean.order_number)
                    }
                    9 -> mPresenter?.deleteOrder(bean.order_number ?: "")
                }
            }
        }
    }

    override fun createPresenter() = OrderPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    fun setSearchVal(content: String?) {
        mSearchVal = content
        if (content.isNullOrEmpty()) {
            return
        }

        showLoading()
        mPresenter?.searchOrder(1, 50, content)
    }

    override fun getOrderListSuccess(bean: OrderBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter.removeAllFooterView()
            } else {
                mAdapter.addNoMoreDataFooter()
            }
        }
    }

    override fun confirmReceiveSuccess() {
        EventBus.getDefault().post("confirmReceive")
        reload()
    }

    override fun orderCancelSuccess() {
        reload()
    }

    override fun orderDeleteSuccess() {
        reload()
    }

    override fun searchOrderSuccess(bean: OrderBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "confirmReceive" && mState == 4) {
            reload()
        }
    }

}
