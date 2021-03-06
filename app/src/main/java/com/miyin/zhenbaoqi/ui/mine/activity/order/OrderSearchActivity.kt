package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ui.mine.fragment.OrderFragment
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils.hideKeyboard
import kotlinx.android.synthetic.main.activity_order_search.*

class OrderSearchActivity : BaseActivity() {

    private var mContent: String? = null
    private lateinit var mOrderFragment: OrderFragment

    override fun getContentView() = R.layout.activity_order_search

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("订单搜索")
        immersionBar { statusBarDarkFont(true) }

        mOrderFragment = supportFragmentManager.findFragmentByTag("order_list_fragment") as OrderFragment

        et_content.run {
            addTextChangedListener(object : EditWatcher() {
                override fun afterTextChanged(editable: Editable?) {
                    mContent = editable.toString().trim { it <= ' ' }
                }
            })
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 点击搜索的时候隐藏软键盘
                    hideKeyboard(et_content)
                    // 在这里写搜索的操作,一般都是网络请求数据
                    mOrderFragment.setSearchVal(mContent)
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    override fun initData() {

    }

}
