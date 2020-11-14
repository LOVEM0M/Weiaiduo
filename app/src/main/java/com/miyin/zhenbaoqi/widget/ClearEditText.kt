package com.miyin.zhenbaoqi.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.miyin.zhenbaoqi.R

class ClearEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr), View.OnFocusChangeListener, TextWatcher {

    /**
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null
    /**
     * 控件是否有焦点
     */
    private var hasFocus = false

    init {
        // 获取 EditText 的 DrawableRight,假如没有设置我们就使用默认的图片  
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(context, R.drawable.ic_clear)
        }
        mClearDrawable?.setBounds(0, 0, mClearDrawable!!.intrinsicWidth, mClearDrawable!!.intrinsicHeight)
        // 默认设置隐藏图标  
        setClearIconVisible(false)
        // 设置焦点改变的监听  
        onFocusChangeListener = this
        // 设置输入框里面内容发生改变的监听  
        addTextChangedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val touchable = event.x > width - totalPaddingRight && event.x < width - paddingRight
                if (touchable) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (hasFocus) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun afterTextChanged(s: Editable) {

    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(text!!.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     *  设置清除图标的显示与隐藏，调用 setCompoundDrawables 为 EditText 绘制上去 
     *  @param visible     
     */
    private fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3])
    }

}
