package com.miyin.zhenbaoqi.ui.shopcart.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.CartGoodsListBean
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.callback.OnTabSelectCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.home.adapter.NewVipAdapter
import com.miyin.zhenbaoqi.ui.mine.adapter.FootprintAdapter
import com.miyin.zhenbaoqi.ui.shopcart.adapter.ShopCartAdapter
import com.miyin.zhenbaoqi.ui.shopcart.contract.ShopCartContract
import com.miyin.zhenbaoqi.ui.shopcart.presenter.ShopCartPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsPayActivity
import com.miyin.zhenbaoqi.utils.ToastUtils
import com.miyin.zhenbaoqi.widget.AddedView
import kotlinx.android.synthetic.main.dialog_add_goods_count.*
import kotlinx.android.synthetic.main.fragment_shop_cart.*
import kotlinx.android.synthetic.main.layout_refresh.*

class ShopCartFragment : BaseListFragment<ShopCartContract.IView, ShopCartContract.IPresenter>(), ShopCartContract.IView {

    private var mList = mutableListOf<CartGoodsListBean.DataBeanX.DataBean>()//页面没有及时刷新
    private lateinit var mAdapter: ShopCartAdapter
    private var mOnTabSelectCallback:OnTabSelectCallback? = null
    private  var mNumber =1
    companion object {
        fun newInstance() = ShopCartFragment()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnTabSelectCallback = context as OnTabSelectCallback

    }
    override fun useImmersionBar() = true


    override fun getContentView() = R.layout.fragment_shop_cart

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()
        mImmersionBar.titleBar(title_bar).init()
        recycler_view.run {
            mAdapter = ShopCartAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            mAdapter.setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.ll_plus ->{//只是表面更新，具体更新需要再次调用添加购物车接口
                        mList[position].cartNumber++
                        adapter.notifyDataSetChanged()
                    }
                    R.id.ll_select ->{
                        ToastUtils.showToast("你弹一下试试？？")
                    }

                }

            }
        }
        setOnClickListener()
    }

    override fun initData() {
        mPresenter?.getShopCartList(mPage,mCount)
    }

    override fun createPresenter() = ShopCartPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override   fun reload() {
        showLoading()
        initData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun getShopCartListSuccess(bean: CartGoodsListBean) {
        with(bean.data) {
            mList.clear()
            mList = this?.data!!.toMutableList()
            mAdapter?.setNewData(mList)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
        }
    }


}
