package com.miyin.zhenbaoqi.widget.like

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.DensityUtils
import java.util.*

class LikeStarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var mBitmap: Bitmap
    private var mPoint: Point? = null
    private var mStartPoint: Point? = null
    private var mEndPoint: Point? = null
    private val mPaint = Paint()

    private fun init(context: Context) {
        val resources = context.resources
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_like)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap, mPoint!!.x.toFloat(), mPoint!!.y.toFloat(), mPaint)
        super.onDraw(canvas)
    }

    fun startAnim() {
        if (mStartPoint == null) {
            return
        }
        // 没有做适配，下面500，201等数字可以根据实际传入
        // 结束的点，起始点就是点击的点
        mEndPoint = Point(mStartPoint!!.x, mStartPoint!!.y - DensityUtils.dp2px(350f))
        // 随机左右偏移控制点位置
        val random = Random()
        val result = DensityUtils.dp2px(random.nextInt(100) - 50f)
        // 贝塞尔曲线的控制点
        val controllerPoint = Point(mStartPoint!!.x + result, mStartPoint!!.y - DensityUtils.dp2px(125f))
        val likeStarEvaluator = LikeStarEvaluator(controllerPoint)
        val animator = ValueAnimator.ofObject(likeStarEvaluator, mStartPoint, mEndPoint)
        animator.addUpdateListener { animation: ValueAnimator ->
            val point = animation.animatedValue as Point
            mPoint!!.x = point.x
            mPoint!!.y = point.y
            // 计算透明度
            alpha = (point.y - mEndPoint!!.y) / 500f
            invalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) { // 移除图像
                val viewGroup = parent as ViewGroup
                viewGroup.removeView(this@LikeStarView)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    /**
     * 设置初始点
     */
    fun setStartPoint(point: Point?) {
        mStartPoint = point
        mPoint = point
    }

    init {
        init(context)
    }
}