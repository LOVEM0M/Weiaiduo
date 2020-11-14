package com.miyin.zhenbaoqi.ui.mine.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.ShopAttentionBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.ShopAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.ShopContract
import com.miyin.zhenbaoqi.ui.mine.presenter.ShopPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import kotlinx.android.synthetic.main.layout_refresh.*

class ShopFragment : BaseListFragment<ShopContract.IView, ShopContract.IPresenter>(), ShopContract.IView {

    private var mList = mutableListOf<ShopAttentionBean.ShopListBean>()
    private lateinit var mAdapter: ShopAdapter

    companion object {
        fun newInstance() = ShopFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = ShopAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tv_entry_shop) {
                    startActivity<MerchantMessageActivity>("merchant_id" to mList[position].merchants_id)
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun getContentView() = R.layout.fragment_shop

    override fun initData() {
        mPresenter?.merchantList(mPage, mCount)
    }

    override fun createPresenter() = ShopPresenter()

    override fun reload() {
        mPage = 1
        initData()
    }

    override fun merchantListSuccess(bean: ShopAttentionBean) {
        with(bean) {
            if (bean.current_page == 1) {
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
