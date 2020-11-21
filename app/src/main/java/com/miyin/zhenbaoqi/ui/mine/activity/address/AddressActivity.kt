package com.miyin.zhenbaoqi.ui.mine.activity.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseListActivity
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.adapter.AddressAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.AddressContact
import com.miyin.zhenbaoqi.ui.mine.dialog.DeleteTipDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.AddressPresenter
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.layout_refresh.*

class AddressActivity : BaseListActivity<AddressContact.IView, AddressContact.IPresenter>(), AddressContact.IView, OnDialogCallback {

    private var mIsClick = true
    private var mList = mutableListOf<AddressBean.DataBeanX.UserAddressBean>()
    private lateinit var mAdapter: AddressAdapter
    private var mPosition = 0

    override fun getContentView(): Int {
        mIsClick = intent.getBooleanExtra("click_enable", true)
        return R.layout.activity_address
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("管理收货地址")
        immersionBar { statusBarDarkFont(true) }

//        showLoading()
        recycler_view.run {
            mAdapter = AddressAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            mAdapter.setOnItemChildClickListener { _, view, position ->
                mPosition = position
                val bean = mList[position]
                when (view.id) {
                    R.id.cl_container -> {
                        if (!mIsClick) {
                            return@setOnItemChildClickListener
                        }

                        val intent = Intent()
                        intent.putExtra("bean", bean)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    R.id.tv_edit -> startActivityForResult<AddAddressActivity>(Constant.INTENT_REQUEST_CODE, "bean" to bean)
                    R.id.tv_delete -> {
                        val dialog = DeleteTipDialog.newInstance()
                        dialog.show(supportFragmentManager, "deleteAddress")
                    }
                    R.id.tv_is_default -> mPresenter?.setDefaultAddress(bean.address!!, bean.adsId, bean.cityId,
                            bean.consignee!!, bean.countyId, bean.isDefault, bean.phoneNo!!, bean.provinceId)
                }
            }
        }
        refreshAndLoadMore(smart_refresh_layout) { initData() }

        mEmptyView?.findViewById<TextView>(R.id.tv_empty)?.text = "暂无收货地址"

        btn_commit.setOnClickListener { startActivityForResult<AddAddressActivity>(Constant.INTENT_REQUEST_CODE) }
    }

    override fun initData() {
        mPresenter?.getAddressList(mPage, mCount)
    }

    override fun createPresenter() = AddressPresenter()

    override fun reload() {
        mPage = 1
        initData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    override fun getAddressListSuccess(bean: AddressBean) {
        visible(btn_commit)
        with(bean) {
                mList = data!!.userAddress!!.toMutableList()
                mAdapter.setNewData(mList)
            smart_refresh_layout.setEnableLoadMore(current_page != pages)
        }
    }

    override fun deleteAddressSuccess() {
        mList.removeAt(mPosition)
        mAdapter.notifyItemRemoved(mPosition)
        mAdapter.notifyItemRangeChanged(mPosition, mList.size - mPosition)
        if (mList.isEmpty()) {
            showEmpty()
            visible(btn_commit)
        }
    }

    override fun setDefaultAddressSuccess() {
        mList.filter { bean -> bean.isDefault == 0 }.forEach {
            it.isDefault = 1
            val index = mList.indexOf(it)
            mAdapter.notifyItemChanged(index)
        }
        mList[mPosition].isDefault = 0
        mAdapter.notifyItemChanged(mPosition)
    }

    override fun onFailure(msg: String, flag: Int) {
        if (flag == 0) {
            invisible(btn_commit)
        } else {
            visible(btn_commit)
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        when (obj) {
            is String -> {
                if (obj == "delete") {
                    val bean = mList[mPosition]
                    mPresenter?.deleteAddress(bean.adsId)
                }
            }
        }
    }

}
