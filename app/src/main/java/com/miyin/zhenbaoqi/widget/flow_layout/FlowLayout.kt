package com.miyin.zhenbaoqi.widget.flow_layout

import android.content.Context
import androidx.core.text.TextUtilsCompat
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.View
import android.view.ViewGroup
import com.miyin.zhenbaoqi.R
import java.util.*

open class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {

    private var mAllViews: MutableList<MutableList<View?>> = ArrayList()
    private var mLineHeight: MutableList<Int> = ArrayList()
    private var mLineWidth: MutableList<Int> = ArrayList()
    private var mGravity = LEFT
    private var mHorizontalSpace = 0
    private var mVerticalSpace = 0
    private var lineViews: MutableList<View?> = ArrayList()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout).apply {
            mGravity = getInt(R.styleable.TagFlowLayout_tag_gravity, LEFT)
            mHorizontalSpace = getDimensionPixelOffset(R.styleable.TagFlowLayout_horizontal_space, 0)
            mVerticalSpace = getDimensionPixelOffset(R.styleable.TagFlowLayout_vertical_space, 0)
            val layoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
            if (layoutDirection == LayoutDirection.RTL) {
                mGravity = if (mGravity == LEFT) {
                    RIGHT
                } else {
                    LEFT
                }
            }
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        // wrap_content
        var width = 0
        var height = 0
        var lineWidth = 0
        var lineHeight = 0
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                if (i == cCount - 1) {
                    width = Math.max(lineWidth, width)
                    height += lineHeight
                }
                continue
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight) {
                width = Math.max(width, lineWidth)
                lineWidth = childWidth
                height += lineHeight
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width)
                height += lineHeight
            }
        }
        setMeasuredDimension(
                if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width + paddingLeft + paddingRight,
                if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height + paddingTop + paddingBottom //
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAllViews.clear()
        mLineHeight.clear()
        mLineWidth.clear()
        lineViews.clear()
        val width = width
        var lineWidth = 0
        var lineHeight = 0
        val cCount = childCount
        for (i in 0 until cCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - paddingLeft - paddingRight) {
                mLineHeight.add(lineHeight)
                mAllViews.add(lineViews)
                mLineWidth.add(lineWidth)
                lineWidth = 0
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
                lineViews = ArrayList()
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin)
            lineViews.add(child)
        }
        mLineHeight.add(lineHeight)
        mLineWidth.add(lineWidth)
        mAllViews.add(lineViews)
        var left = paddingLeft
        var top = paddingTop
        val lineNum = mAllViews.size
        for (i in 0 until lineNum) {
            lineViews = mAllViews[i]
            lineHeight = mLineHeight[i]
            // set gravity
            val currentLineWidth = mLineWidth[i]
            when (mGravity) {
                LEFT -> left = paddingLeft
                CENTER -> left = (width - currentLineWidth) / 2 + paddingLeft
                RIGHT -> {
                    //  适配了rtl，需要补偿一个padding值
                    left = width - (currentLineWidth + paddingLeft) - paddingRight
                    //  适配了rtl，需要把lineViews里面的数组倒序排
                    lineViews.reverse()
                }
            }
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child!!.visibility == View.GONE) {
                    continue
                }
                val lp = child.layoutParams as MarginLayoutParams
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val rc = lc + child.measuredWidth
                val bc = tc + child.measuredHeight
                child.layout(lc, tc, rc, bc)
                left += (child.measuredWidth + lp.leftMargin + lp.rightMargin)
            }
            top += lineHeight
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    companion object {
        private const val LEFT = -1
        private const val CENTER = 0
        private const val RIGHT = 1
    }

}
