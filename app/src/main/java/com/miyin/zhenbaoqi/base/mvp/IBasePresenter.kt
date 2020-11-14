package com.miyin.zhenbaoqi.base.mvp

interface IBasePresenter<in V : IBaseView> {

    fun attachView(view: V)

    fun detachView()

}
