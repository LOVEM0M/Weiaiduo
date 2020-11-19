package com.miyin.zhenbaoqi.ui.mine.activity.address

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.AddressBean
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.ui.mine.contract.AddAddressContract
import com.miyin.zhenbaoqi.ui.mine.dialog.CityDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.AddAddressPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.widget.SwitchButton
import kotlinx.android.synthetic.main.activity_add_address.*

@SuppressLint("SetTextI18n")
class AddAddressActivity : BaseMvpActivity<AddAddressContract.IView, AddAddressContract.IPresenter>(), AddAddressContract.IView, CityDialog.OnCitySelectCallback {

    private var mName: String? = null
    private var mPhone: String? = null
    private var mAddress: String? = null
    private var mIsDefault = 1
    private var mProvinceId = 0
    private var mCityId = 0
    private var mCountyId = 0
    private var mAddressId = 0
    private var mUserAddressBean: AddressBean.DataBeanX.UserAddressBean? = null

    private var mCityDialog: CityDialog? = null

    override fun getContentView(): Int {
        mUserAddressBean = intent.getSerializableExtra("bean") as AddressBean.DataBeanX.UserAddressBean?
        return R.layout.activity_add_address
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("添加新地址")
        immersionBar { statusBarDarkFont(true) }

        mUserAddressBean?.run {
            mName = consignee
            mPhone = phoneNo
            mProvinceId = provinceId
            mCityId = cityId
            mCountyId = countyId
            mAddress = address
            mIsDefault = isDefault
            mAddressId = adsId

            et_name.setText(mName)
            et_phone.setText(mPhone)
            tv_area.text = pccName
            et_address.setText(address)
            switch_button.isChecked = mIsDefault == 0
            btn_commit.isEnabled = true
        }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
                setBtnEnabled()
            }
        })
        et_phone.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPhone = editable.toString().trim { it <= ' ' }
                setBtnEnabled()
            }
        })
        et_address.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mAddress = editable.toString().trim { it <= ' ' }
                setBtnEnabled()
            }
        })
        switch_button.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                mIsDefault = if (isChecked) 0 else 1
            }
        })

        setOnClickListener(fl_area, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = AddAddressPresenter()

    override fun onDestroy() {
        if (null != mCityDialog) {
            mCityDialog = null
        }
        super.onDestroy()
    }

    private fun setBtnEnabled() {
        btn_commit.isEnabled = mProvinceId != 0 && mCityId != 0 && mCountyId != 0 &&
                !mName.isNullOrEmpty() && !mPhone.isNullOrEmpty() && !mAddress.isNullOrEmpty()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_area -> {
                if (null == mCityDialog) {
                    mPresenter?.getProvinceList(0)
                } else {
                    mCityDialog?.show(supportFragmentManager, "city")
                }
            }
            R.id.btn_commit -> {
                if (mAddressId == 0) {
                    mPresenter?.addAddress(mAddress, mCityId, mName, mCountyId, mIsDefault, mPhone, mProvinceId)
                } else {
                    mPresenter?.updateAddress(mAddress, mAddressId, mCityId, mName, mCountyId, mIsDefault, mPhone, mProvinceId)
                }
            }
        }
    }

    override fun addAddressSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun updateAddressSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun getProvinceListSuccess(list: List<CityBean.DataBean>) {
        mCityDialog = CityDialog.newInstance(list)
        mCityDialog?.show(supportFragmentManager, "city")
    }

    override fun getAreaListSuccess(list: List<CityBean.DataBean>, position: Int, state: Int) {
        mCityDialog?.setAreaList(list, state, position)
    }

    override fun onCitySelect(position: Int, state: Int, parentId: Int) {
        mPresenter?.getAreaList(position, state, parentId)
    }

    override fun onSelectCity(province: CityBean.DataBean, city: CityBean.DataBean, county: CityBean.DataBean) {
        mProvinceId = province.dictId
        mCityId = city.dictId
        mCountyId = county.dictId

        tv_area.text = "${province.codeName}-${city.codeName}-${county.codeName}"
        setBtnEnabled()
    }

}
