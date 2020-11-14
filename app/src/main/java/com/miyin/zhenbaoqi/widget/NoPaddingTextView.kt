package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

import com.miyin.zhenbaoqi.R

class NoPaddingTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val mPaint = paint
    private val mBounds = Rect()
    // 是否去除字体内边距，true：去除 false：不去除
    private var mRemoveFontPadding = false

    init {
        initAttributes(context, attrs)
    }

    /**
     * 初始化属性
     */
    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoPaddingTextView)
        mRemoveFontPadding = typedArray.getBoolean(R.styleable.NoPaddingTextView_removeDefaultPadding, true)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mRemoveFontPadding) {
            calculateTextParams()
            setMeasuredDimension(mBounds.right - mBounds.left, -mBounds.top + mBounds.bottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawText(canvas)
    }

    /**
     * 计算文本参数
     */
    private fun calculateTextParams(): String {
        val text = text.toString()
        val textLength = text.length
        mPaint.getTextBounds(text, 0, textLength, mBounds)
        if (textLength == 0) {
            mBounds.right = mBounds.left
        }
        return text
    }

    /**
     * 绘制文本
     */
    private fun drawText(canvas: Canvas) {
        val text = calculateTextParams()
        val left = mBounds.left
        val bottom = mBounds.bottom
        mBounds.offset(-mBounds.left, -mBounds.top)
        mPaint.isAntiAlias = true
        mPaint.color = currentTextColor
        canvas.drawText(text, (-left).toFloat(), (mBounds.bottom - bottom).toFloat(), mPaint)
    }

}
