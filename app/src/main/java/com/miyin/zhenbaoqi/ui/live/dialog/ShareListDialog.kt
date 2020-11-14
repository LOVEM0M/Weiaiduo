package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.ui.live.adapter.LeaderBoardAdapter
import com.miyin.zhenbaoqi.ui.live.fragment.LiveHotFragment
import com.miyin.zhenbaoqi.ui.live.fragment.LiveShareFragment
import kotlinx.android.synthetic.main.dialog_share_list.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ShareListDialog : BaseDialogFragment() {
    lateinit var fragment: LiveShareFragment

    companion object {
        fun newInstance() = ShareListDialog().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
        }
        return R.layout.dialog_share_list
    }

    override fun initView(view: View) {
        fragment = fragmentManager?.findFragmentByTag("live_share_fragment") as LiveShareFragment
        iv_close.setOnClickListener { dismiss() }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

    override fun onDestroyView() {
        if (fragment != null) {
            fragmentManager?.beginTransaction()?.remove(fragment)?.commit()
        }
        super.onDestroyView()
    }

}
