package com.miyin.zhenbaoqi.ui.home.activity

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.home.fragment.SearchGoodsFragment
import com.miyin.zhenbaoqi.ui.home.fragment.SearchShopFragment
import com.miyin.zhenbaoqi.ui.home.fragment.SearchVideoFragment
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.activity_search_result.tab_layout
import kotlinx.android.synthetic.main.activity_search_result.view_pager

class SearchResultActivity :BaseActivity() {

    private var mContent: String? = null

    override fun getContentView(): Int {
        mContent = intent.getStringExtra("content")
        return R.layout.activity_search_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()

        val content = mContent ?: ""
        val titleList = listOf("一口价", "上架", "店铺"/*, "视频"*/)
        val fragmentList = listOf(SearchGoodsFragment.newInstance(content, 0), SearchGoodsFragment.newInstance(content, 1),
                SearchShopFragment.newInstance(content), SearchVideoFragment.newInstance(content))
        val adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList as List<Fragment>)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)

        et_content.setText(mContent)
        et_content.addTextChangedListener(mTextWatcher)
        et_content.setOnEditorActionListener(mOnEditorActionListener)

        setOnClickListener(iv_back, tv_search)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_search -> searchParam()
        }
    }

    private val mTextWatcher = object : EditWatcher() {
        override fun afterTextChanged(editable: Editable?) {
            mContent = editable.toString().trim { it <= ' ' }
        }
    }

    private val mOnEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchParam()
            return@OnEditorActionListener true
        }
        false
    }

    private fun searchParam() {
        if (mContent.isNullOrEmpty()) {
            ToastUtils.showToast("请输入搜索内容")
            return
        }
        KeyboardUtils.hideKeyboard(et_content)
        if (null != view_pager) {
            when (val position = view_pager.currentItem) {
                0 -> {
                    val fragment = (view_pager.adapter as ViewPagerAdapter).getItem(position) as SearchGoodsFragment
                    fragment.researchGoods(mContent!!)
                }
                1 -> {
                    val fragment = (view_pager.adapter as ViewPagerAdapter).getItem(position) as SearchGoodsFragment
                    fragment.researchGoods(mContent!!)
                }
                2 -> {
                    val fragment = (view_pager.adapter as ViewPagerAdapter).getItem(position) as SearchShopFragment
                    fragment.researchGoods(mContent!!)
                }
                3 -> {
                    val fragment = (view_pager.adapter as ViewPagerAdapter).getItem(position) as SearchVideoFragment
                    fragment.researchGoods(mContent!!)
                }
            }
        }
    }

}
