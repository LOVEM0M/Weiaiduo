package com.miyin.zhenbaoqi.ui.mine.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.luck.picture.lib.config.PictureConfig
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.callback.OnPermissionCallback
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.mine.contract.UserInfoContract
import com.miyin.zhenbaoqi.ui.mine.dialog.SelectPhotoDialog
import com.miyin.zhenbaoqi.ui.mine.presenter.UserInfoPresenter
import com.miyin.zhenbaoqi.utils.EditWatcher
import kotlinx.android.synthetic.main.activity_user_info.*
import com.luck.picture.lib.PictureSelector
import com.miyin.zhenbaoqi.bean.ReferrerBean
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.common.SingleDataDialog
import com.miyin.zhenbaoqi.utils.SPUtils
import com.miyin.zhenbaoqi.utils.TimeUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class UserInfoActivity : BaseMvpActivity<UserInfoContract.IView, UserInfoContract.IPresenter>(), UserInfoContract.IView, OnDialogCallback {

    private var mName: String? = null
    private var mPhone: String? = null
    private var mBirthday: String? = null
    private var mGender = 1
    private var mTimePickerView: TimePickerView? = null
    private var mState = 0
    private var mBean: ReferrerBean? = null

    override fun getContentView(): Int {
        mState = intent.getIntExtra("state", 0)
        return R.layout.activity_user_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("账户设置")
        immersionBar { statusBarDarkFont(true) }

        iv_avatar.loadImg(SPUtils.getString("head_img"))

        et_name.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mName = editable.toString().trim { it <= ' ' }
            }
        })
        et_phone.addTextChangedListener(object : EditWatcher() {
            override fun afterTextChanged(editable: Editable?) {
                mPhone = editable.toString().trim { it <= ' ' }
            }
        })

        mName = SPUtils.getString("nick_name")
        if (!mName.isNullOrEmpty()) {
            et_name.setText(mName)
        }
        mPhone = SPUtils.getString("phone")
        if (!TextUtils.isEmpty(mPhone)) {
            et_phone.setText(mPhone)
        }
        mGender = SPUtils.getInt("gender", mGender)
        tv_gender.text = if (mGender == 1) "男" else "女"

        mBirthday = SPUtils.getString("birthday")
        tv_birthday.text = mBirthday

        initTimeDialog()

        setOnClickListener(iv_avatar, fl_gender, fl_birthday, tv_referrer, btn_commit)
    }

    override fun initData() {
        mPresenter?.referrerInfo()
    }

    override fun createPresenter() = UserInfoPresenter()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.run {
                val list = PictureSelector.obtainMultipleResult(this)
                list.forEach {
                    if (it.isCompressed) {
                        mPresenter?.updateHeadImg(it.compressPath)
                    }
                }
            }
        }
    }

    private fun initTimeDialog() {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        startDate.set(1950, 0, 1)

        val currentDate = Calendar.getInstance()
        if (!mBirthday.isNullOrEmpty()) {
            val dateArray = mBirthday!!.split("-")
            currentDate.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
        }

        mTimePickerView = TimePickerBuilder(this) { date, _ ->
            mBirthday = TimeUtils.date2String(date, "yyyy-MM-dd")
            tv_birthday.text = mBirthday

            SPUtils.putString("birthday", mBirthday!!)
        }.setType(booleanArrayOf(true, true, true, false, false, false))
                .setCancelText(getString(R.string.cancel))
                .setSubmitText(getString(R.string.confirm))
                .setTitleSize(15)
                .setOutSideCancelable(true)
                .setSubmitColor(Color.parseColor("#333333"))
                .setCancelColor(Color.parseColor("#666666"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setBgColor(Color.WHITE)
                .setDate(currentDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .isDialog(true)
                .build()

        mTimePickerView?.dialog?.let {
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)

            params.leftMargin = 0
            params.rightMargin = 0
            mTimePickerView!!.dialogContainerLayout.layoutParams = params

            it.window
        }?.let {
            val params = it.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = params

            it.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
            it.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_avatar -> requestPermission(getString(R.string.permission_camera_storage), object : OnPermissionCallback {
                override fun onGranted() {
                    SelectPhotoDialog(this@UserInfoActivity).builder().show()
                }

                override fun onDenied() {
                    showToast(getString(R.string.permission_camera_storage))
                }
            }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            R.id.fl_gender -> {
                val dialog = SingleDataDialog.newInstance(listOf("男", "女"))
                dialog.show(supportFragmentManager, "gender")
            }
            R.id.fl_birthday -> mTimePickerView?.show()
            R.id.tv_referrer -> {
                mBean?.data?.run {
                    if (state == 1 && merchants_id != 0) {
                        //startActivity<ReferrerActivity>("bean" to mBean)
                    }
                }
            }
            R.id.btn_commit -> mPresenter?.updateUserInfo(mBirthday, mGender, mName)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun referrerInfoSuccess(bean: ReferrerBean) {
        mBean = bean
        bean.data?.run {
            if (state == 0) {
                tv_referrer.text = "暂无推荐人"
            } else {
                if (merchants_id != 0) {
                    tv_referrer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow_small, 0)
                    tv_referrer.text = "商户id:$user_id\u3000$nick_name"
                } else {
                    tv_referrer.text = "会员id:$user_id\u3000$nick_name"
                }
            }
        }
    }

    override fun updateHeadImgSuccess(path: String) {
        iv_avatar.loadImg(path)
        SPUtils.putString("head_img", path)
        EventBus.getDefault().post("updateAvatar")
    }

    override fun updateUserInfoSuccess() {
        SPUtils.putString("nick_name", mName!!)
        SPUtils.putString("birthday", mBirthday!!)
        SPUtils.putInt("gender", mGender)
        EventBus.getDefault().post("updateNickName")

        showToast("修改成功")
        finish()
    }

    override fun onDialog(obj: Any, flag: Int) {
        if (obj is String) {
            tv_gender.text = obj
            mGender = if (obj == "男") 1 else 2
            SPUtils.putInt("gender", mGender)
        }
    }

}
