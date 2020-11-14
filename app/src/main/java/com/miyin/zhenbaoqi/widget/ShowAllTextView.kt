package com.miyin.zhenbaoqi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class ShowAllTextView : AppCompatTextView {

    /**
     * 全文按钮点击事件
     */
    private var onAllSpanClickListener: ShowAllSpan.OnAllSpanClickListener? = null
    var maxShowLines = 0
        set(maxShowLines) {
            field = maxShowLines

            if (!TextUtils.isEmpty(mText)) {
                setMyText(mText!!)
            }
        }  //最大显示行数
    private var mText: CharSequence? = null

    // 实现 span 的点击
    private var mPressedSpan: ClickableSpan? = null
    private var result = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    /**
     * 调用此方法才有效果
     */
    fun setMyText(text: CharSequence) {
        super.setText(text)
        mText = text

        post { this.addEllipsisAndAllAtEnd() }
    }

    /**
     * 超过规定行数时, 在文末添加 "...全文"
     */
    private fun addEllipsisAndAllAtEnd() {
        if (this.maxShowLines in 1..(lineCount - 1)) {
            try {
                val moreWidth = getTheTextNeedWidth(paint, "...全文")
                /* 加上...全文 长度超过了 textView 的宽度, 则多减去5个字符 */
                if (layout.getLineRight(this.maxShowLines - 1) + moreWidth >= layout.width) {
                    this.text = text.subSequence(0, layout.getLineEnd(this.maxShowLines - 1) - 5)
                    /* 避免减5个字符后还是长度还是超出了,这里再减4个字符 */
                    if (layout.getLineRight(this.maxShowLines - 1) + moreWidth >= layout.width) {
                        this.text = text.subSequence(0, layout.getLineEnd(this.maxShowLines - 1) - 4)
                    }
                } else {
                    this.text = text.subSequence(0, layout.getLineEnd(this.maxShowLines - 1))
                }
                if (text.toString().endsWith("\n") && text.isNotEmpty()) {
                    this.text = text.subSequence(0, text.length - 1)
                }
                this.append("...")
                val sb = SpannableString("全文")
                sb.setSpan(ShowAllSpan(context, onAllSpanClickListener), 0, sb.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                this.append(sb)
            } catch (e: Exception) {
            }

        }
    }

    fun setOnAllSpanClickListener(onAllSpanClickListener: ShowAllSpan.OnAllSpanClickListener) {
        this.onAllSpanClickListener = onAllSpanClickListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val text = text
        val spannable = Spannable.Factory.getInstance().newSpannable(text)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPressedSpan = getPressedSpan(this, spannable, event)
                result = if (mPressedSpan != null) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(true)
                    }
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan))
                    true
                } else {
                    false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val mClickSpan = getPressedSpan(this, spannable, event)
                if ((null != mPressedSpan) and (mPressedSpan !== mClickSpan)) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(false)
                    }
                    mPressedSpan = null
                    Selection.removeSelection(spannable)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mPressedSpan != null) {
                    if (mPressedSpan is ShowAllSpan) {
                        (mPressedSpan as ShowAllSpan).setPressed(false)
                    }
                    mPressedSpan!!.onClick(this)
                }
                mPressedSpan = null
                Selection.removeSelection(spannable)
            }
        }
        return result

    }

    private fun getTheTextNeedWidth(thePaint: Paint, text: String): Int {
        val widths = FloatArray(text.length)
        thePaint.getTextWidths(text, widths)
        return widths.sumBy { it.toInt() }
    }

    private fun getPressedSpan(textView: TextView, spannable: Spannable, event: MotionEvent): ClickableSpan? {

        var mTouchSpan: ClickableSpan? = null

        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= textView.totalPaddingLeft
        x += textView.scrollX
        y -= textView.totalPaddingTop
        y += textView.scrollY
        val layout = layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val spans = spannable.getSpans(off, off, ShowAllSpan::class.java)
        if (spans != null && spans.isNotEmpty()) {
            mTouchSpan = spans[0]
        }
        return mTouchSpan
    }

    class ShowAllSpan(private val context: Context, private val clickListener: OnAllSpanClickListener?) : ClickableSpan() {
        private var isPressed = false

        override fun onClick(widget: View) {
            clickListener?.onClick(widget)
        }

        fun setPressed(pressed: Boolean) {
            isPressed = pressed
        }

        interface OnAllSpanClickListener {
            fun onClick(widget: View)
        }

        override fun updateDrawState(ds: TextPaint) {
            if (isPressed) {
                ds.bgColor = ContextCompat.getColor(context, android.R.color.darker_gray)
            } else {
                ds.bgColor = ContextCompat.getColor(context, android.R.color.transparent)
            }
            ds.color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            ds.isUnderlineText = false
        }
    }

}