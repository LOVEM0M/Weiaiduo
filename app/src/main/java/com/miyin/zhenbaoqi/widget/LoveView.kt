package com.miyin.zhenbaoqi.widget

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.miyin.zhenbaoqi.R
import java.util.*

class LoveView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(mContext, attrs, defStyleAttr) {

    // 随机心形图片角度
    private val mAngle = floatArrayOf(-30f, -20f, 0f, 20f, 30f)
    // 记录上一次的点击时间
    private var mLastClickTime = 0L
    private var mOnMultiClickListener: OnMultiClickListener? = null
    private val mHandler = Handler()
    private val mRandom = Random()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // 获取点击时间
            val currTime = System.currentTimeMillis()
            // 判断点击之间的时间差
            val interval = currTime - mLastClickTime
            mLastClickTime = currTime
            if (interval < INTERVAL) {
                if (childCount == 0) {
                    mHandler.removeCallbacksAndMessages(null)
                    mOnMultiClickListener?.onMultiClick()
                }

                val imageView = ImageView(mContext)
                // 设置展示的位置，需要在手指触摸的位置上方，即触摸点是心形的右下角的位置
                val size = mContext.resources.getDimensionPixelOffset(R.dimen.dp_75)
                val params = LayoutParams(size, size)
                params.leftMargin = event.x.toInt() - size / 2
                params.topMargin = event.y.toInt() - size
                // 设置图片资源
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like))
                imageView.layoutParams = params
                // 把 ImageView 添加到父布局当中
                addView(imageView)

                //设置控件的动画
                val animatorSet = AnimatorSet()
                // 缩放动画，X轴2倍缩小至0.9倍
                animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0)) // 缩放动画，Y轴2倍缩放至0.9倍
                        .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0)) // 旋转动画，随机旋转角
                        .with(rotation(imageView, 0, 0, mAngle[mRandom.nextInt(4)])) // 渐变透明动画，透明度从0-1
                        .with(alpha(imageView, 0f, 1f, 100, 0)) // 缩放动画，X轴0.9倍缩小至
                        .with(scale(imageView, "scaleX", 0.9f, 1f, 50, 150)) // 缩放动画，Y轴0.9倍缩放至
                        .with(scale(imageView, "scaleY", 0.9f, 1f, 50, 150)) // 位移动画，Y轴从0上移至600
                        .with(translationY(imageView, 0f, -600f, 800, 400)) // 透明动画，从1-0
                        .with(alpha(imageView, 1f, 0f, 300, 400)) // 缩放动画，X轴1至3倍
                        .with(scale(imageView, "scaleX", 1f, 3f, 700, 400)) // 缩放动画，Y轴1至3倍
                        .with(scale(imageView, "scaleY", 1f, 3f, 700, 400))
                // 开始动画
                animatorSet.start()
                // 设置动画结束监听
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        // 当动画结束以后，需要把控件从父布局移除
                        removeViewInLayout(imageView)
                    }
                })
            } else {
                mHandler.postDelayed({
                    mOnMultiClickListener?.onSingleClick()
                }, INTERVAL)
            }
        }
        return true
    }

    fun setOnMultiClickListener(onMultiClickListener: OnMultiClickListener) {
        mOnMultiClickListener = onMultiClickListener
    }

    interface OnMultiClickListener {
        fun onSingleClick()

        fun onMultiClick()
    }

    companion object {
        // 点击的时间间隔
        private const val INTERVAL = 400L

        fun scale(view: View?, propertyName: String?, from: Float, to: Float, time: Long, delayTime: Long): ObjectAnimator {
            val translation = ObjectAnimator.ofFloat(view, propertyName, from, to)
            translation.interpolator = LinearInterpolator()
            translation.startDelay = delayTime
            translation.duration = time
            return translation
        }

        fun translationX(view: View?, from: Float, to: Float, time: Long, delayTime: Long): ObjectAnimator {
            val translation = ObjectAnimator.ofFloat(view, "translationX", from, to)
            translation.interpolator = LinearInterpolator()
            translation.startDelay = delayTime
            translation.duration = time
            return translation
        }

        fun translationY(view: View?, from: Float, to: Float, time: Long, delayTime: Long): ObjectAnimator {
            val translation = ObjectAnimator.ofFloat(view, "translationY", from, to)
            translation.interpolator = LinearInterpolator()
            translation.startDelay = delayTime
            translation.duration = time
            return translation
        }

        fun alpha(view: View?, from: Float, to: Float, time: Long, delayTime: Long): ObjectAnimator {
            val translation = ObjectAnimator.ofFloat(view, "alpha", from, to)
            translation.interpolator = LinearInterpolator()
            translation.startDelay = delayTime
            translation.duration = time
            return translation
        }

        fun rotation(view: View?, time: Long, delayTime: Long, vararg values: Float): ObjectAnimator {
            val rotation = ObjectAnimator.ofFloat(view, "rotation", *values)
            rotation.duration = time
            rotation.startDelay = delayTime
            rotation.interpolator = TimeInterpolator { input: Float -> input }
            return rotation
        }
    }

}
