package com.miyin.zhenbaoqi.widget

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet

import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.ToastUtils

class ContentWithSpaceEditText : AppCompatEditText {

    private var start: Int = 0
    private var count: Int = 0
    private var before: Int = 0
    private var contentType: Int = 0
    private var maxLength = 50
    private var digits: String? = null

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            this@ContentWithSpaceEditText.start = start
            this@ContentWithSpaceEditText.before = before
            this@ContentWithSpaceEditText.count = count
        }

        override fun afterTextChanged(s: Editable?) {
            if (s == null) {
                return
            }
            // 判断是否是在中间输入，需要重新计算
            val isMiddle = start + count < s.length
            // 在末尾输入时，是否需要加入空格
            var isNeedSpace = false
            if (!isMiddle && isSpace(s.length)) {
                isNeedSpace = true
            }
            if (isMiddle || isNeedSpace || count > 1) {
                var newStr = s.toString()
                newStr = newStr.replace(" ", "")
                val sb = StringBuilder()
                var spaceCount = 0
                for (i in newStr.indices) {
                    sb.append(newStr.substring(i, i + 1))
                    // 如果当前输入的字符下一位为空格(i+1+1+spaceCount)，因为i是从0开始计算的，所以一开始的时候需要先加1
                    if (isSpace(i + 2 + spaceCount)) {
                        sb.append(" ")
                        spaceCount += 1
                    }
                }
                removeTextChangedListener(this)
                s.replace(0, s.length, sb)
                // 如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                if (!isMiddle || count > 1) {
                    setSelection(if (s.length <= maxLength) s.length else maxLength)
                } else {
                    // 如果是删除
                    if (count == 0) {
                        // 如果删除时，光标停留在空格的前面，光标则要往前移一位
                        if (isSpace(start - before + 1)) {
                            setSelection(if (start - before > 0) start - before else 0)
                        } else {
                            setSelection(if (start - before + 1 > s.length) s.length else start - before + 1)
                        }
                    } else {
                        if (isSpace(start - before + count)) {
                            setSelection(if (start + count - before + 1 < s.length) start + count - before + 1 else s.length)
                        } else {
                            setSelection(start + count - before)
                        }
                    }// 如果是增加
                }
                addTextChangedListener(this)
            }
        }
    }

    val textWithoutSpace: String
        get() = super.getText()!!.toString().replace(" ", "")

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        parseAttributeSet(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        parseAttributeSet(context, attrs)
    }

    private fun parseAttributeSet(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ContentWithSpaceEditText, 0, 0)
        contentType = ta.getInt(R.styleable.ContentWithSpaceEditText_input_type, 0)
        // 必须调用recycle
        ta.recycle()
        initType()
        setSingleLine()
        addTextChangedListener(watcher)
    }

    private fun initType() {
        when (contentType) {
            TYPE_PHONE -> {
                maxLength = 13
                digits = "0123456789 "
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            TYPE_BANK_CARD -> {
                maxLength = 31
                digits = "0123456789 "
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            TYPE_ID_CARD -> {
                maxLength = 21
                digits = null
                inputType = InputType.TYPE_CLASS_TEXT
            }
        }
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    @Suppress("NAME_SHADOWING")
    override fun setInputType(type: Int) {
        var type = type
        if (contentType == TYPE_PHONE || contentType == TYPE_BANK_CARD) {
            type = InputType.TYPE_CLASS_NUMBER
        } else if (contentType == TYPE_ID_CARD) {
            type = InputType.TYPE_CLASS_TEXT
        }
        super.setInputType(type)
        /* 非常重要:setKeyListener要在setInputType后面调用，否则无效。*/
        if (!TextUtils.isEmpty(digits)) {
            keyListener = DigitsKeyListener.getInstance(digits!!)
        }
    }

    fun setContentType(contentType: Int) {
        this.contentType = contentType
        initType()
    }

    fun checkTextRight(): Boolean {
        val text = textWithoutSpace
        // 这里做个简单的内容判断
        when (contentType) {
            TYPE_PHONE -> when {
                TextUtils.isEmpty(text) -> ToastUtils.showToast("手机号不能为空，请输入正确的手机号")
                text.length < 11 -> ToastUtils.showToast("手机号不足11位，请输入正确的手机号")
                else -> return true
            }
            TYPE_BANK_CARD -> when {
                TextUtils.isEmpty(text) -> ToastUtils.showToast("银行卡号不能为空，请输入正确的银行卡号")
                text.length < 14 -> ToastUtils.showToast("银行卡号位数不正确，请输入正确的银行卡号")
                else -> return true
            }
            TYPE_ID_CARD -> when {
                TextUtils.isEmpty(text) -> ToastUtils.showToast("身份证号不能为空，请输入正确的身份证号")
                text.length < 18 -> ToastUtils.showToast("身份证号不正确，请输入正确的身份证号")
                else -> return true
            }
        }
        return false
    }

    private fun isSpace(length: Int) = when (contentType) {
        TYPE_PHONE -> isSpacePhone(length)
        TYPE_BANK_CARD -> isSpaceCard(length)
        TYPE_ID_CARD -> isSpaceIDCard(length)
        else -> false
    }

    private fun isSpacePhone(length: Int): Boolean {
        return length >= 4 && (length == 4 || (length + 1) % 5 == 0)
    }

    private fun isSpaceCard(length: Int): Boolean {
        return length % 5 == 0
    }

    private fun isSpaceIDCard(length: Int): Boolean {
        return length > 6 && (length == 7 || (length - 2) % 5 == 0)
    }

    companion object {
        private const val TYPE_PHONE = 0
        private const val TYPE_BANK_CARD = 1
        private const val TYPE_ID_CARD = 2
    }

}
