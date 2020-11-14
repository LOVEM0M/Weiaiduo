package com.miyin.zhenbaoqi.widget

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Checkable
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.DensityUtils.dp2px
import kotlin.math.max
import kotlin.math.min

class SwitchButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Checkable {

    /**
     * 阴影半径
     */
    private var mShadowRadius = 0
    /**
     * 阴影Y偏移px
     */
    private var shadowOffset = 0
    /**
     * 阴影颜色
     */
    private var shadowColor = 0
    /**
     * 背景半径
     */
    private var mViewRadius = 0f
    /**
     * 按钮半径
     */
    private var mButtonRadius = 0f
    /**
     * 背景位置
     */
    private var left = 0f
    private var top = 0f
    private var right = 0f
    private var bottom = 0f
    private var centerY = 0f
    /**
     * 背景底色
     */
    private var background = 0
    /**
     * 背景关闭颜色
     */
    private var uncheckColor = 0
    /**
     * 背景打开颜色
     */
    private var checkedColor = 0
    /**
     * 边框宽度px
     */
    private var borderWidth = 0
    /**
     * 打开指示线颜色
     */
    private var checkLineColor = 0
    /**
     * 打开指示线宽
     */
    private var checkLineWidth = 0
    /**
     * 打开指示线长
     */
    private var checkLineLength = 0f
    /**
     * 关闭圆圈颜色
     */
    private var uncheckCircleColor = 0
    /**
     * 关闭圆圈线宽
     */
    private var uncheckCircleWidth = 0
    /**
     * 关闭圆圈位移X
     */
    private var uncheckCircleOffsetX = 0f
    /**
     * 关闭圆圈半径
     */
    private var uncheckCircleRadius = 0f
    /**
     * 打开指示线位移X
     */
    private var checkedLineOffsetX = 0f
    /**
     * 打开指示线位移Y
     */
    private var checkedLineOffsetY = 0f
    /**
     * 按钮最左边
     */
    private var buttonMinX = 0f
    /**
     * 按钮最右边
     */
    private var buttonMaxX = 0f
    /**
     * 按钮画笔
     */
    private val mButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 背景画笔
     */
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 当前状态
     */
    private val mViewState = ViewState()
    private val mBeforeState = ViewState()
    private val mAfterState = ViewState()
    private val mRect = RectF()
    /**
     * 动画状态
     */
    private var mAnimateState = ANIMATE_STATE_NONE
    /**
     *
     */
    private var mValueAnimator: ValueAnimator
    private val mArgbEvaluator = ArgbEvaluator()
    /**
     * 是否选中
     */
    private var isChecked = false
    /**
     * 是否启用动画
     */
    private var enableEffect = false
    /**
     * 是否启用阴影效果
     */
    private var shadowEffect = false
    /**
     * 是否显示指示器
     */
    private var showIndicator = false
    /**
     * 收拾是否按下
     */
    private var isTouchingDown = false
    /**
     *
     */
    private var isUiInited = false
    /**
     *
     */
    private var isEventBroadcast = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null
    /**
     * 手势按下的时刻
     */
    private var touchDownTime: Long = 0

