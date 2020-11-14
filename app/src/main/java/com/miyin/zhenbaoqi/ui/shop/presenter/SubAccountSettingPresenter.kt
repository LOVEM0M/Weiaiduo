package com.miyin.zhenbaoqi.ui.shop.presenter

import com.miyin.zhenbaoqi.base.mvp.BasePresenter
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.shop.contract.SubAccountSettingContract

class SubAccountSettingPresenter : BasePresenter<SubAccountSettingContract.IView>(), SubAccountSettingContract.IPresenter {

    override fun addSubAccount(type: Int, avatar: String?, name: String?, account: String?, password: String?) {
//        if (avatar.isNullOrEmpty()) {
//            getView()?.showToast("请上传头像")
//            return
//        }
//        if (name.isNullOrEmpty()) {
//            getView()?.showToast("请填写昵称")
//            return
//        }
        if (account.isNullOrEmpty()) {
            getView()?.showToast("请填写账号")
            return
        }
        if (account.length != 11) {
            getView()?.showToast("请输入正确的手机号")
            return
        }
        if (password.isNullOrEmpty()) {
            getView()?.showToast("请填写密码")
            return
        }

        request(RetrofitUtils.mApiService.merchantsSubAdd(type, account, password), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.onAddSubAccountSuccess()
            }
        })
    }

    override fun updateSubAccount(id: Int, avatar: String?, name: String?, account: String?, password: String?) {
//        if (avatar.isNullOrEmpty()) {
//            getView()?.showToast("请上传头像")
//            return
//        }
//        if (name.isNullOrEmpty()) {
//            getView()?.showToast("请填写昵称")
//            return
//        }
        if (account.isNullOrEmpty()) {
            getView()?.showToast("请填写账号")
            return
        }
        if (account.length != 11) {
            getView()?.showToast("请输入正确的手机号")
            return
        }
        if (password.isNullOrEmpty()) {
            getView()?.showToast("请填写密码")
            return
        }

        request(RetrofitUtils.mApiService.merchantsSubUpdate(id, account, password), object : BaseSingleObserver<ResponseBean>() {
            override fun doOnSuccess(data: ResponseBean) {
                getView()?.onUpdateSubAccountSuccess()
            }
        })
    }

}
