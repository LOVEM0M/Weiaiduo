package com.miyin.zhenbaoqi.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.miyin.zhenbaoqi.R
import com.noober.background.drawable.DrawableCreator

class LineTitle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private var mTitle: String? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.LineTitle).apply {
            mTitle = getString(R.styleable.LineTitle_line_title)
            recycle()
        }

        View(context).apply {
            val params = LayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_90), resources.getDimensionPixelOffset(R.dimen.dp_1))
            background = DrawableCreator.Builder()
                    .setGradientAngle(0)
                    .setGradientColor(0xFFFFFFFF.toInt(), 0xFF97753D.toInt())
                    .build()
            addView(this, params)
        }

        val textView = TextView(context)
        textView.text = mTitle
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_12))
        textView.setTextColor(0xFF97753D.toInt())
        textView.setPadding(resources.getDimensionPixelOffset(R.dimen.dp_10), 0, resources.getDimensionPixelOffset(R.dimen.dp_10), 0)
        addView(textView)

        View(context).apply {
            val params = LayoutParams(resources.getDimensionPixelOffset(R.dimen.dp_90), resources.getDimensionPixelOffset(R.dimen.dp_1))
            background = DrawableCreator.Builder()
                    .setGradientAngle(0)
                    .setGradientColor(0xFF97753D.toInt(), 0xFFFFFFFF.toInt())
                    .build()
            addView(this, params)
        }
    }

}
