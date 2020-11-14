package com.miyin.zhenbaoqi.ui

import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gyf.immersionbar.ImmersionBar
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.activity.BaseActivity
import com.miyin.zhenbaoqi.ext.startActivity
import com.miyin.zhenbaoqi.ui.login.activity.WXLoginActivity
import com.miyin.zhenbaoqi.utils.SPUtils
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : BaseActivity() {

    private val mDrawableList = arrayOf(R.drawable.ic_guide_first, R.drawable.ic_guide_second, R.drawable.ic_guide_third, R.drawable.ic_guide_firth)
    private val mViewList = mutableListOf<ImageView>()

    override fun getContentView() = R.layout.activity_guide

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()

        mDrawableList.forEachIndexed { _, it ->
            val imageView = ImageView(applicationContext)
            imageView.setImageResource(it)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            mViewList.add(imageView)
        }

        view_pager.adapter = MyViewPagerAdapter()
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    visible(view_empty)
                } else {
                    invisible(view_empty)
                }
            }
        })

        view_empty.setOnClickListener {
            startActivity<WXLoginActivity>()
            SPUtils.putBoolean("show_guide", true)

            finish()
        }
    }

    override fun initData() {

    }

    private inner class MyViewPagerAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any) = (view == obj)

        override fun getCount() = mDrawableList.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = mViewList[position]
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(mViewList[position])
        }

    }

}
