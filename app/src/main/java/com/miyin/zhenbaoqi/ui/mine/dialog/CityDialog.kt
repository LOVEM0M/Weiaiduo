package com.miyin.zhenbaoqi.ui.mine.dialog

import android.content.Context
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ui.mine.adapter.CityAdapter
import kotlinx.android.synthetic.main.dialog_city.*
import kotlinx.android.synthetic.main.layout_refresh.view.*
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class CityDialog : BaseDialogFragment() {

    private val mTitleList = mutableListOf<String>()
    private val mViewList = mutableListOf<View>()
    private lateinit var mAdapter: MyAdapter
    private var mProvinceList = mutableListOf<CityBean.CityListBean>()
    private var mCityList = mutableListOf<CityBean.CityListBean>()
    private var mCountyList = mutableListOf<CityBean.CityListBean>()
    private lateinit var mProvinceAdapter: CityAdapter
    private lateinit var mCityAdapter: CityAdapter
    private lateinit var mCountyAdapter: CityAdapter
    private var mProvinceView: View? = null
    private var mCityView: View? = null
    private var mCountyView: View? = null
    private var mProvincePosition = -1
    private var mCityPosition = -1
    private var mCountyPosition = -1
    private var mOnCitySelectCallback: OnCitySelectCallback? = null

    companion object {
        fun newInstance(list: List<CityBean.CityListBean>) = CityDialog().apply {
            arguments = Bundle().apply {
                putSerializable("list", list as Serializable)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnCitySelectCallback = context as OnCitySelectCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mProvinceList = (getSerializable("list") as List<CityBean.CityListBean>).toMutableList()
        }
        return R.layout.dialog_city
    }

    override fun initView(view: View) {
        if (mTitleList.isEmpty()) {
            mTitleList.add("请选择")
        }

        if (mViewList.isEmpty()) {
            mProvinceView = View.inflate(context, R.layout.layout_refresh, null).apply {
                smart_refresh_layout.isEnabled = false
                recycler_view.run {
                    mProvinceAdapter = CityAdapter(mProvinceList)
                    adapter = mProvinceAdapter
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    mProvinceAdapter.setOnItemClickListener { _, _, position ->
                        if (mProvincePosition == position) {
                            tab_layout.getTabAt(1)?.select()
                            return@setOnItemClickListener
                        }

                        mOnCitySelectCallback?.onCitySelect(position, 0, mProvinceList[position].dict_id)
                    }
                }
                mViewList.add(this)
            }
            mCityView = View.inflate(context, R.layout.layout_refresh, null).apply {
                smart_refresh_layout.isEnabled = false
                recycler_view.run {
                    mCityAdapter = CityAdapter(mCityList)
                    adapter = mCityAdapter
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    mCityAdapter.setOnItemClickListener { _, _, position ->
                        if (mCityPosition == position) {
                            tab_layout.getTabAt(2)?.select()
                            return@setOnItemClickListener
                        }

                        mOnCitySelectCallback?.onCitySelect(position, 1, mCityList[position].dict_id)
                    }
                }
                mViewList.add(this)
            }
            mCountyView = View.inflate(context, R.layout.layout_refresh, null).apply {
                smart_refresh_layout.isEnabled = false
                recycler_view.run {
                    mCountyAdapter = CityAdapter(mCountyList)
                    adapter = mCountyAdapter
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    mCountyAdapter.setOnItemClickListener { _, _, position ->
                        if (mCountyPosition == position) {
                            return@setOnItemClickListener
                        }

                        mCountyPosition = position
                        val name = mCountyList[position].code_name ?: ""
                        mTitleList[2] = name

                        tab_layout.setupWithViewPager(view_pager)
                        mAdapter.notifyDataSetChanged()
                        tab_layout.getTabAt(2)?.select()

                        mOnCitySelectCallback?.onSelectCity(mProvinceList[mProvincePosition], mCityList[mCityPosition], mCountyList[mCountyPosition])
                        dismiss()
                    }
                }
                mViewList.add(this)
            }
        }

        mAdapter = MyAdapter()
        view_pager.adapter = mAdapter
        tab_layout.setupWithViewPager(view_pager)

        iv_close.setOnClickListener { dismiss() }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    fun setAreaList(list: List<CityBean.CityListBean>, state: Int, position: Int) {
        when (state) {
            0 -> {
                mCityList = list.toMutableList()
                mCityAdapter.setNewData(mCityList)
                mCityView?.recycler_view?.scrollToPosition(0)

                mProvincePosition = position
                val name = mProvinceList[position].code_name ?: ""
                if (mTitleList.size == 1) {
                    mTitleList.add(0, name)
                } else {
                    if (mTitleList.size > 2) {
                        mTitleList.removeAt(2)
                    }
                    mTitleList[0] = name
                    mTitleList[1] = "请选择"
                }

                tab_layout.setupWithViewPager(view_pager)
                mAdapter.notifyDataSetChanged()
                tab_layout.getTabAt(1)?.select()
            }
            1 -> {
                mCountyList = list.toMutableList()
                mCountyAdapter.setNewData(mCountyList)
                mCountyView?.recycler_view?.scrollToPosition(0)

                mCityPosition = position
                val name = mCityList[position].code_name ?: ""
                if (mTitleList.size == 2) {
                    mTitleList.add(1, name)
                } else {
                    mTitleList[1] = name
                    mTitleList[2] = "请选择"
                }

                tab_layout.setupWithViewPager(view_pager)
                mAdapter.notifyDataSetChanged()
                tab_layout.getTabAt(2)?.select()
            }
        }
    }

    private inner class MyAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = mViewList[position]
            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, obj: Any) = view == obj

        override fun getCount() = mTitleList.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mViewList[position])
        }

        override fun getPageTitle(position: Int) = mTitleList[position]
    }

    interface OnCitySelectCallback {
        fun onCitySelect(position: Int, state: Int, parentId: Int)

        fun onSelectCity(province: CityBean.CityListBean, city: CityBean.CityListBean, county: CityBean.CityListBean)
    }

}
