package com.miyin.zhenbaoqi.ui.live.fragment

import android.os.Bundle
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseFragment

class BlankFragment : BaseFragment() {

    companion object {
        fun newInstance() = BlankFragment()
    }

    override fun getContentView() = R.layout.fragment_blank

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

}
