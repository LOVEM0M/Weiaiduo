package com.miyin.zhenbaoqi.utils

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import android.util.StateSet
import android.view.View

object ShadowUtils {

    private const val SHADOW_TAG = -16

    fun apply(vararg views: View?) {
        for (view in views) {
            apply(view, Config())
        }
    }

    fun apply(view: View?, builder: Config?) {
        if (view == null || builder == null) return
        var background = view.background
        val tag = view.getTag(SHADOW_TAG)
        if (tag is Drawable) {
            ViewCompat.setBackground(view, tag)
        } else {
            background = builder.apply(background)
            ViewCompat.setBackground(view, background)
            view.setTag(SHADOW_TAG, background)
        }
    }

    class Config {
        private var mShadowRadius = -1f
        private var mShadowSizeNormal = -1f
        private var mShadowSizePressed = -1f
        private var mShadowMaxSizeNormal = -1f
        private var mShadowMaxSizePressed = -1f
        private var mShadowColorNormal = SHADOW_COLOR_DEFAULT
        private var mShadowColorPressed = SHADOW_COLOR_DEFAULT
        private var isCircle = false

        fun setShadowRadius(radius: Float): Config {
            mShadowRadius = radius
            require(!isCircle) { "Set circle needn't set radius." }
            return this
        }

        fun setCircle(): Config {
            isCircle = true
            require(mShadowRadius == -1f) { "Set circle needn't set radius." }
            return this
        }

        fun setShadowSize(size: Int): Config {
            return setShadowSize(size, size)
        }

        fun setShadowSize(sizeNormal: Int, sizePressed: Int): Config {
            mShadowSizeNormal = sizeNormal.toFloat()
            mShadowSizePressed = sizePressed.toFloat()
            return this
        }

        fun setShadowMaxSize(maxSize: Int): Config {
            return setShadowMaxSize(maxSize, maxSize)
        }

        fun setShadowMaxSize(maxSizeNormal: Int, maxSizePressed: Int): Config {
            mShadowMaxSizeNormal = maxSizeNormal.toFloat()
            mShadowMaxSizePressed = maxSizePressed.toFloat()
            return this
        }

        fun setShadowColor(color: Int): Config {
            return setShadowColor(color, color)
        }

        fun setShadowColor(colorNormal: Int, colorPressed: Int): Config {
            mShadowColorNormal = colorNormal
            mShadowColorPressed = colorPressed
            return this
        }

        fun apply(srcDrawable: Drawable?): Drawable {
            var src = srcDrawable
            if (src == null) {
                src = ColorDrawable(Color.TRANSPARENT)
            }
            val drawable = StateListDrawable()
            drawable.addState(intArrayOf(android.R.attr.state_pressed),
                    ShadowDrawable(src, shadowRadius, shadowSizeNormal,
                            shadowMaxSizeNormal, mShadowColorPressed, isCircle)
            )
            drawable.addState(
                    StateSet.WILD_CARD,
                    ShadowDrawable(src, shadowRadius, shadowSizePressed,
                            shadowMaxSizePressed, mShadowColorNormal, isCircle)
            )
            return drawable
        }

        private val shadowRadius: Float
            get() {
                if (mShadowRadius == -1f) {
                    mShadowRadius = 0f
                }
                return mShadowRadius
            }

        private val shadowSizeNormal: Float
            get() {
                if (mShadowSizeNormal == -1f) {
                    mShadowSizeNormal = SHADOW_SIZE.toFloat()
                }
                return mShadowSizeNormal
            }

        private val shadowSizePressed: Float
            get() {
                if (mShadowSizePressed == -1f) {
                    mShadowSizePressed = shadowSizeNormal
                }
                return mShadowSizePressed
            }

        private val shadowMaxSizeNormal: Float
            get() {
                if (mShadowMaxSizeNormal == -1f) {
                    mShadowMaxSizeNormal = shadowSizeNormal
                }
                return mShadowMaxSizeNormal
            }

        private val shadowMaxSizePressed: Float
            get() {
                if (mShadowMaxSizePressed == -1f) {
                    mShadowMaxSizePressed = shadowSizePressed
                }
                return mShadowMaxSizePressed
            }

