package com.miyin.zhenbaoqi.widget

import android.content.Context
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.ext.getDimensionPixelSize
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.noober.background.drawable.DrawableCreator

class AddedView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var mCountView: EditText
    private var mOnNumberChangedListener: OnNumberChangedListener? = null
    private var mNumber = 1
    private var mMaxNumber = 6

    init {
        val reduceView = TextView(context).apply {
            layoutParams = LayoutParams(context.getDimensionPixelSize(R.dimen.dp_30), context.getDimensionPixelSize(R.dimen.dp_30))
            background = setSolid(context)
            gravity = Gravity.CENTER
            text = "-"
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_18))
            setOnClickListener {
                val count = mCountView.text.toString().trim().toInt()
                if (count == 1) {
                    return@setOnClickListener
                }
                mNumber = count - 1
                mCountView.setText("$mNumber")
                mOnNumberChangedListener?.onNumberChanged(mNumber)
            }
        }
        addView(reduceView)

        mCountView = EditText(context).apply {
            val params = LayoutParams(context.resources.getDimensionPixelOffset(R.dimen.dp_60), context.resources.getDimensionPixelSize(R.dimen.dp_30)).apply {
                leftMargin = context.getDimensionPixelSize(R.dimen.dp_9)
                rightMargin = context.getDimensionPixelSize(R.dimen.dp_9)
            }
            layoutParams = params
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_14))
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setText("$mNumber")
            setPadding(0, 0, 0, 0)
            inputType = InputType.TYPE_CLASS_NUMBER
            background = setSolid(context)
            addTextChangedListener(object : EditWatcher() {
                override fun afterTextChanged(editable: Editable?) {
                    val count = editable.toString().trim { it <= ' ' }
                    when {
                        TextUtils.isEmpty(count) -> {
                            mNumber = 1
                            setText("$mNumber")
                        }
                        count.toInt() > mMaxNumber -> {
                            mNumber = mMaxNumber
                            setText("$mNumber")
                        }
                        else -> mNumber = count.toInt()
                    }
                    mOnNumberChangedListener?.onNumberChanged(mNumber)
                }
            })
        }
        addView(mCountView)

        val addedView = TextView(context).apply {
            layoutParams = LayoutParams(context.getDimensionPixelSize(R.dimen.dp_30), context.getDimensionPixelSize(R.dimen.dp_30))
            background = setSolid(context)
            text = "+"
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getDimension(R.dimen.sp_14))
            gravity = Gravity.CENTER
            setOnClickListener {
                val count = mCountView.text.toString().trim().toInt()
                if (count == mMaxNumber) {
                    return@setOnClickListener
                }
                mNumber = count + 1
                mCountView.setText("$mNumber")
                mOnNumberChangedListener?.onNumberChanged(mNumber)
            }
        }
        addView(addedView)
    }

    fun setMaxNumber(maxNumber: Int) {
        mMaxNumber = maxNumber
    }

    fun setOnNumberChangedListener(listener: OnNumberChangedListener) {
        mOnNumberChangedListener = listener
    }

    private fun setSolid(context: Context) = DrawableCreator.Builder()
            .setSolidColor(0xFFEFEFEF.toInt())
            .setCornersRadius(context.resources.getDimension(R.dimen.dp_1))
            .build()

    interface OnNumberChangedListener {
        fun onNumberChanged(number: Int)
    }

}
