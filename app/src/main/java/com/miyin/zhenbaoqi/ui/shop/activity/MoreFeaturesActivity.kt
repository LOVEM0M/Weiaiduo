package com.miyin.zhenbaoqi.ui.shop.activity

import android.os.Bundle
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.shop.activity.business.BusinessActivity
import com.miyin.zhenbaoqi.ui.shop.activity.lucky_bag.LuckyBagActivity
import com.miyin.zhenbaoqi.ui.shop.activity.material.MaterialActivity
import com.miyin.zhenbaoqi.ui.shop.activity.release_goods.ReleaseGoodsActivity
import com.miyin.zhenbaoqi.ui.shop.activity.sub_account.SubAccountManagerActivity
import com.miyin.zhenbaoqi.ui.shop.activity.video.ManagerVideoActivity
import com.miyin.zhenbaoqi.ui.shop.contract.MoreFeaturesContract
import com.miyin.zhenbaoqi.ui.shop.dialog.LiveManagerDialog
import com.miyin.zhenbaoqi.ui.shop.presenter.MoreFeaturesPresenter
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_more_features.*

class MoreFeaturesActivity : BaseMvpActivity<MoreFeaturesContract.IView, MoreFeaturesContract.IPresenter>(), MoreFeaturesContract.IView, OnDialogCallback {

    override fun getContentView() = R.layout.activity_more_features

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("更多功能")
        immersionBar { statusBarDarkFont(true) }

        setOnClickListener(tv_live, tv_release_goods, tv_bind_lucky_bag, tv_business_school, tv_material_manager,
                tv_video_manager, tv_shop_confirm, tv_black_list, tv_activity_sign_up, tv_sub_account)
    }

    override fun initData() {

    }

    override fun createPresenter() = MoreFeaturesPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_live -> {
                val dialog = LiveManagerDialog.newInstance()
                dialog.show(supportFragmentManager, "live")
            }
            R.id.tv_release_goods -> {
                val merchantHeadImg = SPUtils.getString("merchant_head_img")
                val merchantName = SPUtils.getString("merchant_name")
                val merchantDesc = SPUtils.getString("merchant_desc")

                if (merchantHeadImg.isEmpty() or merchantName.isEmpty() or merchantDesc.isEmpty()) {
                    showToast("请完善店铺信息")
                    startActivity<MerchantInfoActivity>()
                } else {
                    startActivity<ReleaseGoodsActivity>()
                }
            }
            R.id.tv_bind_lucky_bag -> startActivity<LuckyBagActivity>()
            R.id.tv_business_school -> startActivity<BusinessSchoolActivity>()
            R.id.tv_material_manager -> startActivity<MaterialActivity>()
            R.id.tv_video_manager -> startActivity<ManagerVideoActivity>()
            R.id.tv_shop_confirm -> startActivity<ShopVerifyActivity>()
            R.id.tv_black_list -> startActivity<BlackListActivity>()
            R.id.tv_activity_sign_up -> startActivity<BusinessActivity>()
            R.id.tv_sub_account -> startActivity<SubAccountManagerActivity>()
        }
    }


    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            when (obj) {
                "live" -> startActivity<PushLiveActivity>()
//                "goods" -> startActivity<ManagerGoodsActivity>()
            }
        }
    }

}
