package com.miyin.zhenbaoqi.ui.shop.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.bean.MerchantHasAuthBean
import com.miyin.zhenbaoqi.bean.TotalAmountBean
import com.miyin.zhenbaoqi.constant.Constant
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ext.startActivityForResult
import com.miyin.zhenbaoqi.http.BaseSingleObserver
import com.miyin.zhenbaoqi.http.RetrofitUtils
import com.miyin.zhenbaoqi.ui.common.ViewPagerAdapter
import com.miyin.zhenbaoqi.ui.common.WebActivity
import com.miyin.zhenbaoqi.ui.shop.activity.release_goods.ReleaseGoodsActivity
import com.miyin.zhenbaoqi.ui.shop.activity.security_deposit.QualificationActivity
import com.miyin.zhenbaoqi.ui.shop.activity.security_deposit.SecurityDepositActivity
import com.miyin.zhenbaoqi.ui.shop.fragment.ManagerShopFragment
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_manager_shop.*

class ManagerShopActivity : BaseActivity() {

    private val mDisposable = CompositeDisposable()
    private var mMerchantHeadImg: String? = null
    private var mMerchantName: String? = null
    private var mMerchantDesc: String? = null

    override fun getContentView() = R.layout.activity_manager_shop

    override fun initView(savedInstanceState: Bundle?) {
        initTitleBar("店铺管理", rightTitle = "认证品质店铺")
        immersionBar { statusBarDarkFont(true) }
        findViewById<TextView>(R.id.tv_right_title).setTextColor(0xFFEA1A1A.toInt())

        mMerchantHeadImg = SPUtils.getString("merchant_head_img")
        mMerchantName = SPUtils.getString("merchant_name")
        mMerchantDesc = SPUtils.getString("merchant_desc")

        iv_cover.loadImg(mMerchantHeadImg)
        tv_shop_name.text = mMerchantName
        tv_desc.text = mMerchantDesc

        val titleList = listOf("已置顶", "已上架", "已下架")
        val stateList = listOf(2, 0, 1)
        val fragmentList = mutableListOf<Fragment>()
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(ManagerShopFragment.newInstance(stateList[index]))
        }
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, titleList, fragmentList)
        tab_layout.setViewPager(view_pager)
        view_pager.offscreenPageLimit = titleList.size

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offsetVertical ->
            if (Math.abs(offsetVertical) != 0) {
                if (Math.abs(offsetVertical) == appBarLayout.totalScrollRange) {
                    view_pager.setCanScroll(true)
                } else {
                    view_pager.setCanScroll(false)
                }
            } else if (offsetVertical == 0) {
                view_pager.setCanScroll(false)
            }
        })

        tv_shop_decoration.setOnClickListener {
            startActivityForResult<MerchantInfoActivity>(Constant.INTENT_REQUEST_CODE)
        }
        tv_shop_desc.setOnClickListener {
            WebActivity.openActivity(this@ManagerShopActivity, "店铺规范及管理说明", Constant.WARRANTY_DESC)
        }
        btn_commit.setOnClickListener {
            if (mMerchantHeadImg.isNullOrEmpty() or mMerchantName.isNullOrEmpty() or mMerchantDesc.isNullOrEmpty()) {
                ToastUtils.showToast("请完善店铺信息")
                startActivityForResult<MerchantInfoActivity>(Constant.INTENT_REQUEST_CODE)
            } else {
                startActivity<ReleaseGoodsActivity>()
                finish()
            }
        }
    }

    override fun initData() {

    }

    override fun onRightClick() {
        val disposable = RetrofitUtils.mApiService.walletMerchantTotalAmount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<TotalAmountBean>() {
                    override fun doOnSuccess(data: TotalAmountBean) {
                        data.data?.run {
                            if (quality_balance == 0) {
                                WebActivity.openActivity(applicationContext, "申请认证品质店铺", Constant.AUTH_STORE)
                            } else {
                                getMerchantAuthState()
                            }
                        }
                    }
                })
        mDisposable.add(disposable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mMerchantHeadImg = SPUtils.getString("merchant_head_img")
            mMerchantName = SPUtils.getString("merchant_name")
            mMerchantDesc = SPUtils.getString("merchant_desc")

            iv_cover.loadImg(mMerchantHeadImg)
            tv_shop_name.text = mMerchantName
            tv_desc.text = mMerchantDesc
        }
    }

    override fun onDestroy() {
        mDisposable.dispose()
        mDisposable.clear()
        super.onDestroy()
    }

    fun getMerchantAuthState() {
        val disposable = RetrofitUtils.mApiService.merchantIsAuth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : BaseSingleObserver<MerchantHasAuthBean>() {
                    override fun doOnSuccess(data: MerchantHasAuthBean) {
                        when (data.data?.status) {
                            0 -> {
                                startActivity<QualificationActivity>()
                            }
                            1 -> {
                                ToastUtils.showToast("待高级商家核实")
                            }
                            2 -> {
                                ToastUtils.showToast("待总部核实")
                            }
                            else -> startActivity<SecurityDepositActivity>("has_money" to true)
                        }
                    }
                })
        mDisposable.add(disposable)
    }

}
