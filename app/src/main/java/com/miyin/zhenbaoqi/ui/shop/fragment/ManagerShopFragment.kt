package com.miyin.zhenbaoqi.ui.shop.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.MerchantGoodsBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.ManagerShopAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.ManagerShopContract
import com.miyin.zhenbaoqi.ui.shop.presenter.ManagerShopPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ManagerShopFragment : BaseListFragment<ManagerShopContract.IView, ManagerShopContract.IPresenter>(), ManagerShopContract.IView {

    private var mState = 0
    private var mList = mutableListOf<MerchantGoodsBean.ListBean>()
    private lateinit var mAdapter: ManagerShopAdapter

    companion object {
        fun newInstance(state: Int) = ManagerShopFragment().apply {
            arguments = Bundle().apply {
                putInt("state", state)
            }
        }
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        arguments?.run {
            mState = getInt("state")
        }
        return R.layout.fragment_manager_shop
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ManagerShopAdapter(mList, mState)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.onItemChildClickListener = mOnItemChildClickListener
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getMerchantGoodsList(mPage, mCount, mState)
    }

    override fun createPresenter() = ManagerShopPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onRefreshEvent("refreshTakeOff")
        }
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    private val mOnItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        val bean = mList[position]
        when (view.id) {
            R.id.fl_container -> {
                startActivity<GoodsDetailActivity>("goods_id" to bean.goods_id, "source" to 1,
                        "state" to mState)
            }
            R.id.tv_sticky -> {
                if (mState == 0) {
                    mPresenter?.updateMerchantGoodsState(bean.goods_id, 3)
                } else {
                    mPresenter?.updateMerchantGoodsState(bean.goods_id, 4)
                }
            }
            R.id.tv_take_off -> mPresenter?.updateMerchantGoodsState(bean.goods_id, 1)
            R.id.tv_relaunch -> mPresenter?.updateMerchantGoodsState(bean.goods_id, 0)
            R.id.tv_delete -> mPresenter?.updateMerchantGoodsState(bean.goods_id, 2)
        }
    }

    override fun getMerchantGoodsListSuccess(bean: MerchantGoodsBean) {
        with(bean) {
            if (current_page == 1) {
                mList = list!!.toMutableList()
                mAdapter.setNewData(mList)
            } else {
                mAdapter.addData(list!!)
            }
            smart_refresh_layout.setEnableLoadMore(current_page != pages)
        }
    }

    override fun updateMerchantGoodsStateSuccess(state: Int) {
        reload()
        when (state) {
            1 -> EventBus.getDefault().post("refreshTakeOff")
            0 -> EventBus.getDefault().post("refreshShelves")
            3 -> EventBus.getDefault().post("refreshTop")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(tag: String) {
        if (tag == "refreshTakeOff" && mState == 1) {
            reload()
        } else if (tag == "refreshShelves" && mState == 0) {
            reload()
        } else if (tag == "refreshTop" && mState == 2) {
            reload()
        }
    }

}
