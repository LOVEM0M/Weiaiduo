package com.miyin.zhenbaoqi.base.mvp

interface IBaseView {

    fun showLoading()

    fun showNormal()

    fun showEmpty()

    fun showError()

    fun showDialog(msg: String = "正在加载...", isCancel: Boolean = true)

    fun hideDialog()

    fun showToast(msg: String?)

}
