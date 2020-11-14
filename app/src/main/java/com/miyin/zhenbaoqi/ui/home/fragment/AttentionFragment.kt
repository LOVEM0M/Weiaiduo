package com.miyin.zhenbaoqi.ui.home.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.HomeMerchantBean
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.home.adapter.AttentionAdapter
import com.miyin.zhenbaoqi.ui.home.contract.AttentionContract
import com.miyin.zhenbaoqi.ui.home.presenter.AttentionPresenter
import com.miyin.zhenbaoqi.ui.mine.activity.CollectActivity
import com.miyin.zhenbaoqi.ui.sort.activity.MerchantMessageActivity
import kotlinx.android.synthetic.main.fragment_attention.*

class AttentionFragment : BaseListFragment<AttentionContract.IView, AttentionContract.IPresenter>(), AttentionContract.IView {

    private var mList = mutableListOf<HomeMerchantBean.DataBean>()
    private lateinit var mAdapter: AttentionAdapter

    companion object {
        fun newInstance() = AttentionFragment()
    }

    override fun getContentView() = R.layout.fragment_attention

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mAdapter = AttentionAdapter(mList)
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = if (position == 0) 3 else 1
                }
            }
            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                if (view.id == R.id.tv_attention) {
                    mPresenter?.updateMerchantState(bean.is_focus, bean.merchants_id, position)
                } else if (view.id == R.id.tv_see) {
                    startActivity<MerchantMessageActivity>("merchant_id" to bean.merchants_id)
                }
            }

            val headerView = FrameLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, context.getDimensionPixelSize(R.dimen.dp_50))
                setOnClickListener { startActivity<CollectActivity>() }

                val leftView = TextView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                        leftMargin = context.getDimensionPixelSize(R.dimen.dp_9)
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    text = "优选店铺"
                    setTextColor(ContextCompat.getColor(context, R.color.text_33))
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_14))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                addView(leftView)

                val rightView = TextView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                        rightMargin = context.getDimensionPixelSize(R.dimen.dp_9)
                        gravity = Gravity.CENTER_VERTICAL or Gravity.END
                    }
                    text = "查看全部"
                    setTextColor(ContextCompat.getColor(context, R.color.text_99))
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_12))

                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow_preferred, 0)
                    compoundDrawablePadding = context.getDimensionPixelSize(R.dimen.dp_7)
                }
                addView(rightView)
            }
            mAdapter.addHeaderView(headerView)
        }

        refresh(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getMerchantList()
    }

    override fun createPresenter() = AttentionPresenter()

    override fun reload() {
        showLoading()
        initData()
    }

    override fun getMerchantListSuccess(bean: HomeMerchantBean) {
        bean.data?.let {
            if (it.isEmpty()) {
                showEmpty()
            } else {
                mList = it.toMutableList()
                mAdapter.setNewData(mList)
            }
        }
    }

    override fun updateMerchantStateSuccess(state: Int, position: Int) {
        val bean = mList[position]
        bean.is_focus = state
        mAdapter.notifyItemChanged(position)
    }

}
