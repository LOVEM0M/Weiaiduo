package com.miyin.zhenbaoqi.ui.home.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ui.home.contract.SearchContract
import com.miyin.zhenbaoqi.ui.home.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.SearchBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils
import com.miyin.zhenbaoqi.widget.flow_layout.FlowLayout
import com.miyin.zhenbaoqi.widget.flow_layout.TagAdapter
import com.miyin.zhenbaoqi.widget.flow_layout.TagFlowLayout
import com.noober.background.drawable.DrawableCreator

class SearchActivity : com.miyin.zhenbaoqi.base.activity.BaseMvpActivity<SearchContract.IView, SearchContract.IPresenter>(), SearchContract.IView {

    private val mRecentList = mutableListOf<SearchBean.UserSearchBean>()
    private lateinit var mRecentAdapter: TagAdapter
    private val mList = mutableListOf<SearchBean.TotalSearchBean>()
    private lateinit var mHotAdapter: TagAdapter
    private var mContent: String? = null

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg?.what == 0) {
                KeyboardUtils.hideKeyboard(et_content)

                startActivity<SearchResultActivity>("content" to mContent)
                finish()
            }
        }
    }

    override fun getContentView() = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ImmersionBar.with(this).titleBar(title_bar).statusBarDarkFont(true).init()

        mRecentAdapter = object : TagAdapter(mRecentList) {
            override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.dp_32)).apply {
                    leftMargin = resources.getDimensionPixelSize(R.dimen.dp_15)
                    bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_15)
                }
                layoutParams = params
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.dp_15), 0, resources.getDimensionPixelSize(R.dimen.dp_15), 0)
                text = (data as SearchBean.UserSearchBean).search_param
                setTextColor(ContextCompat.getColor(context, R.color.text_66))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_13))
                background = DrawableCreator.Builder()
                        .setSolidColor(0xFFF0F0F0.toInt())
                        .setCornersRadius(resources.getDimension(R.dimen.dp_3))
                        .build()
            }
        }
        flex_box_recent.setAdapter(mRecentAdapter)
        flex_box_recent.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                mContent = mRecentList[position].search_param
                et_content.setText(mContent)

                mHandler.sendEmptyMessage(0)
            }
        })

        mHotAdapter = object : TagAdapter(mList) {
            override fun getView(parent: FlowLayout, position: Int, data: Any) = TextView(applicationContext).apply {
                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.dp_32)).apply {
                    leftMargin = resources.getDimensionPixelSize(R.dimen.dp_15)
                    bottomMargin = resources.getDimensionPixelSize(R.dimen.dp_15)
                }
                layoutParams = params
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.dp_15), 0, resources.getDimensionPixelSize(R.dimen.dp_15), 0)
                text = (data as SearchBean.TotalSearchBean).search_param
                setTextColor(ContextCompat.getColor(context, R.color.text_66))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_13))
                background = DrawableCreator.Builder()
                        .setSolidColor(0xFFF0F0F0.toInt())
                        .setCornersRadius(resources.getDimension(R.dimen.dp_3))
                        .build()
            }
        }
        flex_box_hot.setAdapter(mHotAdapter)
        flex_box_hot.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View, position: Int, parent: FlowLayout) {
                mContent = mList[position].search_param
                et_content.setText(mContent)

                mHandler.sendEmptyMessage(0)
            }
        })

        et_content.addTextChangedListener(mTextWatcher)
        et_content.setOnEditorActionListener(mOnEditorActionListener)
        setOnClickListener(iv_back, tv_cancel, iv_delete)
    }

    override fun initData() {
        mPresenter?.getGoodsSearchList()
    }

    override fun createPresenter() = SearchPresenter()

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.tv_cancel -> finish()
            R.id.iv_delete -> {
                gone(fl_container, flex_box_recent)
            }
        }
    }

    private val mTextWatcher = object : EditWatcher() {
        override fun afterTextChanged(editable: Editable?) {
            mContent = editable.toString().trim { it <= ' ' }

            if (!TextUtils.isEmpty(mContent)) {
                mHandler.removeCallbacksAndMessages(null)
                mHandler.sendEmptyMessageDelayed(0, 3 * 1000)
            } else {
                mHandler.removeCallbacksAndMessages(null)

                visible(fl_container, flex_box_recent, tv_hot_search, flex_box_hot)
            }
        }
    }

    private val mOnEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(mContent)) {
                mHandler.removeCallbacksAndMessages(null)

                visible(fl_container, flex_box_recent, tv_hot_search, flex_box_hot)
            } else {
                mHandler.removeCallbacksAndMessages(null)
                mHandler.sendEmptyMessage(0)
            }
            return@OnEditorActionListener true
        }
        false
    }

    override fun getGoodsSearchListSuccess(bean: SearchBean) {
        with(bean) {
            if (user_serachs!!.isEmpty()) {
                gone(fl_container)
            } else {
                visible(fl_container)

                mRecentList.clear()
                mRecentList.addAll(user_serachs!!)
                mRecentAdapter.notifyDataChanged()
            }

            mList.clear()
            mList.addAll(total_serachs!!)
            mHotAdapter.notifyDataChanged()
        }
    }

}
