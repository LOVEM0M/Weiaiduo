package com.miyin.zhenbaoqi.ui.mine.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.RealNameBean
import com.miyin.zhenbaoqi.ui.mine.contract.VerifiedContract
import com.miyin.zhenbaoqi.ui.mine.presenter.VerifiedPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_verified.*

class VerifiedActivity : BaseMvpActivity<VerifiedContract.IView, VerifiedContract.IPresenter>(), VerifiedContract.IView {

    private var mName: String? = null
    private var mIdCard: String? = null

    override fun getContentView() = R.layout.activity_verified

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("实名认证")
        immersionBar { statusBarDarkFont(true) }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
                isBtnEnable()
            }
        })
        et_id_card.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mIdCard = editable.toString().trim { it <= ' ' }
                isBtnEnable()
            }
        })

        btn_commit.setOnClickListener {
            mPresenter?.realName(et_id_card.textWithoutSpace, mName)
        }
    }

    override fun onLeftClick() {
        setResult(Activity.RESULT_OK)
        super.onLeftClick()
    }

    override fun initData() {
        mPresenter?.showRealName()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
    }

    override fun createPresenter() = VerifiedPresenter()

    private fun isBtnEnable() {
        btn_commit.isEnabled = !(TextUtils.isEmpty(mName) or TextUtils.isEmpty(mIdCard))
    }

    override fun showRealNameSuccess(bean: RealNameBean) {
        with(bean) {
            if (bean.is_real == 0) {
                et_name.setText(real_name)
                et_id_card.setText(id_no)

                btn_commit.isEnabled = false
                et_name.isEnabled = false
                et_id_card.isEnabled = false
            }
        }
    }

    override fun realNameSuccess() {
        val intent = Intent()
        intent.putExtra("username", mName)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
