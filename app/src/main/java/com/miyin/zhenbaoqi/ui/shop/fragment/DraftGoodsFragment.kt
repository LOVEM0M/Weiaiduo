package com.miyin.zhenbaoqi.ui.shop.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.fragment.BaseListFragment
import com.miyin.zhenbaoqi.bean.DraftAuctionGoodsBean
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.sql.AuctionGoodsDraftEntity
import com.miyin.zhenbaoqi.ui.shop.activity.release_goods.ReleaseGoodsActivity
import com.miyin.zhenbaoqi.ui.shop.adapter.DraftGoodsAdapter
import com.miyin.zhenbaoqi.ui.shop.contract.DraftGoodsContract
import com.miyin.zhenbaoqi.ui.shop.presenter.DraftGoodsPresenter
import com.miyin.zhenbaoqi.utils.JSONUtils
import kotlinx.android.synthetic.main.layout_refresh.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DraftGoodsFragment : BaseListFragment<DraftGoodsContract.IView, DraftGoodsContract.IPresenter>(), DraftGoodsContract.IView {

    private var mList = mutableListOf<AuctionGoodsDraftEntity>()
    private lateinit var mDraftGoodsAdapter: DraftGoodsAdapter
    private var mPosition = 0

    companion object {
        fun newInstance() = DraftGoodsFragment()
    }

    override fun useEventBus() = true

    override fun getContentView(): Int {
        return R.layout.fragment_draft_goods
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        showLoading()

        recycler_view.run {
            mDraftGoodsAdapter = DraftGoodsAdapter(mList)
            adapter = mDraftGoodsAdapter
            layoutManager = LinearLayoutManager(context)

            mDraftGoodsAdapter.setOnItemChildClickListener { _, view, position ->
                mPosition = position
                val entity = mList[position]
                if (view.id == R.id.tv_edit) {
                    val bean = JSONUtils.fromJSON<DraftAuctionGoodsBean>(entity.data)
                    startActivity<ReleaseGoodsActivity>("bean" to bean, "auction_goods" to true)
                } else if (view.id == R.id.tv_delete) {
                    AlertDialog.Builder(activity!!)
                            .setTitle("温馨提示")
                            .setMessage("您确定从草稿箱是删除此条记录吗？")
                            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                                mPresenter?.deleteDraftGoods(mList[position].id)
                                dialog.dismiss()
                            }
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show()
                }
            }
        }

        refreshAndLoadMore(smart_refresh_layout) { initData() }
    }

    override fun initData() {
        mPresenter?.getDraftGoodsList(mPage, mCount)
    }

    override fun createPresenter() = DraftGoodsPresenter()

    override fun reload() {
        showLoading()
        mPage = 1
        initData()
    }

    override fun getDraftGoodsListSuccess(list: List<AuctionGoodsDraftEntity>) {
        if (mPage == 1) {
            if (list.isEmpty()) {
                showEmpty()
            } else {
                showNormal()
                mList = list.toMutableList()
                mDraftGoodsAdapter.setNewData(mList)
            }
        } else {
            mDraftGoodsAdapter.addData(list)
        }
        if (mPage != 1 && list.isEmpty()) {
            smart_refresh_layout.setEnableLoadMore(false)
        }
    }

    override fun deleteDraftGoodsSuccess() {
        reload()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: String) {
        if (msg == "deleteDraft") {
            mPresenter?.deleteDraftGoods(mList[mPosition].id)
        }
    }

}
