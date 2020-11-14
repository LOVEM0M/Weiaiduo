package com.miyin.zhenbaoqi.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.ui.home.adapter.SearchVideoAdapter

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.dialog_delay_bid.*

class SearchVideoFragment : BaseFragment() {

    private var mContent: String? = null
    private var mList = mutableListOf<String>()
    private lateinit var mAdapter: SearchVideoAdapter

    companion object {
        fun newInstance(content: String) = SearchVideoFragment().apply {
            arguments = Bundle().apply {
                putString("content", content)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mContent = getString("content")
        }
        return R.layout.fragment_search_video
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        recycler_view.run {
            mAdapter = SearchVideoAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun initData() {

    }

    fun researchGoods(content: String) {
        mContent = content
        initData()
    }

}
