package com.miyin.zhenbaoqi.utils

import android.app.Activity
import android.app.Dialog
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import com.bumptech.glide.Glide

import java.util.ArrayList

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.ext.loadImg
import java.lang.ref.WeakReference

object MaxImageView {

    private val mArrayLive = ArrayList<String>()
    private var mDialog: Dialog? = null
    private var mActivity: WeakReference<Activity>? = null

    /**
     * 多张图片展示
     */
    fun maxImageView(activity: Activity, list: List<String>, position: Int) {
        mActivity = WeakReference(activity)
        mArrayLive.clear()
        mArrayLive.addAll(list)

        // 全屏显示的方法
        val context = mActivity?.get()!!
        mDialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val viewPager = ViewPager(context).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            adapter = VPAdapter()
            currentItem = position
        }
        mDialog!!.setContentView(viewPager)
        mDialog!!.show()
    }

    /**
     * 单个大图展示
     */
    fun singleImageView(activity: Activity, url: String) {
        mActivity = WeakReference(activity)
        val context = mActivity?.get()!!

        mDialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            Glide.with(context).load(url).into(this)
        }
        mDialog!!.setContentView(imageView)
        mDialog!!.show()
        imageView.setOnClickListener { mDialog!!.dismiss() }
    }

    fun clear() {
        if (null != mActivity) {
            mActivity?.clear()
            mActivity = null
        }
        if (null != mDialog) {
            mDialog = null
        }
    }

    /**
     * ViewPager 的适配器
     */
    internal class VPAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mArrayLive.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val url = mArrayLive[position]
            return ImageView(mActivity?.get()).apply {
                val dimensionPixelSize = mActivity?.get()?.getDimensionPixelSize(R.dimen.dp_360)!!
                layoutParams = FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize)
                loadImg(url)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setOnClickListener { mDialog!!.dismiss() }
                container.addView(this)
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

}
