package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.FirstCategorySecondBean
import com.miyin.zhenbaoqi.bean.SecondCategoryGoodsBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ui.home.adapter.FirstCategorySecondAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.SecondCategoryGoodsAdapter
import com.miyin.zhenbaoqi.ui.home.contract.SearchContract
import com.miyin.zhenbaoqi.ui.home.contract.SnacksContract
import com.miyin.zhenbaoqi.ui.home.fragment.HomeFragment
import com.miyin.zhenbaoqi.ui.home.presenter.SnacksPresenter
import com.tencent.qcloud.tim.uikit.base.BaseActvity
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_popular_snacks.*
import kotlinx.android.synthetic.main.fragment_popular_snacks.ll_search_home
import kotlinx.android.synthetic.main.item_home_classify4.*
import kotlinx.android.synthetic.main.layout_refresh.*

@Suppress("UNCHECKED_CAST")
@SuppressLint("SetTextI18n")
class SnacksActivity: BaseListActivity<SnacksContract.IView, SnacksContract.IPresenter>(), SnacksContract.IView{
    private var mName: String? = null
    private var cateId1 = 0
    private var cateId2 = 10//初始值设为10，凭运气看有没有数据
    private lateinit var mTabAdapter: FirstCategorySecondAdapter
    private lateinit var mBottomAdapter: SecondCategoryGoodsAdapter
    private var mTabList = mutableListOf<FirstCategorySecondBean.DataBean>()
    private var mBottomList = mutableListOf<SecondCategoryGoodsBean.DataBeanX.DataBean>()
    companion object {
        fun newInstance() = SnacksActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()
        tv_top_title.setText(mName)
        recycler_view_tab.run {
            mTabAdapter = FirstCategorySecondAdapter(mTabList)
            mTabAdapter.setHeaderAndEmpty(true)
            adapter = mTabAdapter
            layoutManager = StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
            mTabAdapter.setOnItemClickListener { _, _, position ->
                cateId2 = mTabList[position].dictId
                mPresenter?.getSecondCategoryGoodsList(cateId2,mPage,mCount)
            }
        }
        recycler_view.run {
            mBottomAdapter = SecondCategoryGoodsAdapter(mBottomList)
//            mBottomAdapter.setHeaderAndEmpty(true)
            adapter = mBottomAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mBottomAdapter.setOnItemClickListener { _, _, position ->
          //     startActivityForResult<GoodsDetailActivity>(Constant.INTENT_REQUEST_CODE, "goods_id" to mBottomList[position].goods_id)
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
        mName = intent.getStringExtra("title")
        cateId1 = intent.getIntExtra("cateId1",0)
        return R.layout.fragment_popular_snacks
    }

    override fun initData() {
        mPresenter?.getFirstCategorySecondList(cateId1)
        mPresenter?.getSecondCategoryGoodsList(cateId2,mPage,mCount)
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

    override fun createPresenter() = SnacksPresenter()
    override fun getFirstCategorySecondListSuccess(bean: FirstCategorySecondBean) {
                mTabList = bean.data!!.toMutableList()
                mTabAdapter?.setNewData(mTabList)
    }


    override fun getSecondCategoryGoodsListSuccess(bean: SecondCategoryGoodsBean) {
        with(bean) {
            if (current_page == 1) {
                mBottomList = data!!.data!!.toMutableList()
                mBottomAdapter?.setNewData(mBottomList)
                smart_refresh_layout.finishRefresh()
            } else {
                mBottomList.clear()
                mBottomAdapter?.addData(data!!.data!!)
                mBottomAdapter.notifyDataSetChanged()
                smart_refresh_layout.finishLoadMore()
            }

        }
    }

    override fun onFailure(msg: String, type: Int) {
        when (type) {
            0 -> {
//                mBottomAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
        }
    }

}