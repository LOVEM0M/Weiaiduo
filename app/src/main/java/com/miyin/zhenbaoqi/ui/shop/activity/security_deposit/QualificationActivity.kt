package com.miyin.zhenbaoqi.ui.shop.activity.security_deposit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.CityBean
import com.miyin.zhenbaoqi.bean.MerchantBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.common.SingleDataDialog
import com.miyin.zhenbaoqi.ui.mine.dialog.CityDialog
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.shop.contract.QualificationContract
import com.miyin.zhenbaoqi.ui.shop.presenter.QualificationPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMFriendshipManager
import com.tencent.imsdk.TIMUserProfile
import kotlinx.android.synthetic.main.activity_qualification.*

@SuppressLint("SetTextI18n")
class QualificationActivity : BaseMvpActivity<QualificationContract.IView, QualificationContract.IPresenter>(),
        QualificationContract.IView, OnDialogCallback, CityDialog.OnCitySelectCallback {

    private var mSelectType = 0
    private var mAvatar: String? = null
    private var mCover: String? = null
    private var mLeftPath: String? = null
    private var mMiddlePath: String? = null
    private var mRightPath: String? = null
    private var mGoodsLicense: String? = null
    private var mIdentityType = 0

    private var mAddress: String? = null
    private var mCityId = 0
    private var mConsignee: String? = null
    private var mCountyId = 0
    private var mMainCate = 0
    private var mMerchantsName: String? = null
    private var mMerchantsSubtitle: String? = null
    private var mPhoneNo: String? = null
    private var mProvinceId = 0
    private var mWechatId: String? = null

    private var mCityDialog: CityDialog? = null

    override fun getContentView(): Int {
        return R.layout.activity_qualification
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("资质提交")
        immersionBar { statusBarDarkFont(true) }

        et_shop_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mMerchantsName = editable.toString().trim { it <= ' ' }
            }
        })
        et_shop_introduce.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mMerchantsSubtitle = editable.toString().trim { it <= ' ' }
            }
        })
        et_shop_wx.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mWechatId = editable.toString().trim { it <= ' ' }
            }
        })
        et_phone.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPhoneNo = editable.toString().trim { it <= ' ' }
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

        setOnClickListener(fl_avatar, iv_cover, tv_ship_address, fl_select_identity, iv_left, iv_middle,
                iv_right, fl_select_product, iv_goods_license, btn_commit)
    }

    override fun initData() {
        mPresenter?.getMerchantInfo()
    }

    override fun createPresenter() = QualificationPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                PictureSelector.obtainMultipleResult(this).forEach {
                    if (it.isCompressed) {
                        val path = it.compressPath
                        mPresenter?.uploadImage(path)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_avatar -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 0
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.iv_cover -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 1
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.tv_ship_address -> mPresenter?.getProvinceList()
            R.id.fl_select_identity -> {
                val dialog = SingleDataDialog.newInstance(listOf("个人", "个体户/企业"))
                dialog.show(supportFragmentManager, "identity")
            }
            R.id.iv_left -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 2
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.iv_middle -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 3
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.iv_right -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 4
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.fl_select_product -> mPresenter?.getCategoryList(1)//TODO 暂不确定传值
            R.id.iv_goods_license -> {
                requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                    override fun onGranted() {
                        mSelectType = 5
                        SelectPhotoDialog(this@QualificationActivity).builder().setSelectNum(1).show()
                    }

                    override fun onDenied() {
                        showToast(getString(R.string.permission_camera_storage))
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            R.id.btn_commit -> {
                if (mLeftPath.isNullOrEmpty() or mMiddlePath.isNullOrEmpty()) {
                    showToast("请上传对应图片")
                    return
                }
                val identityImage = if (mRightPath.isNullOrEmpty()) {
                    "$mLeftPath,$mMiddlePath"
                } else {
                    "$mLeftPath,$mMiddlePath,$mRightPath"
                }

                mPresenter?.merchantAuth(mAddress, mCityId, mConsignee, mCountyId, mAvatar, identityImage, mIdentityType, mGoodsLicense
                        ?: "",
                        mMainCate, mCover, mMerchantsName, mMerchantsSubtitle, mPhoneNo, mProvinceId, mWechatId)
            }
        }
    }

    override fun getMerchantInfoSuccess(bean: MerchantBean) {
        with(bean) {
            mAvatar = head_img
            mMerchantsName = merchants_name
            mMerchantsSubtitle = merchants_subtitle
            mWechatId = wechat_id
            mPhoneNo = phone_no
            mConsignee = consignee
            mProvinceId = province_id
            mCityId = city_id
            mCountyId = county_id
            mAddress = address
            mCover = merchants_back

            iv_avatar.loadImg(head_img)
            et_shop_name.setText(merchants_name)
            et_shop_introduce.setText(merchants_subtitle)
            et_shop_wx.setText(wechat_id)
            et_phone.setText(phone_no)
            et_consignee.setText(consignee)
            tv_ship_address.text = pcc_name
            et_address.setText(address)
            iv_cover.loadImg(merchants_back)
        }
    }

    override fun uploadImageSuccess(path: String) {
        when (mSelectType) {
            0 -> {
                mAvatar = path
                iv_avatar.loadImg(path)
            }
            1 -> {
                mCover = path
                iv_cover.loadImg(path)
            }
            2 -> {
                mLeftPath = path
                iv_left.loadImg(path)
            }
            3 -> {
                mMiddlePath = path
                iv_middle.loadImg(path)
            }
            4 -> {
                mRightPath = path
                iv_right.loadImg(path)
            }
            5 -> {
                mGoodsLicense = path
                iv_goods_license.loadImg(path)
            }
        }
    }

    override fun merchantAuthSuccess() {
        val hashMap = HashMap<String, Any>().apply {
            put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, mAvatar ?: "")
            put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, mMerchantsName ?: "")
        }
        TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, object : TIMCallBack {
            override fun onSuccess() {

            }

            override fun onError(p0: Int, p1: String?) {

            }
        })

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

        mAddress = ""
        et_address.setText("")

        tv_ship_address.text = "${province.codeName}${city.codeName}${county.codeName}"
    }

    override fun getCategoryListSuccess(bean: CityBean) {
        val list = bean.data!!.toMutableList()

        val dialog = SingleDataDialog.newInstance(list)
        dialog.show(supportFragmentManager, "cate")
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            when (obj) {
                "个人" -> {
                    if (mIdentityType == 1) {
                        return
                    }
                    Glide.with(this).clear(iv_left)
                    Glide.with(this).clear(iv_middle)

                    mLeftPath = ""
                    mMiddlePath = ""

                    tv_select_identity.text = obj
                    visible(iv_left, iv_middle, iv_right)

                    iv_left.setBackgroundResource(R.drawable.ic_id_card_positive)
                    iv_middle.setBackgroundResource(R.drawable.ic_id_card_reverse)
                    iv_right.setBackgroundResource(R.drawable.ic_award_certificate)

                    mIdentityType = 1
                }
                "个体户/企业" -> {
                    if (mIdentityType == 2) {
                        return
                    }
                    Glide.with(this).clear(iv_left)
                    Glide.with(this).clear(iv_middle)
                    Glide.with(this).clear(iv_right)

                    mLeftPath = ""
                    mMiddlePath = ""
                    mRightPath = ""

                    tv_select_identity.text = obj
                    visible(iv_left, iv_middle)
                    invisible(iv_right)

                    iv_left.setBackgroundResource(R.drawable.ic_business_license)
                    iv_middle.setBackgroundResource(R.drawable.ic_id_card)

                    mIdentityType = 2
                }
            }
        } else if (obj is CityBean.DataBean) {
            if (mMainCate == obj.dictId) {
                return
            }

            mMainCate = obj.dictId
            tv_select_product.text = obj.codeName

            if (!mGoodsLicense.isNullOrEmpty()) {
                mGoodsLicense = ""
                Glide.with(this).clear(iv_goods_license)
            }

            if (obj.codeName == "滋补养生") {
                visible(fl_goods_license)
            } else {
                gone(fl_goods_license)
            }
        }
    }

}
