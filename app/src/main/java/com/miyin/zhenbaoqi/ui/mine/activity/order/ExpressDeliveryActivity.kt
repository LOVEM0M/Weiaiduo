package com.miyin.zhenbaoqi.ui.mine.activity.order

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.copyMsg
import com.miyin.zhenbaoqi.ui.common.SingleDataDialog
import com.miyin.zhenbaoqi.ui.mine.contract.ExpressDeliveryContract
import com.miyin.zhenbaoqi.ui.mine.presenter.ExpressDeliveryPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_express_delivery.*


class ExpressDeliveryActivity : BaseMvpActivity<ExpressDeliveryContract.IView, ExpressDeliveryContract.IPresenter>(), ExpressDeliveryContract.IView, OnDialogCallback {

    private var mOrderNumber: String? = null
    private var mUserInfo: String? = null
    private var mAddress: String? = null
    private var mSource: String? = null
    private var mCourierNo: String? = null
    private var mCourierName: String? = null
    private var mCodeValue: String? = null
    private var mDictId = 0

    override fun getContentView(): Int {
        with(intent) {
            mOrderNumber = getStringExtra("order_number")
            mUserInfo = getStringExtra("user_info")
            mAddress = getStringExtra("address")
            mSource = getStringExtra("source")
        }
        return R.layout.activity_express_delivery
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("快递发货")
        immersionBar { statusBarDarkFont(true) }

        tv_user_info.text = SpanUtils()
                .append("收  货  人：").setForegroundColor(ContextCompat.getColor(this, R.color.text_99))
                .append(mUserInfo ?: "")
                .create()
        tv_address.text = mAddress

        et_courier_no.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mCourierNo = editable.toString().trim { it <= ' ' }
            }
        })
        et_courier_no.setOnLongClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (clipboardManager.hasPrimaryClip()) {
                et_courier_no.setText(clipboardManager.text)
                showToast("粘贴成功")
            }
            true
        }
        setOnClickListener(tv_copy, fl_company, iv_scanner, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = ExpressDeliveryPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_copy -> {
                copyMsg(mUserInfo + mAddress)
                showToast("复制成功")
            }
            R.id.fl_company -> mPresenter?.getCourierCompanyList("courier")
            R.id.iv_scanner -> {
            }
            R.id.btn_commit -> {
                when (mSource) {
                    "merchantUpdate" -> {
                        mPresenter?.updateCourierNo(mCodeValue, mCourierName
                                ?: "", mCourierNo, mOrderNumber!!)
                    }
                    "merchant" -> {
                        mPresenter?.merchantShip(mCodeValue, mCourierName
                                ?: "", mCourierNo, mOrderNumber!!)
                    }
                    else -> {
                        mPresenter?.confirmShip(mCourierNo, mDictId, mOrderNumber!!)
                    }
                }
            }
        }
    }

    override fun getCourierCompanyListSuccess(list: List<CityBean.CityListBean>) {
        val dialog = SingleDataDialog.newInstance(list)
        dialog.show(supportFragmentManager, "courierCompany")
    }

    override fun confirmShipSuccess() {
        showToast("提交成功")
        finish()
    }

    override fun merchantShipSuccess() {
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is CityBean.CityListBean) {
            mDictId = obj.dict_id
            mCodeValue = obj.code_value
            mCourierName = obj.code_name
            tv_company.text = obj.code_name
        }
    }

}
