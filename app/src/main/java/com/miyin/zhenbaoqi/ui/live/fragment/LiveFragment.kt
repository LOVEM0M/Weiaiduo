package com.miyin.zhenbaoqi.ui.live.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseStateFragment
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.home.adapter.LiveCategoryAdapter
import com.miyin.zhenbaoqi.ui.live.contract.LiveContract
import com.miyin.zhenbaoqi.ui.live.presenter.LivePresenter
import com.miyin.zhenbaoqi.widget.PopWindow
import kotlinx.android.synthetic.main.fragment_live.*
import kotlinx.android.synthetic.main.pop_category.view.*

class LiveFragment : BaseStateFragment<LiveContract.IView, LiveContract.IPresenter>(), LiveContract.IView {

    private val mTitleList = mutableListOf<String>()
    private val mIndexList = mutableListOf<Int>()
    private val mFragmentList = mutableListOf<Fragment>()
    private lateinit var mPopWindow: PopWindow
    private lateinit var mAdapter: LiveCategoryAdapter

    companion object {
        fun newInstance() = LiveFragment()
    }

    override fun getContentView() = R.layout.fragment_live

    override fun useImmersionBar() = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        mImmersionBar.titleBar(title_bar).statusBarDarkFont(true).init()

        showLoading()

//        tab_layout.getTitleView(0).run {
//            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_heart, 0, 0, 0)
//            compoundDrawablePadding = context.getDimensionPixelSize(R.dimen.dp_10)
//        }
//        tab_layout.getTitleView(1).run {
//            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_live_hot, 0, 0, 0)
//            compoundDrawablePadding = context.getDimensionPixelSize(R.dimen.dp_10)
//        }

        initPopupWindow()
        iv_category.setOnClickListener {
            mPopWindow.showAsDropDown(title_bar)
        }
    }

    override fun initData() {
        mPresenter?.getCategoryList()
    }

    override fun createPresenter() = LivePresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    private fun initPopupWindow() {
        val view = View.inflate(context, R.layout.pop_category, null)
        mPopWindow = PopWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isFocusable = true
            isOutsideTouchable = true

            view.run {
                ll_container.setOnClickListener { dismiss() }
                recycler_view.run {
                    mAdapter = LiveCategoryAdapter(mTitleList)
                    adapter = mAdapter
                    layoutManager = GridLayoutManager(context, 4)
                    mAdapter.setOnItemChildClickListener { _, view, position ->
                        if (view.id == R.id.tv_title) {
                            mAdapter.setPosition(position)
                            view_pager.currentItem = position
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun getCategoryListSuccess(bean: CityBean) {
        bean.data?.forEach {
            mTitleList.add(it.codeName ?: "")
            mIndexList.add(it.dictId)
        }

        mTitleList.forEachIndexed { index, it ->
            mFragmentList.add(LiveCategoryFragment.newInstance(mIndexList[index], it))
        }
        view_pager.adapter = ViewPagerAdapter(childFragmentManager, mTitleList, mFragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(position: Int) {
                mAdapter.setPosition(position)
            }
        })
    }

    internal inner class ViewPagerAdapter : FragmentStatePagerAdapter(childFragmentManager) {

        override fun getItem(i: Int) = mFragmentList[i]

        override fun getCount() = mTitleList.size

        override fun getPageTitle(position: Int) = mTitleList[position]

    }

}
