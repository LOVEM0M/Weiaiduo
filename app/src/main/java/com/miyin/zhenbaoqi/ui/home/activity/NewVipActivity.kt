package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.VipFirstFreegoodsBean
import com.miyin.zhenbaoqi.bean.takeThreeVipBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.home.adapter.NewVipAdapter
import com.miyin.zhenbaoqi.ui.home.contract.NewVipContract
import com.miyin.zhenbaoqi.ui.home.presenter.NewVipPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsDetailActivity
import com.miyin.zhenbaoqi.ui.sort.activity.GoodsPayActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_new_vip.*
import kotlinx.android.synthetic.main.layout_refresh.*


@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class NewVipActivity : BaseListActivity<NewVipContract.IView, NewVipContract.IPresenter>(), NewVipContract.IView {
    private var mList = mutableListOf<VipFirstFreegoodsBean.DataBeanX.DataBean>()
    private lateinit var mAdapter: NewVipAdapter
    private var mVipType = 0
    companion object {
        fun newInstance() = NewVipActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mVipType = SPUtils.getInt("vipType")
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        recycler_view.run {
            mAdapter = NewVipAdapter(mList)
            mAdapter.setHeaderAndEmpty(true)
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mAdapter.setOnItemClickListener { _, _, position ->
                if(mVipType==3){
                    startActivityForResult<GoodsPayActivity>(Constant.INTENT_REQUEST_CODE)//去到立即抢购页面
                }
                else
                    ToastUtils.showToast("很抱歉，非正式VIP会员不能购买~")
            }
        }
        setOnClickListener(ll_goback)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_goback -> finish()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_new_vip
    }
    override fun reload() {
        initData()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun initData() {
        mPresenter?.getVipFirstFreegoodsList(mPage,mCount)
    }

    override fun createPresenter() = NewVipPresenter()

    override fun getVipFirstFreegoodsListSuccess(bean: VipFirstFreegoodsBean) {
        with(bean.data) {
//            if (this!!.total == 1) {//这个page
//                mList = data!!.toMutableList()
//                mAdapter?.setNewData(mList)
//            } else {
                mAdapter?.addData(this?.data!!)
//            }
        }

//            val hasMore = current_page != pages
//            smart_refresh_layout.setEnableLoadMore(hasMore)
//            if (hasMore) {
//                mAdapter.removeAllFooterView()
//            } else {
//                mAdapter.addNoMoreDataFooter()
//            }
        }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
        }
    }
}



