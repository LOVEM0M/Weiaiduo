package com.miyin.zhenbaoqi.ui.live.dialog

import android.view.Gravity
import android.view.View

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.base.BaseDialogFragment.Companion.MATCH_PARENT
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.live.fragment.LiveHotFragment
import kotlinx.android.synthetic.main.dialog_live_hot.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LiveHotDialog : BaseDialogFragment() {

    lateinit var fragment: LiveHotFragment

    companion object {
        fun newInstance() = LiveHotDialog().apply {
        }
    }

    override fun getContentView(): Int {
        return R.layout.dialog_live_hot
    }

    override fun initView(view: View) {
        fragment = fragmentManager?.findFragmentByTag("live_hot_fragment") as LiveHotFragment
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
