@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.base.activity

import android.app.ProgressDialog
import android.os.Bundle
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.utils.ToastUtils

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity<in V : IBaseView, P : IBasePresenter<V>> : BaseActivity(), IBaseView {

    protected var mPresenter: P? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun initView(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        if (null != mPresenter) {
            mPresenter!!.detachView()
            mPresenter = null
        }
        super.onDestroy()
    }

    override fun showLoading() {

    }

    override fun showNormal() {

    }

    override fun showEmpty() {

    }

    override fun showError() {

    }

    override fun showToast(msg: String?) {
        ToastUtils.showToast(msg ?: "未知错误")
    }

    override fun showDialog(msg: String, isCancel: Boolean) {
        if (null == mProgressDialog) {
            mProgressDialog = ProgressDialog(this, ProgressDialog.STYLE_SPINNER).apply {
                setMessage(msg)
                setCancelable(isCancel)
                setCanceledOnTouchOutside(isCancel)
                show()
            }
        }
    }

    override fun hideDialog() {
        if (null != mProgressDialog) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

    abstract fun createPresenter(): P?

}
