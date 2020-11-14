package com.miyin.zhenbaoqi.ui.mine.activity.bank_card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.BankCardBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.ui.mine.activity.VerifiedActivity
import com.miyin.zhenbaoqi.ui.mine.adapter.BankCardAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.BankCardContract
import com.miyin.zhenbaoqi.ui.mine.dialog.DeleteTipDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.BankCardPresenter
import kotlinx.android.synthetic.main.activity_bank_card.*

class BankCardActivity : BaseMvpActivity<BankCardContract.IView, BankCardContract.IPresenter>(), BankCardContract.IView, OnDialogCallback {

    private var mList = mutableListOf<BankCardBean.ListBean>()
    private lateinit var mAdapter: BankCardAdapter
    private var mPosition = 0

    override fun getContentView() = R.layout.activity_bank_card

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("银行卡")
        immersionBar { statusBarDarkFont(true) }

        recycler_view.run {
            mAdapter = BankCardAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

            mAdapter.setOnItemChildClickListener { _, view, position ->
                val bean = mList[position]
                if (view.id == R.id.fl_container) {
                    val intent = Intent()
                    intent.putExtra("bean", bean)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else if (view.id == R.id.fl_right_menu) {
                    mPosition = position

                    val dialog = DeleteTipDialog.newInstance()
                    dialog.show(supportFragmentManager, "deleteBankCard")
                }
            }
        }

        btn_commit.setOnClickListener {
            startActivityForResult<BindBandCardActivity>(Constant.INTENT_REQUEST_CODE)
        }
    }

    override fun initData() {
        mPresenter?.getBankCardList()
    }

    override fun createPresenter() = BankCardPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            initData()
        }
    }

    override fun getBankCardListSuccess(list: List<BankCardBean.ListBean>) {
        visible(btn_commit)

        mList = list.toMutableList()
        mAdapter.setNewData(list)
    }

    override fun deleteBankCardSuccess(position: Int) {
        mList.removeAt(position)
        mAdapter.setNewData(mList)

        if (mList.isEmpty()) {
            mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
        }
    }

    override fun onFailure(msg: String, flag: Int) {
        when (flag) {
            1 -> {
                visible(btn_commit)
                mAdapter.setEmptyView(R.layout.view_empty, recycler_view)
            }
            2 -> {
                invisible(btn_commit)
                mAdapter.setEmptyView(R.layout.view_error, recycler_view)
            }
            5 -> {
                showToast(msg)
                startActivityForResult<VerifiedActivity>(Constant.INTENT_REQUEST_CODE)
            }
            else -> {
                showToast(msg)

                visible(btn_commit)
                val errorView = LayoutInflater.from(this).inflate(R.layout.view_empty, recycler_view, false).apply {
                    setOnClickListener {
                        initData()
                    }
                }
                mAdapter.emptyView = errorView
            }
        }
    }

    override fun onDialog(obj: Any, flag: Int) {
        when (obj) {
            is String -> {
                if ("delete" == obj) {
                    mPresenter?.deleteBankCard(mList[mPosition].user_bank_id, mPosition)
                }
            }
        }
    }

}
