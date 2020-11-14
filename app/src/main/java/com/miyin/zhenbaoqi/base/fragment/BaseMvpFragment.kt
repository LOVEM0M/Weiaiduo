@file:Suppress("DEPRECATION")

package com.miyin.zhenbaoqi.base.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView
import com.miyin.zhenbaoqi.utils.ToastUtils

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpFragment<in V : IBaseView, P : IBasePresenter<V>> : BaseFragment(), IBaseView {

    protected var mPresenter: P? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroyView() {
        if (null != mPresenter) {
            mPresenter!!.detachView()
            mPresenter = null
        }
        super.onDestroyView()
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
            mProgressDialog = ProgressDialog(activity, ProgressDialog.STYLE_SPINNER)
        }
        mProgressDialog?.run {
            setMessage(msg)
            setCancelable(isCancel)
            show()
        }
    }

    override fun hideDialog() {
        if (null != mProgressDialog && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }

    abstract fun createPresenter(): P?

}
