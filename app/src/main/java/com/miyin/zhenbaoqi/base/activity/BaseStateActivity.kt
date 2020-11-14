package com.miyin.zhenbaoqi.base.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.mvp.IBasePresenter
import com.miyin.zhenbaoqi.base.mvp.IBaseView

abstract class BaseStateActivity<in V : IBaseView, P : IBasePresenter<V>> : BaseMvpActivity<V, P>() {

    protected open var mErrorView: View? = null
    protected open var mLoadingView: View? = null
    protected open var mEmptyView: View? = null
    protected open var mNormalView: ViewGroup? = null
    protected open var currentState = NORMAL_STATE

    protected open fun getLoadingLayout() = R.layout.view_loading

    protected open fun getEmptyLayout() = R.layout.view_empty

    protected open fun getErrorLayout() = R.layout.view_error

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mNormalView = findViewById(R.id.normal_view)
        if (mNormalView == null) {
            throw IllegalStateException("The subclass of RootActivity must contain a View named 'mNormalView'.")
        }
        if (mNormalView!!.parent !is ViewGroup) {
            throw IllegalStateException("mNormalView's ParentView should be a ViewGroup.")
        }
        val parent = mNormalView!!.parent as ViewGroup
        View.inflate(this, getLoadingLayout(), parent)
        View.inflate(this, getErrorLayout(), parent)
        View.inflate(this, getEmptyLayout(), parent)

        mLoadingView = parent.findViewById(R.id.loading_group)
        mErrorView = parent.findViewById(R.id.error_group)
        mEmptyView = parent.findViewById(R.id.empty_group)

        parent.findViewById<View>(R.id.fl_empty_group).setOnClickListener { reload() }
        parent.findViewById<View>(R.id.fl_error_group).setOnClickListener { reload() }

        gone(mErrorView!!)
        gone(mEmptyView!!)
        gone(mLoadingView!!)
        visible(mNormalView!!)
    }

    override fun showLoading() {
        if (currentState == LOADING_STATE) {
            return
        }
        hideCurrentView()
        currentState = LOADING_STATE
        visible(mLoadingView!!)
    }

    override fun showError() {
        if (currentState == ERROR_STATE) {
            return
        }
        hideCurrentView()
        currentState = ERROR_STATE
        visible(mErrorView!!)
    }

    override fun showEmpty() {
        if (currentState == EMPTY_STATE) {
            return
        }
        hideCurrentView()
        currentState = EMPTY_STATE
        visible(mEmptyView!!)
    }

    override fun showNormal() {
        if (currentState == NORMAL_STATE) {
            return
        }
        hideCurrentView()
        currentState = NORMAL_STATE
        visible(mNormalView!!)
    }

    private fun hideCurrentView() {
        when (currentState) {
            NORMAL_STATE -> {
                if (mNormalView == null) {
                    return
                }
                gone(mNormalView!!)
            }
            LOADING_STATE -> gone(mLoadingView!!)
            ERROR_STATE -> gone(mErrorView!!)
            EMPTY_STATE -> gone(mEmptyView!!)
            else -> {
            }
        }
    }

    protected open fun reload() {

    }

    companion object {
        private const val NORMAL_STATE = 0
        private const val LOADING_STATE = 1
        private const val ERROR_STATE = 2
        private const val EMPTY_STATE = 3
    }

}