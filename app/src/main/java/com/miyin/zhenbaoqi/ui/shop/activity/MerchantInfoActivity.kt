package com.miyin.zhenbaoqi.ui.shop.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.MerchantBean
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.mine.dialog.CityDialog
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.MerchantInfoContract
import com.miyin.zhenbaoqi.ui.shop.presenter.MerchantInfoPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.SPUtils
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMFriendshipManager
import com.tencent.imsdk.TIMUserProfile
import kotlinx.android.synthetic.main.activity_merchant_info.*
import kotlinx.android.synthetic.main.activity_merchant_info.btn_commit
import kotlinx.android.synthetic.main.activity_merchant_info.et_name
import kotlinx.android.synthetic.main.activity_merchant_info.et_phone

class MerchantInfoActivity : BaseMvpActivity<MerchantInfoContract.IView, MerchantInfoContract.IPresenter>(),
        MerchantInfoContract.IView, CityDialog.OnCitySelectCallback {

    private var mAvatar: String? = null
    private var mName: String? = null
    private var mIntroduce: String? = null
    private var mWXChat: String? = null
    private var mPhone: String? = null
    private var mAddress: String? = null
    private var mProvinceId = 0
    private var mCityId = 0
    private var mCountyId = 0
    private var mBackground: String? = null
    private var mIsUpdateName = 1
    private var mConsignee: String? = null
    private var mTag = 0
    private var mCityDialog: CityDialog? = null

    override fun getContentView() = R.layout.activity_merchant_info

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("店铺信息")
        immersionBar { statusBarDarkFont(true) }

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
            }
        })
        et_introduce.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mIntroduce = editable.toString().trim { it <= ' ' }
            }
        })
        et_wx.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mWXChat = editable.toString().trim { it <= ' ' }
            }
        })
        et_phone.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPhone = editable.toString().trim { it <= ' ' }
            }
        })
        et_consignee.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mConsignee = editable.toString().trim { it <= ' ' }
            }
        })
        et_address.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mAddress = editable.toString().trim { it <= ' ' }
            }
        })
        et_name.isEnabled = false

        setOnClickListener(fl_avatar, fl_address, tv_shop_background, btn_commit)
    }

    override fun initData() {
        mPresenter?.getMerchantInfo()
    }

    override fun createPresenter() = MerchantInfoPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath

                        if (mTag == 0) {
                            mPresenter?.uploadMerchantHead(path)
                        } else if (mTag == 1) {
                            mPresenter?.uploadMerchantBackground(path)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (null != mCityDialog) {
            mCityDialog = null
        }
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_avatar -> {
                if (mIsUpdateName == 1) {
                    showToast("需要改名卡才可以修改")
                    return
                }

                mTag = 0
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@MerchantInfoActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.fl_address -> {
                if (null == mCityDialog) {
                    mPresenter?.getProvinceList()
                } else {
                    mCityDialog?.show(supportFragmentManager, "city")
                }
            }
            R.id.tv_shop_background -> {
                mTag = 1
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        SelectPhotoDialog(this@MerchantInfoActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.btn_commit -> {
                mPresenter?.updateMerchantInfo(mAddress, mCityId, mConsignee, mCountyId,
                        mAvatar, mBackground, mName, mIntroduce, mPhone, mProvinceId, mWXChat)
            }
        }
    }

    override fun getMerchantInfoSuccess(bean: MerchantBean) {
        with(bean) {
            mAvatar = head_img
            mName = merchants_name
            mIntroduce = merchants_subtitle
            mWXChat = wechat_id
            mPhone = phone_no
            mAddress = address
            mBackground = merchants_back
            mIsUpdateName = is_update_name
            mConsignee = consignee
            mProvinceId = province_id
            mCityId = city_id
            mCountyId = county_id

            iv_avatar.loadImg(mAvatar)
            et_name.setText(mName)
            et_introduce.setText(mIntroduce)
            et_wx.setText(mWXChat)
            et_phone.setText(mPhone)
            et_consignee.setText(mConsignee)
            tv_address.text = pcc_name
            et_address.setText(address)
            iv_cover.loadImg(mBackground)

            et_name.isEnabled = mIsUpdateName == 0
        }
    }

    override fun uploadMerchantHeadSuccess(path: String) {
        mAvatar = path
        iv_avatar.loadImg(path)
    }

    override fun uploadMerchantBackgroundSuccess(path: String) {
        mBackground = path
        iv_cover.loadImg(path)
    }

    override fun updateMerchantInfoSuccess() {
        showToast("修改成功")

        SPUtils.putString("merchant_head_img", mAvatar ?: "")
        SPUtils.putString("merchant_name", mName ?: "")
        SPUtils.putString("merchant_desc", mIntroduce ?: "")

        val hashMap = HashMap<String, Any>().apply {
            put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, mAvatar ?: "")
            put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, mName ?: "")
        }
        TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, object : TIMCallBack {
            override fun onSuccess() {

            }

            override fun onError(p0: Int, p1: String?) {

            }
        })

        setResult(Activity.RESULT_OK)
        finish()
    }


    override fun getProvinceListSuccess(list: List<CityBean.DataBean>) {
        mCityDialog = CityDialog.newInstance(list)
        mCityDialog?.show(supportFragmentManager, "city")
    }

    override fun getAreaListSuccess(list: List<CityBean.DataBean>, position: Int, state: Int) {
        mCityDialog?.setAreaList(list, state, position)
    }

    override fun onCitySelect(position: Int, state: Int, parentId: Int) {
        mPresenter?.getAreaList(position, state, parentId)
    }

    override fun onSelectCity(province: CityBean.DataBean, city: CityBean.DataBean, county: CityBean.DataBean) {
        mProvinceId = province.dictId
        mCityId = city.dictId
        mCountyId = county.dictId

        tv_address.text = "${province.codeName}${city.codeName}${county.codeName}"
    }

}
