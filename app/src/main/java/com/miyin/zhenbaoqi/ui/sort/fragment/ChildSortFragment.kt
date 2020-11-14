package com.miyin.zhenbaoqi.ui.sort.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseMvpFragment
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.CommodityBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.sort.contract.ChildSortContract
import com.miyin.zhenbaoqi.ui.sort.presenter.ChildSortPresenter
import com.miyin.zhenbaoqi.ui.sort.activity.CategoryActivity
import com.miyin.zhenbaoqi.ui.sort.adapter.RightAdapter
import kotlinx.android.synthetic.main.fragment_child_sort.*

class ChildSortFragment : BaseMvpFragment<ChildSortContract.IView, ChildSortContract.IPresenter>(), ChildSortContract.IView {

    private var mList = mutableListOf<CommodityBean>()
    private lateinit var mAdapter: RightAdapter
    private var mBean: CityBean.CityListBean? = null

    companion object {
        fun newInstance(bean: CityBean.CityListBean) = ChildSortFragment().apply {
            arguments = Bundle().apply {
                putSerializable("bean", bean)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mBean = getSerializable("bean") as CityBean.CityListBean
        }
        return R.layout.fragment_child_sort
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
//        showLoading()

        recycler_view.run {
            mAdapter = RightAdapter(mList)
            adapter = mAdapter
            val gridLayoutManager = GridLayoutManager(context, 3)
            layoutManager = gridLayoutManager
            mAdapter.setOnItemClickListener { _, _, position ->
                mBean?.run {
                    val bean = mList[position]
                    if (bean.itemType == 1) {
                        val cate1Id = if (dict_id == 0) bean.parentId else dict_id
                        startActivity<CategoryActivity>("cate_id1" to cate1Id, "cate_id2" to bean.dictId,
                                "cate2_name" to bean.title, "cate_cover" to bean.cover)
                    }
                }
            }
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (mList[position].itemType == 0) 3 else 1
            }
        }
    }

    override fun initData() {
        mBean?.run {
            if (dict_id == 0) {
                mPresenter?.getRecommend()
            } else {
                mPresenter?.getSecondLevel(dict_id)
            }
        }
    }

    override fun createPresenter() = ChildSortPresenter()

    override fun getRecommendSuccess(bean: CityBean) {
        mBean?.run {
            mList.add(CommodityBean(0, code_name ?: "", dictId = dict_id))
            bean.dicts?.forEach {
                mList.add(CommodityBean(1, it.code_name!!, it.code_value, it.dict_id, parentId = it.parent_id))
            }

            mAdapter.setNewData(mList)
        }
    }

    override fun getSecondLevelSuccess(bean: CityBean) {
        mBean?.run {
            mList.add(CommodityBean(0, code_name ?: "", dictId = dict_id))
            bean.dicts?.forEach {
                mList.add(CommodityBean(1, it.code_name!!, it.code_value, it.dict_id))
            }
            mAdapter.setNewData(mList)
        }
    }

}