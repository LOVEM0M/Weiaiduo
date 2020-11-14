package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.live.fragment.LiveGoodsFragment
import kotlinx.android.synthetic.main.dialog_goods.*

class GoodsDialog : BaseDialogFragment() {
    var room_id = 0

    companion object {
        fun newInstance(room_id: Int) = GoodsDialog().apply {
            arguments = Bundle().apply {
                putInt("room_id", room_id)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            room_id = getInt("room_id")
        }
        return R.layout.dialog_goods
    }

    override fun initView(view: View) {
        val stateList = listOf(0, 2)
        val titleList = listOf("秒杀中", "拍卖中")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, it ->
            fragmentList.add(LiveGoodsFragment.newInstance(it, stateList[index],room_id))
        }

        val adapter = ViewPagerAdapter(childFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)

        iv_close.setOnClickListener { dismiss() }

    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
