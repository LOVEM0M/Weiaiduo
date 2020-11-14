package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.graphics.Color
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.miyin.zhenbaoqi.R

class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val mContext = context
    private var mLeftImageView: ImageView? = null
    private var mCenterTextView: TextView? = null
    private var mRightImageView: ImageView? = null
    private var mRightTextView: TextView? = null
    private var mLineView: View? = null
    private var mOnTitleClickListener: OnTitleClickListener? = null

    init {
        mLeftImageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER
            setOnClickListener { mOnTitleClickListener?.onLeftViewClick() }
        }
        addView(mLeftImageView)

        mCenterTextView = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER
            layoutParams = params
            gravity = Gravity.CENTER_VERTICAL
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.sp_16))
            setTextColor(Color.parseColor("#333333"))
        }
        addView(mCenterTextView)

        mRightImageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER
            visibility = View.GONE
            setOnClickListener { mOnTitleClickListener?.onRightViewClick() }
        }
        addView(mRightImageView)

        mRightTextView = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.END
            layoutParams = params
            gravity = Gravity.CENTER_VERTICAL
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.sp_14))
            setTextColor(Color.parseColor("#333333"))
            setPadding(context.resources.getDimensionPixelOffset(R.dimen.dp_14), 0, context.resources.getDimensionPixelOffset(R.dimen.dp_14), 0)
            visibility = View.GONE
            setOnClickListener {
                mOnTitleClickListener?.onRightViewClick()
            }
        }
        addView(mRightTextView)

        mLineView = View(context).apply {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelOffset(R.dimen.dp_1))
            params.gravity = Gravity.BOTTOM
            layoutParams = params
            setBackgroundColor(Color.GRAY)

            visibility = View.GONE
        }
        addView(mLineView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 设置左边 ImageView 的宽高
        val leftImageViewParams = LayoutParams(h, h)
        mLeftImageView!!.layoutParams = leftImageViewParams

        // 设置右边 ImageView 的宽高
        val rightImageViewParams = LayoutParams(h, h)
        rightImageViewParams.gravity = Gravity.END
        mRightImageView!!.layoutParams = rightImageViewParams
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (null != mOnTitleClickListener) {
            mOnTitleClickListener = null
        }
    }

    fun setCenterTitle(title: String) {
        mCenterTextView?.text = title
    }

    fun setCenterTitleSize(@DimenRes dimenRes: Int) {
        mCenterTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(dimenRes))
    }

    fun setRightTitle(title: String) {
        mRightTextView?.text = title
        mRightTextView?.visibility = View.VISIBLE
        mRightImageView?.visibility = View.GONE
    }

    fun setRightTitleSize(@DimenRes dimenRes: Int) {
        mRightTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(dimenRes))
    }

    fun setRightTitleColor(color: Int) {
        mRightTextView?.setTextColor(color)
    }

    fun setLeftImageResource(@DrawableRes drawableRes: Int) {
        mLeftImageView?.setImageResource(drawableRes)
    }

    fun setRightImageResource(@DrawableRes drawableRes: Int) {
        mRightImageView?.setImageResource(drawableRes)
        mRightImageView?.visibility = View.VISIBLE
        mRightTextView?.visibility = View.GONE
    }

    fun setLineVisible() {
        mLineView?.visibility = View.VISIBLE
    }

    fun setOnTitleClickListener(onTitleClickListener: OnTitleClickListener) {
        mOnTitleClickListener = onTitleClickListener
    }

    interface OnTitleClickListener {
        fun onLeftViewClick()

        fun onRightViewClick()
    }

}
