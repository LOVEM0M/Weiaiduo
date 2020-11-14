package com.miyin.zhenbaoqi.ui.shop.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.MerchantGoodsStoreBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.shop.contract.ShopDetailContract
import com.miyin.zhenbaoqi.ui.shop.dialog.ShopDetailAdapter
import com.miyin.zhenbaoqi.ui.shop.presenter.ShopDetailPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.AuctionDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class ShopDetailFragment : BaseListFragment<ShopDetailContract.IView, ShopDetailContract.IPresenter>(), ShopDetailContract.IView {

    private var mMerchantId = 0
    private var mType = 0
    private var mList = mutableListOf<MerchantGoodsStoreBean.ListBean>()
    private lateinit var mAdapter: ShopDetailAdapter

    companion object {
        fun newInstance(merchantId: Int, type: Int) = ShopDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("merchant_id", merchantId)
                putInt("type", type)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mMerchantId = getInt("merchant_id")
            mType = getInt("type")
        }
        return R.layout.fragment_shop_detail
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ShopDetailAdapter(mList)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            mAdapter.setOnItemClickListener { _, _, position ->
                val bean = mList[position]
                if (mType == 1) {
                    startActivity<AuctionDetailActivity>("goods_id" to bean.goods_id)
                } else {
                    startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to bean.goods_id)
                }
            }
        }

        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getMerchantGoodsList(mPage, mMerchantId, mCount, mType)
    }

    override fun createPresenter() = ShopDetailPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getMerchantGoodsListSuccess(bean: MerchantGoodsStoreBean) {
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

}
