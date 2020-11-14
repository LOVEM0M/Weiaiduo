package com.miyin.zhenbaoqi.ui.shop.activity.sub_account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.SubAccountInfoBean
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.SubAccountSettingContract
import com.miyin.zhenbaoqi.ui.shop.presenter.SubAccountSettingPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_sub_account_setting.*

class SubAccountSettingActivity : BaseMvpActivity<SubAccountSettingContract.IView, SubAccountSettingContract.IPresenter>(), SubAccountSettingContract.IView {

    private var mBean: SubAccountInfoBean.DataBean? = null
    private var mState = 0
    private var mAvatar: String? = null
    private var mName: String? = null
    private var mAccount: String? = null
    private var mPassword: String? = null

    override fun getContentView(): Int {
        with(intent) {
            mState = getIntExtra("state", 0)
            mBean = getSerializableExtra("bean") as SubAccountInfoBean.DataBean?
        }
        return R.layout.activity_sub_account_setting
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("子账号设置")
        immersionBar { statusBarDarkFont(true) }

        tv_identity.text = when (mState) {
            0 -> "聊天"
            1 -> "发货"
            2 -> "商品"
            else -> "聊天"
        }
        mBean?.run {
            mState = type
            mAccount = account_name
            mPassword = passward

            et_account.setText(mAccount)
            et_password.setText(mPassword)
            setButtonEnable()
        }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
            }
        })
        et_account.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mAccount = editable.toString().trim { it <= ' ' }
                setButtonEnable()
            }
        })
        et_password.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPassword = editable.toString().trim { it <= ' ' }
                setButtonEnable()
            }
        })

        setOnClickListener(fl_container, btn_commit)
    }

    override fun initData() {

    }

    override fun createPresenter() = SubAccountSettingPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && requestCode == Activity.RESULT_OK) {
            if (null != data) {
                PictureSelector.obtainMultipleResult(data).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath

                        showDialog("图片正在上传...", false)
                        uploadImg(path)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_container -> {
                SelectPhotoDialog(this).builder().show()
            }
            R.id.btn_commit -> {
                if (null == mBean) {
                    mPresenter?.addSubAccount(mState, mAvatar, mName, mAccount, mPassword)
                } else {
                    mPresenter?.updateSubAccount(mBean?.id!!, mAvatar, mName, mAccount, mPassword)
                }
            }
        }
    }

    private fun uploadImg(path: String) {

    }

    private fun setButtonEnable() {
        btn_commit.isEnabled = (!mAccount.isNullOrEmpty() && !mPassword.isNullOrEmpty())
    }

    override fun onAddSubAccountSuccess() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onUpdateSubAccountSuccess() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
