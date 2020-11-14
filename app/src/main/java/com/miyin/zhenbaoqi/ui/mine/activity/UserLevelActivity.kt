package com.miyin.zhenbaoqi.ui.mine.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseMvpActivity
import com.miyin.zhenbaoqi.bean.ResponseBean
import com.miyin.zhenbaoqi.bean.UserGradeBean
import com.miyin.zhenbaoqi.bean.UserLevelBean
import com.miyin.zhenbaoqi.ext.loadImg
import com.miyin.zhenbaoqi.ui.mine.adapter.UserLevelAdapter
import com.miyin.zhenbaoqi.ui.mine.contract.UserLevelContract
import com.miyin.zhenbaoqi.ui.mine.presenter.UserLevelPresenter
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_user_level.*

class UserLevelActivity : BaseMvpActivity<UserLevelContract.IView, UserLevelContract.IPresenter>(), UserLevelContract.IView {

    private val mList = mutableListOf<UserLevelBean.DataBean>()
    private lateinit var mAdapter: UserLevelAdapter

    override fun getContentView() = R.layout.activity_user_level

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initTitleBar("用户等级")
        immersionBar { statusBarDarkFont(true) }

        iv_avatar.loadImg(SPUtils.getString("head_img"))
        recycler_view.run {
            isNestedScrollingEnabled = false
            isFocusable = false
            isFocusableInTouchMode = false

            mAdapter = UserLevelAdapter(mList)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    override fun initData() {
        mPresenter?.getUserLevel()
    }

    override fun createPresenter() = UserLevelPresenter()

    @SuppressLint("SetTextI18n")
    override fun getUserLevelSuccess(bean: ResponseBean) {
        if (bean is UserGradeBean) {
            bean.data?.run {
                tv_name.text = SPUtils.getString("nick_name")
                iv_level.loadImg(icon)
                tv_user_integral.text = "唯爱分：${point}"
            }
        } else if (bean is UserLevelBean) {
            bean.data?.run {
                mList.addAll(this)
                mAdapter.setNewData(mList)
            }
        }
    }

}