    private val animatorUpdateListener: AnimatorUpdateListener = object : AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator) {
            val value = animation.animatedValue as Float
            when (mAnimateState) {
                ANIMATE_STATE_PENDING_DRAG -> {
                    mViewState.checkedLineColor = mArgbEvaluator.evaluate(value, mBeforeState.checkedLineColor, mAfterState.checkedLineColor) as Int
                    mViewState.radius = (mBeforeState.radius + (mAfterState.radius - mBeforeState.radius) * value)
                    if (mAnimateState != ANIMATE_STATE_PENDING_DRAG) {
                        mViewState.buttonX = (mBeforeState.buttonX + (mAfterState.buttonX - mBeforeState.buttonX) * value)
                    }
                    mViewState.checkStateColor = mArgbEvaluator.evaluate(value, mBeforeState.checkStateColor, mAfterState.checkStateColor) as Int
                }
                ANIMATE_STATE_SWITCH -> {
                    mViewState.buttonX = (mBeforeState.buttonX + (mAfterState.buttonX - mBeforeState.buttonX) * value)
                    val fraction = (mViewState.buttonX - buttonMinX) / (buttonMaxX - buttonMinX)
                    mViewState.checkStateColor = mArgbEvaluator.evaluate(fraction, uncheckColor, checkedColor) as Int
                    mViewState.radius = fraction * mViewRadius
                    mViewState.checkedLineColor = mArgbEvaluator.evaluate(fraction, Color.TRANSPARENT, checkLineColor) as Int
                }
                ANIMATE_STATE_DRAGGING -> {
                    run {}
                    run {}
                }
                ANIMATE_STATE_NONE -> {
                }
                else -> {
                    run {}
                    run {}
                }
            }
            postInvalidate()
        }
    }
    private val animatorListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            when (mAnimateState) {
                ANIMATE_STATE_PENDING_DRAG -> {
                    mAnimateState = ANIMATE_STATE_DRAGGING
                    mViewState.checkedLineColor = Color.TRANSPARENT
                    mViewState.radius = mViewRadius
                    postInvalidate()
                }
                ANIMATE_STATE_PENDING_RESET -> {
                    mAnimateState = ANIMATE_STATE_NONE
                    postInvalidate()
                }
                ANIMATE_STATE_PENDING_SETTLE -> {
                    mAnimateState = ANIMATE_STATE_NONE
                    postInvalidate()
                    broadcastEvent()
                }
                ANIMATE_STATE_SWITCH -> {
                    isChecked = !isChecked
                    mAnimateState = ANIMATE_STATE_NONE
                    postInvalidate()
                    broadcastEvent()
                }
                ANIMATE_STATE_NONE -> {
                }
                else -> {
                }
            }
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(0, 0, 0, 0)
    }

    init {
        val buttonColor: Int
        val effectDuration: Int
        context.obtainStyledAttributes(attrs, R.styleable.SwitchButton).apply {
            shadowEffect = getBoolean(R.styleable.SwitchButton_sb_shadow_effect, true)
            uncheckCircleColor = getColor(R.styleable.SwitchButton_sb_uncheck_circle_color, 0xFFAAAAAA.toInt())
            uncheckCircleWidth = getDimensionPixelOffset(R.styleable.SwitchButton_sb_uncheck_circle_width, dp2px(1.5f))
            uncheckCircleOffsetX = dp2px(10f).toFloat()
            uncheckCircleRadius = getDimension(R.styleable.SwitchButton_sb_uncheck_circle_radius, dp2px(4f).toFloat())
            checkedLineOffsetX = dp2px(4f).toFloat()
            checkedLineOffsetY = dp2px(4f).toFloat()
            mShadowRadius = getDimensionPixelOffset(R.styleable.SwitchButton_sb_shadow_radius, dp2px(2.5f))
            shadowOffset = getDimensionPixelOffset(R.styleable.SwitchButton_sb_shadow_offset, dp2px(1.5f))
            shadowColor = getColor(R.styleable.SwitchButton_sb_shadow_color, 0X33000000)
            uncheckColor = getColor(R.styleable.SwitchButton_sb_uncheck_color, 0xFFDDDDDD.toInt())
            checkedColor = getColor(R.styleable.SwitchButton_sb_checked_color, 0xff51d367.toInt())
            borderWidth = getDimensionPixelOffset(R.styleable.SwitchButton_sb_border_width, dp2px(1f))
            checkLineColor = getColor(R.styleable.SwitchButton_sb_check_line_color, Color.WHITE)
            checkLineWidth = getDimensionPixelOffset(R.styleable.SwitchButton_sb_check_line_width, dp2px(1f))
            checkLineLength = dp2px(6f).toFloat()
            buttonColor = getColor(R.styleable.SwitchButton_sb_button_color, Color.WHITE)
            effectDuration = getInt(R.styleable.SwitchButton_sb_effect_duration, 300)
            isChecked = getBoolean(R.styleable.SwitchButton_sb_checked, false)
            showIndicator = getBoolean(R.styleable.SwitchButton_sb_show_indicator, true)
            background = getColor(R.styleable.SwitchButton_sb_background, Color.WHITE)
            enableEffect = getBoolean(R.styleable.SwitchButton_sb_enable_effect, true)
            recycle()
        }

        mButtonPaint.color = buttonColor
        if (shadowEffect) {
            mButtonPaint.setShadowLayer(mShadowRadius.toFloat(), 0f, shadowOffset.toFloat(), shadowColor)
        }

        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = effectDuration.toLong()
            repeatCount = 0
            addUpdateListener(animatorUpdateListener)
            addListener(animatorListener)
        }
        super.setClickable(true)
        setPadding(0, 0, 0, 0)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var widthMeasureSpec = widthSpec
        var heightMeasureSpec = heightSpec
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY)
        }
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY)
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val viewPadding = max(mShadowRadius + shadowOffset, borderWidth).toFloat()
        /* 背景高 */
        val height = h - viewPadding - viewPadding
        mViewRadius = height * .5f
        mButtonRadius = mViewRadius - borderWidth
        left = viewPadding
        top = viewPadding
        right = w - viewPadding
        bottom = h - viewPadding
        centerY = (top + bottom) * .5f
        buttonMinX = left + mViewRadius
        buttonMaxX = right - mViewRadius
        if (isChecked()) {
            setCheckedViewState(mViewState)
        } else {
            setUncheckViewState(mViewState)
        }
        isUiInited = true
        postInvalidate()
    }

    private fun setUncheckViewState(viewState: ViewState) {
        viewState.radius = 0f
        viewState.checkStateColor = uncheckColor
        viewState.checkedLineColor = Color.TRANSPARENT
        viewState.buttonX = buttonMinX
    }

    private fun setCheckedViewState(viewState: ViewState) {
        viewState.radius = mViewRadius
        viewState.checkStateColor = checkedColor
        viewState.checkedLineColor = checkLineColor
        viewState.buttonX = buttonMaxX
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.strokeWidth = borderWidth.toFloat()
        mPaint.style = Paint.Style.FILL
        // 绘制白色背景
        mPaint.color = background
        drawRoundRect(canvas, left, top, right, bottom, mViewRadius)
        // 绘制关闭状态的边框
        mPaint.style = Paint.Style.STROKE
        mPaint.color = uncheckColor
        drawRoundRect(canvas, left, top, right, bottom, mViewRadius)
        // 绘制小圆圈
        if (showIndicator) {
            drawUncheckIndicator(canvas)
        }
        // 绘制开启背景色
        val des = mViewState.radius * .5f
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mViewState.checkStateColor
        mPaint.strokeWidth = borderWidth + des * 2f
        drawRoundRect(canvas, left + des, top + des, right - des, bottom - des, mViewRadius)
        // 绘制按钮左边绿色长条遮挡
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 1f
        drawArc(canvas, left, top, left + 2 * mViewRadius, top + 2 * mViewRadius)
        canvas.drawRect(left + mViewRadius, top, mViewState.buttonX, top + 2 * mViewRadius, mPaint)
        // 绘制小线条
        if (showIndicator) {
            drawCheckedIndicator(canvas)
        }
        //绘制按钮
        drawButton(canvas, mViewState.buttonX, centerY)
    }

    /**
     * 绘制选中状态指示器
     */
    private fun drawCheckedIndicator(canvas: Canvas) {
        mPaint.run {
            style = Paint.Style.STROKE
            color = mViewState.checkedLineColor
            strokeWidth = checkLineWidth.toFloat()
        }

        val sx = left + mViewRadius - checkedLineOffsetX
        val sy = centerY - checkLineLength
        val ex = left + mViewRadius - checkedLineOffsetY
        val ey = centerY + checkLineLength
        canvas.drawLine(sx, sy, ex, ey, mPaint)
    }

    /**
     * 绘制关闭状态指示器
     */
    private fun drawUncheckIndicator(canvas: Canvas) {
        mPaint.run {
            style = Paint.Style.STROKE
            color = uncheckCircleColor
            strokeWidth = uncheckCircleWidth.toFloat()
            canvas.drawCircle(right - uncheckCircleOffsetX, centerY, uncheckCircleRadius, this)
        }
    }

    private fun drawArc(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(left, top, right, bottom, 90f, 180f, true, mPaint)
        } else {
            mRect[left, top, right] = bottom
            canvas.drawArc(mRect, 90f, 180f, true, mPaint)
        }
    }

    private fun drawRoundRect(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, backgroundRadius: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, backgroundRadius, backgroundRadius, mPaint)
        } else {
            mRect[left, top, right] = bottom
            canvas.drawRoundRect(mRect, backgroundRadius, backgroundRadius, mPaint)
        }
    }

    private fun drawButton(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, mButtonRadius, mButtonPaint)
        mPaint.run {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = -0x222223
            canvas.drawCircle(x, y, mButtonRadius, this)
        }
    }

    override fun setChecked(checked: Boolean) {
        if (checked == isChecked()) {
            postInvalidate()
            return
        }
        toggle(enableEffect, false)
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        toggle(true)
    }

    /**
     * 切换状态
     */
    fun toggle(animate: Boolean) {
        toggle(animate, true)
    }

    private fun toggle(animate: Boolean, broadcast: Boolean) {
        if (!isEnabled) {
            return
        }
        if (isEventBroadcast) {
            throw RuntimeException("should NOT switch the state in method: [onCheckedChanged]!")
        }
        if (!isUiInited) {
            isChecked = !isChecked
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        if (mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
        if (!enableEffect || !animate) {
            isChecked = !isChecked
            if (isChecked()) {
                setCheckedViewState(mViewState)
            } else {
                setUncheckViewState(mViewState)
            }
            postInvalidate()
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        mAnimateState = ANIMATE_STATE_SWITCH
        mBeforeState.copy(mViewState)
        if (isChecked()) { // 切换到 unchecked
            setUncheckViewState(mAfterState)
        } else {
            setCheckedViewState(mAfterState)
        }
        mValueAnimator.start()
    }

    private fun broadcastEvent() {
        if (mOnCheckedChangeListener != null) {
            isEventBroadcast = true
            mOnCheckedChangeListener?.onCheckedChanged(this, isChecked())
        }
        isEventBroadcast = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isTouchingDown = true
                touchDownTime = System.currentTimeMillis()
                // 取消准备进入拖动状态
                removeCallbacks(postPendingDrag)
                // 预设 100ms 进入拖动状态
                postDelayed(postPendingDrag, 100)
            }
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                if (isPendingDragState) { // 在准备进入拖动状态过程中，可以拖动按钮位置
                    var fraction = eventX / width
                    fraction = max(0f, min(1f, fraction))
                    mViewState.buttonX = (buttonMinX + (buttonMaxX - buttonMinX) * fraction)
                } else if (isDragState) { // 拖动按钮位置，同时改变对应的背景颜色
                    var fraction = eventX / width
                    fraction = max(0f, min(1f, fraction))
                    mViewState.buttonX = (buttonMinX + (buttonMaxX - buttonMinX) * fraction)
                    mViewState.checkStateColor = mArgbEvaluator.evaluate(fraction, uncheckColor, checkedColor) as Int
                    postInvalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                isTouchingDown = false
                // 取消准备进入拖动状态
                removeCallbacks(postPendingDrag)
                if (System.currentTimeMillis() - touchDownTime <= 300) { // 点击时间小于 300ms，认为是点击操作
                    toggle()
                } else if (isDragState) { // 在拖动状态，计算按钮位置，设置是否切换状态
                    val eventX = event.x
                    var fraction = eventX / width
                    fraction = max(0f, min(1f, fraction))
                    val newCheck = fraction > .5f
                    if (newCheck == isChecked()) {
                        pendingCancelDragState()
                    } else {
                        isChecked = newCheck
                        pendingSettleState()
                    }
                } else if (isPendingDragState) { // 在准备进入拖动状态过程中，取消之，复位
                    pendingCancelDragState()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                isTouchingDown = false
                removeCallbacks(postPendingDrag)
                if (isPendingDragState || isDragState) { // 复位
                    pendingCancelDragState()
                }
            }
        }
        return true
    }

    /**
     * 是否在动画状态
     */
    private val isInAnimating: Boolean
        get() = mAnimateState != ANIMATE_STATE_NONE

    /**
     * 是否在进入拖动或离开拖动状态
     */
    private val isPendingDragState: Boolean
        get() = (mAnimateState == ANIMATE_STATE_PENDING_DRAG || mAnimateState == ANIMATE_STATE_PENDING_RESET)

    /**
     * 是否在手指拖动状态
     */
    private val isDragState: Boolean
        get() = mAnimateState == ANIMATE_STATE_DRAGGING

    /**
     * 设置是否启用阴影效果
     */
    fun setShadowEffect(shadowEffect: Boolean) {
        if (this.shadowEffect == shadowEffect) {
            return
        }
        this.shadowEffect = shadowEffect
        if (this.shadowEffect) {
            mButtonPaint.setShadowLayer(mShadowRadius.toFloat(), 0f, shadowOffset.toFloat(), shadowColor)
        } else {
            mButtonPaint.setShadowLayer(0f, 0f, 0f, 0)
        }
    }

    fun setEnableEffect(enable: Boolean) {
        enableEffect = enable
    }

    /**
     * 开始进入拖动状态
     */
    private fun pendingDragState() {
        if (isInAnimating) {
            return
        }
        if (!isTouchingDown) {
            return
        }
        if (mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
        mAnimateState = ANIMATE_STATE_PENDING_DRAG
        mBeforeState.copy(mViewState)
        mAfterState.copy(mViewState)
        if (isChecked()) {
            mAfterState.checkStateColor = checkedColor
            mAfterState.buttonX = buttonMaxX
            mAfterState.checkedLineColor = checkedColor
        } else {
            mAfterState.checkStateColor = uncheckColor
            mAfterState.buttonX = buttonMinX
            mAfterState.radius = mViewRadius
        }
        mValueAnimator.start()
    }

    /**
     * 取消拖动状态
     */
    private fun pendingCancelDragState() {
        if (isDragState || isPendingDragState) {
            if (mValueAnimator.isRunning) {
                mValueAnimator.cancel()
            }
            mAnimateState = ANIMATE_STATE_PENDING_RESET
            mBeforeState.copy(mViewState)
            if (isChecked()) {
                setCheckedViewState(mAfterState)
            } else {
                setUncheckViewState(mAfterState)
            }
            mValueAnimator.start()
        }
    }

    /**
     * 动画-设置新的状态
     */
    private fun pendingSettleState() {
        if (mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
        mAnimateState = ANIMATE_STATE_PENDING_SETTLE
        mBeforeState.copy(mViewState)
        if (isChecked()) {
            setCheckedViewState(mAfterState)
        } else {
            setUncheckViewState(mAfterState)
        }
        mValueAnimator.start()
    }

    override fun setOnClickListener(l: OnClickListener?) {}

    override fun setOnLongClickListener(l: OnLongClickListener?) {}

    fun setOnCheckedChangeListener(l: OnCheckedChangeListener?) {
        mOnCheckedChangeListener = l
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean)
    }

    private val postPendingDrag = Runnable {
        if (!isInAnimating) {
            pendingDragState()
        }
    }

    /**
     * 保存动画状态
     */
    private class ViewState internal constructor() {
        /**
         * 按钮x位置[buttonMinX-buttonMaxX]
         */
        var buttonX = 0f
        /**
         * 状态背景颜色
         */
        var checkStateColor = 0
        /**
         * 选中线的颜色
         */
        var checkedLineColor = 0
        /**
         * 状态背景的半径
         */
        var radius = 0f

        fun copy(source: ViewState) {
            buttonX = source.buttonX
            checkStateColor = source.checkStateColor
            checkedLineColor = source.checkedLineColor
            radius = source.radius
        }
    }

    companion object {
        private val DEFAULT_WIDTH = dp2px(58f)
        private val DEFAULT_HEIGHT = dp2px(36f)

        /**
         * 动画状态：
         * 1.静止
         * 2.进入拖动
         * 3.处于拖动
         * 4.拖动-复位
         * 5.拖动-切换
         * 6.点击切换
         */
        private const val ANIMATE_STATE_NONE = 0
        private const val ANIMATE_STATE_PENDING_DRAG = 1
        private const val ANIMATE_STATE_DRAGGING = 2
        private const val ANIMATE_STATE_PENDING_RESET = 3
        private const val ANIMATE_STATE_PENDING_SETTLE = 4
        private const val ANIMATE_STATE_SWITCH = 5

    }
}
