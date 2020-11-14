package com.miyin.zhenbaoqi.ui.live.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.live.fragment.LiveGoodsFragment
import com.miyin.zhenbaoqi.ui.shop.activity.OperateGoodsActivity
import kotlinx.android.synthetic.main.dialog_goods.iv_close
import kotlinx.android.synthetic.main.dialog_goods.tab_layout
import kotlinx.android.synthetic.main.dialog_goods.view_pager
import kotlinx.android.synthetic.main.dialog_goods_live.*

class GoodsLiveDialog : BaseDialogFragment() {

    private var mRoomId = 0

    companion object {
        fun newInstance(room_id: Int) = GoodsLiveDialog().apply {
            arguments = Bundle().apply {
                putInt("room_id", room_id)
            }
        }
    }

    override fun getContentView(): Int {
        arguments?.run {
            mRoomId = getInt("room_id")
        }
        return R.layout.dialog_goods_live
    }

    override fun initView(view: View) {
        val stateList = listOf(0, 2)
        val titleList = listOf("全部宝贝", "编辑中的宝贝")
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, it ->
            fragmentList.add(LiveGoodsFragment.newInstance(it, stateList[index], mRoomId))
        }

        val adapter = ViewPagerAdapter(childFragmentManager, titleList, fragmentList)
        view_pager.adapter = adapter
        tab_layout.setViewPager(view_pager)

        iv_close.setOnClickListener { dismiss() }

        btn_add.setOnClickListener {
            startActivity<OperateGoodsActivity>("title" to "创建商品", "room_id" to mRoomId)
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setGravity() = Gravity.BOTTOM

}