        companion object {
            private const val SHADOW_COLOR_DEFAULT = -0x50000000
            private val SHADOW_SIZE = dp2px(8f)
            private fun dp2px(dpValue: Float): Int {
                val scale = Resources.getSystem().displayMetrics.density
                return (dpValue * scale + 0.5f).toInt()
            }
        }
    }

    class ShadowDrawable(content: Drawable?, radius: Float, shadowSize: Float, maxShadowSize: Float, private val mShadowStartColor: Int, private val isCircle: Boolean) : DrawableWrapper(content) {
        private var mShadowMultiplier = 1f
        private var mShadowTopScale = 1f
        private var mShadowHorizScale = 1f
        private var mShadowBottomScale = 1f
        private val mCornerShadowPaint: Paint
        private val mEdgeShadowPaint: Paint
        private val mContentBounds: RectF
        private var mCornerRadius: Float
        private var mCornerShadowPath: Path? = null
        // updated value with inset
        private var mMaxShadowSize = 0f
        // actual value set by developer
        private var mRawMaxShadowSize = 0f
        // multiplied value to account for shadow offset
        private var mShadowSize = 0f
        // actual value set by developer
        private var mRawShadowSize = 0f
        private var mDirty = true
        private val mShadowEndColor = 0
        private var mAddPaddingForCorners = false
        private var mRotation = 0f

        fun setAddPaddingForCorners(addPaddingForCorners: Boolean) {
            mAddPaddingForCorners = addPaddingForCorners
            invalidateSelf()
        }

        override fun setAlpha(alpha: Int) {
            super.setAlpha(alpha)
            mCornerShadowPaint.alpha = alpha
            mEdgeShadowPaint.alpha = alpha
        }

        override fun onBoundsChange(bounds: Rect) {
            mDirty = true
        }

        fun setShadowSize(shadowSize: Float, maxShadowSize: Float) {
            var shadowSize = shadowSize
            var maxShadowSize = maxShadowSize
            require(!(shadowSize < 0 || maxShadowSize < 0)) { "invalid shadow size" }
            shadowSize = toEven(shadowSize).toFloat()
            maxShadowSize = toEven(maxShadowSize).toFloat()
            if (shadowSize > maxShadowSize) {
                shadowSize = maxShadowSize
            }
            if (mRawShadowSize == shadowSize && mRawMaxShadowSize == maxShadowSize) {
                return
            }
            mRawShadowSize = shadowSize
            mRawMaxShadowSize = maxShadowSize
            mShadowSize = Math.round(shadowSize * mShadowMultiplier).toFloat()
            mMaxShadowSize = maxShadowSize
            mDirty = true
            invalidateSelf()
        }

