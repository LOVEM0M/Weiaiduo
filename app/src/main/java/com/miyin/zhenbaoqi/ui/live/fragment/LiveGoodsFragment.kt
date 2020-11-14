package com.miyin.zhenbaoqi.ui.live.fragment

import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.LiveGoodsBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.live.adapter.LiveGoodsAdapter
import com.miyin.zhenbaoqi.ui.live.contract.LiveGoodsContract
import com.miyin.zhenbaoqi.ui.live.presenter.LiveGoodsPresenter
import com.miyin.zhenbaoqi.ui.shop.activity.OperateGoodsActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsPayActivity
import com.miyin.zhenbaoqi.ui.sort.dialog.AddGoodsCountDialog
import com.miyin.zhenbaoqi.utils.MaxImageView
import kotlinx.android.synthetic.main.fragment_live_goods.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LiveGoodsFragment : BaseListFragment<LiveGoodsContract.IView, LiveGoodsContract.IPresenter>(), LiveGoodsContract.IView, OnDialogCallback {

    private var mTitle: String = ""
    private var mList = mutableListOf<LiveGoodsBean.LiveGoodsDataBean>()
    private var mAdapter: LiveGoodsAdapter? = null
    private var mState = 0
    private var mRroomId = 0
    private var mPosition = 0

    companion object {
        fun newInstance(title: String, state: Int, room_id: Int) = LiveGoodsFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putInt("state", state)
                putInt("room_id", room_id)
            }
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mTitle = getString("title").toString()
            mState = getInt("state")
            mRroomId = getInt("room_id")
        }
        return R.layout.fragment_live_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = LiveGoodsAdapter(mList, mTitle)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            mAdapter?.onItemChildClickListener = mOnItemChildClickListener
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    private val mOnItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        mPosition = position
        val bean = mList[position]
        when (view.id) {
            R.id.cl_container -> {
                when (mTitle) {
                    "编辑中的宝贝" -> {
                        startActivity<OperateGoodsActivity>("title" to "编辑商品", "room_id" to mRroomId, "bean" to bean)
                    }
                }
            }
            R.id.tv_delete -> {
                AlertDialog.Builder(activity!!)
                        .setTitle("提示")
                        .setMessage("是否删除该商品？")
                        .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                            mPresenter?.updateShelfGoods(bean.goods_id, 2)
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
            }
            R.id.tv_operate -> {
                when (mTitle) {
                    "秒杀中" -> {
                        if (bean.is_restriction == 1) {
                            val goodsName = bean.goods_name ?: ""
                            val dialog = AddGoodsCountDialog.newInstance(bean.goods_img, goodsName, bean.goods_amount, bean.inventory)
                            dialog.setOnDialogCallback(this@LiveGoodsFragment)
                            dialog.show(fragmentManager!!, "addGoodsCount")
                        } else {
                            EventBus.getDefault().post("hideLiveGoodsDialog")
                            startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE, "goods_name" to bean.goods_name,
                                    "goods_freight" to bean.goods_freight, "goods_img" to bean.goods_img, "price" to bean.goods_amount,
                                    "goods_id" to bean.goods_id, "from" to "general", "shop_name" to "", "is_seven" to (bean.is_seven == 0)) // TODO 待更改 shop_name
                        }
                    }
                    "拍卖中" -> {
                        when {
                            bean.service_time < bean.start_time_timestamp -> {
                                showToast("未到开拍时间")
                            }
                            bean.service_time > bean.end_time_timestamp -> {
                                showToast("竞拍已结束")
                            }
                            else -> {
                                val map = ArrayMap<String, Any>().apply {
                                    put("type", "add_price")
                                    put("goods_id", bean.goods_id)
                                    put("add_price", bean.add_amount)
                                }
                                EventBus.getDefault().post(map)
                            }
                        }
                    }
                    "全部宝贝" -> {
                        var state = bean.state
                        if (state == 0) {
                            state = 1
                        } else {
                            state = 0
                        }
                        mPresenter?.updateShelfGoods(bean.goods_id, state)
                    }
                    "编辑中的宝贝" -> {
                        mPresenter?.updateShelfGoods(bean.goods_id, 0)
                    }
                }
            }
            R.id.iv_cover -> {
                val goodsImg = bean.goods_img
                if (goodsImg.isNullOrEmpty()) {
                    return@OnItemChildClickListener
                }
                val list = mutableListOf<String>()
                if (goodsImg.contains(",")) {
                    goodsImg.split(",").forEach { list.add(it) }
                } else {
                    list.add(goodsImg)
                }
                MaxImageView.maxImageView(activity!!, list, position)
            }
        }
    }

    override fun initData() {
        if (mList.isEmpty()) {
            showEmpty()
        } else {
            showNormal()
        }
        when (mTitle) {
            "秒杀中" -> {
                mPresenter?.obtainLiveRoomGoodsList(mPage, mCount, mTitle, mRroomId)

            }
            "拍卖中" -> {
                mPresenter?.obtainLiveRoomGoodsList(mPage, mCount, mTitle, mRroomId)

            }
            "全部宝贝" -> {
                mPresenter?.obtainLiveGoodsList(mPage, mCount, mTitle)

            }
            "编辑中的宝贝" -> {
                mPresenter?.obtainLiveGoodsList(mPage, mCount, mTitle)
            }
        }
    }

    override fun createPresenter() = LiveGoodsPresenter()

    override fun onDestroyView() {
        MaxImageView.clear()
        super.onDestroyView()
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun obtainLiveGoodsDataSuccess(bean: LiveGoodsBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter?.setNewData(mList)
                recycler_view.scrollToPosition(0)
            } else {
                mAdapter?.addData(list!!)
            }
            val hasMore = current_page != pages
            smart_refresh_layout.setEnableLoadMore(hasMore)
            if (hasMore) {
                mAdapter?.removeAllFooterView()
            } else {
                mAdapter?.addNoMoreDataFooter()
            }
        }
    }

    override fun showEmptyView() {
        mList.clear()
        mAdapter?.setNewData(mList)

        val emptyView = LayoutInflater.from(context).inflate(R.layout.view_empty, recycler_view, false)
        mAdapter?.emptyView = emptyView
        smart_refresh_layout.setEnableLoadMore(false)
    }

    override fun updateShelfGoodsSuccess(state: Int) {
        mPage = 1
        initData()

        if (mTitle == "全部宝贝") {
            EventBus.getDefault().post("editGoods")
        } else if (mTitle == "编辑中的宝贝") {
            EventBus.getDefault().post("allGoods")
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        EventBus.getDefault().post("hideLiveGoodsDialog")
        mList[mPosition].run {
            startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE, "goods_name" to goods_name,
                    "goods_img" to goods_img, "price" to goods_amount, "goods_id" to goods_id, "from" to "general",
                    "goods_freight" to goods_freight, "count" to obj, "shop_name" to "", "is_seven" to (is_seven == 0))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "refreshCommonGoods") {
            mPage = 1
            initData()
        } else if (msg == "editGoods" && mTitle == "编辑中的宝贝") {
            mPage = 1
            initData()
        } else if (msg == "allGoods" && mTitle == "全部宝贝") {
            mPage = 1
            initData()
        }
    }

}
