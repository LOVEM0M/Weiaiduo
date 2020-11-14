package com.miyin.zhenbaoqi.base.fragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.constant.Constant
import org.greenrobot.eventbus.EventBus

import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseFragment : Fragment(), View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private var mIsCreateView = false
    private var mIsLoadData = false
    protected lateinit var mImmersionBar: ImmersionBar
    private var mOnPermissionCallback: OnPermissionCallback? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isLoadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this)
            mImmersionBar.init()
        }
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initView(view, savedInstanceState)
        mIsCreateView = true
        isLoadData()
    }

    override fun onDestroyView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        mIsCreateView = false
        mIsLoadData = false
        super.onDestroyView()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && useImmersionBar()) {
            mImmersionBar.init()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        mOnPermissionCallback?.onGranted()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("如果没有授权，应用将无法正常工作。我们将引导您到设置页面修改应用权限。")
                    .setPositiveButton(getString(R.string.confirm))
                    .setNegativeButton(getString(R.string.cancel))
                    .build()
                    .show()
        } else {
            mOnPermissionCallback?.onDenied()
        }
    }

    protected fun requestPermission(rationale: String, callback: OnPermissionCallback, vararg permission: String) {
        if (!EasyPermissions.hasPermissions(context!!, *permission)) {
            EasyPermissions.requestPermissions(this, rationale, Constant.PERMISSION_REQUEST_CODE, *permission)
        } else {
            callback.onGranted()
        }
    }

    protected open fun useImmersionBar() = false

    protected open fun useEventBus() = false

    private fun isLoadData() {
        if (!mIsCreateView) {
            return
        }
        if (userVisibleHint) {
            if (!mIsLoadData) {
                initData()
                mIsLoadData = true
            }
        }
    }

    override fun onClick(v: View?) {

    }

    protected fun setOnClickListener(vararg views: View?) {
        setOnClickListener(this, *views)
    }

    protected fun setOnClickListener(listener: View.OnClickListener, vararg views: View?) {
        views.filterNotNull().forEach {
            it.setOnClickListener(listener)
        }
    }

    protected fun visible(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.VISIBLE) {
                it.visibility = View.VISIBLE
            }
        }
    }

    protected fun invisible(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.INVISIBLE) {
                it.visibility = View.INVISIBLE
            }
        }
    }

    protected fun gone(vararg views: View?) {
        views.filterNotNull().forEach {
            if (it.visibility != View.GONE) {
                it.visibility = View.GONE
            }
        }
    }

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    protected abstract fun initData()

}