        override fun getPadding(padding: Rect): Boolean {
            val vOffset = Math.ceil(calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius,
                    mAddPaddingForCorners).toDouble()).toInt()
            val hOffset = Math.ceil(calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius,
                    mAddPaddingForCorners).toDouble()).toInt()
            padding[hOffset, vOffset, hOffset] = vOffset
            return true
        }

        private fun calculateVerticalPadding(maxShadowSize: Float, cornerRadius: Float, addPaddingForCorners: Boolean): Float {
            return if (addPaddingForCorners) {
                (maxShadowSize * mShadowMultiplier + (1 - COS_45) * cornerRadius).toFloat()
            } else {
                maxShadowSize * mShadowMultiplier
            }
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }

        override fun draw(canvas: Canvas) {
            if (mDirty) {
                buildComponents(bounds)
                mDirty = false
            }
            drawShadow(canvas)
            super.draw(canvas)
        }

        fun setRotation(rotation: Float) {
            if (mRotation != rotation) {
                mRotation = rotation
                invalidateSelf()
            }
        }

        private fun drawShadow(canvas: Canvas) {
            if (isCircle) {
                val saved = canvas.save()
                canvas.translate(mContentBounds.centerX(), mContentBounds.centerY())
                canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
                canvas.restoreToCount(saved)
                return
            }
            val rotateSaved = canvas.save()
            canvas.rotate(mRotation, mContentBounds.centerX(), mContentBounds.centerY())
            val edgeShadowTop = -mCornerRadius - mShadowSize
            val shadowOffset = mCornerRadius
            val drawHorizontalEdges = mContentBounds.width() - 2 * shadowOffset > 0
            val drawVerticalEdges = mContentBounds.height() - 2 * shadowOffset > 0
            val shadowOffsetTop = mRawShadowSize - mRawShadowSize * mShadowTopScale
            val shadowOffsetHorizontal = mRawShadowSize - mRawShadowSize * mShadowHorizScale
            val shadowOffsetBottom = mRawShadowSize - mRawShadowSize * mShadowBottomScale
            val shadowScaleHorizontal = if (shadowOffset == 0f) 1f else shadowOffset / (shadowOffset + shadowOffsetHorizontal)
            val shadowScaleTop = if (shadowOffset == 0f) 1f else shadowOffset / (shadowOffset + shadowOffsetTop)
            val shadowScaleBottom = if (shadowOffset == 0f) 1f else shadowOffset / (shadowOffset + shadowOffsetBottom)
            // LT
            var saved = canvas.save()
            canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.top + shadowOffset)
            canvas.scale(shadowScaleHorizontal, shadowScaleTop)
            canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
            if (drawHorizontalEdges) { // TE
                canvas.scale(1f / shadowScaleHorizontal, 1f)
                canvas.drawRect(0f, edgeShadowTop, mContentBounds.width() - 2 * shadowOffset, -mCornerRadius, mEdgeShadowPaint)
            }
            canvas.restoreToCount(saved)
            // RB
            saved = canvas.save()
            canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.bottom - shadowOffset)
            canvas.scale(shadowScaleHorizontal, shadowScaleBottom)
            canvas.rotate(180f)
            canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
            if (drawHorizontalEdges) { // BE
                canvas.scale(1f / shadowScaleHorizontal, 1f)
                canvas.drawRect(0f, edgeShadowTop, mContentBounds.width() - 2 * shadowOffset, -mCornerRadius, mEdgeShadowPaint)
            }
            canvas.restoreToCount(saved)
            // LB
            saved = canvas.save()
            canvas.translate(mContentBounds.left + shadowOffset, mContentBounds.bottom - shadowOffset)
            canvas.scale(shadowScaleHorizontal, shadowScaleBottom)
            canvas.rotate(270f)
            canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
            if (drawVerticalEdges) { // LE
                canvas.scale(1f / shadowScaleBottom, 1f)
                canvas.drawRect(0f, edgeShadowTop, mContentBounds.height() - 2 * shadowOffset, -mCornerRadius, mEdgeShadowPaint)
            }
            canvas.restoreToCount(saved)
            // RT
            saved = canvas.save()
            canvas.translate(mContentBounds.right - shadowOffset, mContentBounds.top + shadowOffset)
            canvas.scale(shadowScaleHorizontal, shadowScaleTop)
            canvas.rotate(90f)
            canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
            if (drawVerticalEdges) { // RE
                canvas.scale(1f / shadowScaleTop, 1f)
                canvas.drawRect(0f, edgeShadowTop, mContentBounds.height() - 2 * shadowOffset, -mCornerRadius, mEdgeShadowPaint)
            }
            canvas.restoreToCount(saved)
            canvas.restoreToCount(rotateSaved)
        }

        private fun buildShadowCorners() {
            if (isCircle) {
                val size = mContentBounds.width() / 2 - 1f
                val innerBounds = RectF(-size, -size, size, size)
                val outerBounds = RectF(innerBounds)
                outerBounds.inset(-mShadowSize, -mShadowSize)
                if (mCornerShadowPath == null) {
                    mCornerShadowPath = Path()
                } else {
                    mCornerShadowPath!!.reset()
                }
                mCornerShadowPath!!.fillType = Path.FillType.EVEN_ODD
                mCornerShadowPath!!.moveTo(-size, 0f)
                mCornerShadowPath!!.rLineTo(-mShadowSize, 0f)
                // outer arc
                mCornerShadowPath!!.arcTo(outerBounds, 180f, 180f, false)
                mCornerShadowPath!!.arcTo(outerBounds, 360f, 180f, false)
                // inner arc
                mCornerShadowPath!!.arcTo(innerBounds, 540f, -180f, false)
                mCornerShadowPath!!.arcTo(innerBounds, 360f, -180f, false)
                mCornerShadowPath!!.close()
                val shadowRadius = -outerBounds.top
                if (shadowRadius > 0f) {
                    val startRatio = size / shadowRadius
                    mCornerShadowPaint.shader = RadialGradient(0f, 0f, shadowRadius, intArrayOf(0, mShadowStartColor, mShadowEndColor), floatArrayOf(0f, startRatio, 1f), Shader.TileMode.CLAMP)
                }
                return
            }
            val innerBounds = RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius)
            val outerBounds = RectF(innerBounds)
            outerBounds.inset(-mShadowSize, -mShadowSize)
            if (mCornerShadowPath == null) {
                mCornerShadowPath = Path()
            } else {
                mCornerShadowPath!!.reset()
            }
            mCornerShadowPath!!.fillType = Path.FillType.EVEN_ODD
            mCornerShadowPath!!.moveTo(-mCornerRadius, 0f)
            mCornerShadowPath!!.rLineTo(-mShadowSize, 0f)
            // outer arc
            mCornerShadowPath!!.arcTo(outerBounds, 180f, 90f, false)
            // inner arc
            mCornerShadowPath!!.arcTo(innerBounds, 270f, -90f, false)
            mCornerShadowPath!!.close()
            val shadowRadius = -outerBounds.top
            if (shadowRadius > 0f) {
                val startRatio = mCornerRadius / shadowRadius
                mCornerShadowPaint.shader = RadialGradient(0f, 0f, shadowRadius, intArrayOf(0, mShadowStartColor, mShadowEndColor), floatArrayOf(0f, startRatio, 1f), Shader.TileMode.CLAMP)
            }
            // we offset the content shadowSize/2 pixels up to make it more realistic.
            // this is why edge shadow shader has some extra space
            // When drawing bottom edge shadow, we use that extra space.
            mEdgeShadowPaint.shader = LinearGradient(0f, innerBounds.top, 0f, outerBounds.top, intArrayOf(mShadowStartColor, mShadowEndColor), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
            mEdgeShadowPaint.isAntiAlias = false
        }

        private fun buildComponents(bounds: Rect) {
            // Card is offset mShadowMultiplier * maxShadowSize to account for the shadow shift.
            // We could have different top-bottom offsets to avoid extra gap above but in that case
            // center aligning Views inside the CardView would be problematic.
            if (isCircle) {
                mCornerRadius = bounds.width() / 2.toFloat()
            }
            val verticalOffset = mRawMaxShadowSize * mShadowMultiplier
            mContentBounds[bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset, bounds.right - mRawMaxShadowSize] = bounds.bottom - verticalOffset
            wrappedDrawable!!.setBounds(mContentBounds.left.toInt(), mContentBounds.top.toInt(), mContentBounds.right.toInt(), mContentBounds.bottom.toInt())
            buildShadowCorners()
        }

        var cornerRadius: Float
            get() = mCornerRadius
            set(value) {
                var radius = value
                radius = Math.round(radius).toFloat()
                if (mCornerRadius == radius) {
                    return
                }
                mCornerRadius = radius
                mDirty = true
                invalidateSelf()
            }

        var shadowSize: Float
            get() = mRawShadowSize
            set(size) {
                setShadowSize(size, mRawMaxShadowSize)
            }

        var maxShadowSize: Float
            get() = mRawMaxShadowSize
            set(size) {
                setShadowSize(mRawShadowSize, size)
            }

        val minWidth: Float
            get() {
                val content = 2 *
                        Math.max(mRawMaxShadowSize, mCornerRadius + mRawMaxShadowSize / 2)
                return content + mRawMaxShadowSize * 2
            }

        val minHeight: Float
            get() {
                val content = 2 * Math.max(mRawMaxShadowSize, mCornerRadius
                        + mRawMaxShadowSize * mShadowMultiplier / 2)
                return content + mRawMaxShadowSize * mShadowMultiplier * 2
            }

        companion object {
            // used to calculate content padding
            private val COS_45 = Math.cos(Math.toRadians(45.0))

            /**
             * Casts the value to an even integer.
             */
            private fun toEven(value: Float): Int {
                val i = Math.round(value)
                return if (i % 2 == 1) i - 1 else i
            }

            private fun calculateHorizontalPadding(maxShadowSize: Float, cornerRadius: Float,
                                                   addPaddingForCorners: Boolean): Float {
                return if (addPaddingForCorners) {
                    (maxShadowSize + (1 - COS_45) * cornerRadius).toFloat()
                } else {
                    maxShadowSize
                }
            }
        }

        init {
            if (isCircle) {
                mShadowMultiplier = 1f
                mShadowTopScale = 1f
                mShadowHorizScale = 1f
                mShadowBottomScale = 1f
            }
            mCornerShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
            mCornerShadowPaint.style = Paint.Style.FILL
            mCornerRadius = Math.round(radius).toFloat()
            mContentBounds = RectF()
            mEdgeShadowPaint = Paint(mCornerShadowPaint)
            mEdgeShadowPaint.isAntiAlias = false
            setShadowSize(shadowSize, maxShadowSize)
        }
    }

    open class DrawableWrapper(var wrappedDrawable: Drawable?) : Drawable(), Drawable.Callback {

        private var mDrawable: Drawable? = null

        init {
            setDrawable(wrappedDrawable)
        }

        private fun setDrawable(drawable: Drawable?) {
            if (mDrawable != null) {
                mDrawable!!.callback = null
            }
            mDrawable = drawable
            drawable?.callback = this
        }

        override fun draw(canvas: Canvas) {
            mDrawable!!.draw(canvas)
        }

        override fun onBoundsChange(bounds: Rect) {
            mDrawable!!.bounds = bounds
        }

        override fun setChangingConfigurations(configs: Int) {
            mDrawable!!.changingConfigurations = configs
        }

        override fun getChangingConfigurations(): Int {
            return mDrawable!!.changingConfigurations
        }

        override fun setDither(dither: Boolean) {
            mDrawable!!.setDither(dither)
        }

        override fun setFilterBitmap(filter: Boolean) {
            mDrawable!!.isFilterBitmap = filter
        }

        override fun setAlpha(alpha: Int) {
            mDrawable!!.alpha = alpha
        }

        override fun setColorFilter(cf: ColorFilter?) {
            mDrawable!!.colorFilter = cf
        }

        override fun isStateful(): Boolean {
            return mDrawable!!.isStateful
        }

        override fun setState(stateSet: IntArray): Boolean {
            return mDrawable!!.setState(stateSet)
        }

        override fun getState(): IntArray {
            return mDrawable!!.state
        }

        override fun jumpToCurrentState() {
            DrawableCompat.jumpToCurrentState(mDrawable!!)
        }

        override fun getCurrent(): Drawable {
            return mDrawable!!.current
        }

        override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
            return super.setVisible(visible, restart) || mDrawable!!.setVisible(visible, restart)
        }

        override fun getOpacity(): Int {
            return mDrawable!!.opacity
        }

        override fun getTransparentRegion(): Region {
            return mDrawable!!.transparentRegion!!
        }

        override fun getIntrinsicWidth(): Int {
            return mDrawable!!.intrinsicWidth
        }

        override fun getIntrinsicHeight(): Int {
            return mDrawable!!.intrinsicHeight
        }

        override fun getMinimumWidth(): Int {
            return mDrawable!!.minimumWidth
        }

        override fun getMinimumHeight(): Int {
            return mDrawable!!.minimumHeight
        }

        override fun getPadding(padding: Rect): Boolean {
            return mDrawable!!.getPadding(padding)
        }

        override fun invalidateDrawable(who: Drawable) {
            invalidateSelf()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            scheduleSelf(what, `when`)
        }

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            unscheduleSelf(what)
        }

        override fun onLevelChange(level: Int): Boolean {
            return mDrawable!!.setLevel(level)
        }

        override fun setAutoMirrored(mirrored: Boolean) {
            DrawableCompat.setAutoMirrored(mDrawable!!, mirrored)
        }

        override fun isAutoMirrored(): Boolean {
            return DrawableCompat.isAutoMirrored(mDrawable!!)
        }

        override fun setTint(tint: Int) {
            DrawableCompat.setTint(mDrawable!!, tint)
        }

        override fun setTintList(tint: ColorStateList?) {
            DrawableCompat.setTintList(mDrawable!!, tint)
        }

        override fun setTintMode(tintMode: PorterDuff.Mode?) {
            DrawableCompat.setTintMode(mDrawable!!, tintMode!!)
        }

        override fun setHotspot(x: Float, y: Float) {
            DrawableCompat.setHotspot(mDrawable!!, x, y)
        }

        override fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {
            DrawableCompat.setHotspotBounds(mDrawable!!, left, top, right, bottom)
        }

    }

}