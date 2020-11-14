package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.miyin.zhenbaoqi.R

class LineFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val mPaint: Paint
    private var mLineColor: Int = 0
    private var mLeftPadding: Float = 0f
    private var mRightPadding: Float = 0f
    private var mLineHeight: Float = 0f

    init {
        context.obtainStyledAttributes(attrs, R.styleable.LineFrameLayout).apply {
            mLineColor = getColor(R.styleable.LineFrameLayout_lineColor, ContextCompat.getColor(context, R.color.line_eb))
            mLineHeight = getDimension(R.styleable.LineFrameLayout_lineHeight, resources.getDimension(R.dimen.dp_1))
            mLeftPadding = getDimension(R.styleable.LineFrameLayout_leftPadding, 0f)
            mRightPadding = getDimension(R.styleable.LineFrameLayout_rightPadding, 0f)
            recycle()
        }

        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.color = mLineColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(mLeftPadding, height - mLineHeight, width - mRightPadding, height - mLineHeight, mPaint)
    }

}
