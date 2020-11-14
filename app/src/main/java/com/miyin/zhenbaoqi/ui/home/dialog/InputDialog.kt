package com.miyin.zhenbaoqi.ui.home.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.GridLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.util.ArrayMap
import android.view.*
import android.widget.LinearLayout
import com.miyin.zhenbaoqi.ui.home.adapter.EmojiAdapter
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.base.BaseDialogFragment
import com.miyin.zhenbaoqi.callback.OnDialogCallback
import com.miyin.zhenbaoqi.ext.getDimension
import com.miyin.zhenbaoqi.utils.EditWatcher
import com.miyin.zhenbaoqi.utils.KeyboardUtils
import com.miyin.zhenbaoqi.utils.SoftKeyBoardListener
import com.noober.background.drawable.DrawableCreator
import com.orhanobut.logger.Logger
import com.tencent.qcloud.tim.uikit.component.face.Emoji
import com.tencent.qcloud.tim.uikit.component.face.FaceManager
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.layout_emoji.view.*

class InputDialog :BaseDialogFragment() {

    private var mOnDialogCallback: OnDialogCallback? = null
    private var mName: String? = null
    private var mReplyId = 0
    private var mComment: String? = null
    private var mIsShowKeyBoard = false
    private var mIsClickSend = false
    private var mLastPosition = 0

    companion object {
        fun newInstance(replyId: Int, name: String) = InputDialog().apply {
            arguments = Bundle().apply {
                putString("name", name)
                putInt("reply_id", replyId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnDialogCallback = context as OnDialogCallback
    }

    override fun getContentView(): Int {
        arguments?.run {
            mName = getString("name")
            mReplyId = getInt("reply_id")
        }
        return R.layout.dialog_input
    }

    override fun initView(view: View) {
        SoftKeyBoardListener.setListener(activity!!, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                if (fl_container?.visibility == View.VISIBLE) {
                    fl_container?.visibility = View.GONE
                }
            }

            override fun keyBoardHide(height: Int) {
                if (!mIsShowKeyBoard) {
                    fl_container.visibility = View.VISIBLE

                    val params = view_empty?.layoutParams as LinearLayout.LayoutParams
                    params.weight = 1f
                    view_empty?.layoutParams = params
                } else {
                    mIsShowKeyBoard = false
                }
                if (fl_container?.visibility == View.GONE) {
                    dismiss()
                }
            }
        })

        view_pager.run {
            val emojiList = FaceManager.getEmojiList()
            val size = emojiList.size
            val pageCount = if (size % 20 == 0) {
                size / 20
            } else {
                size / 20 + 1
            }
            repeat((pageCount * 20 - size)) {
                val emoji = Emoji().apply {
                    icon = null
                    filter = "null"
                }
                emojiList.add(emoji)
            }
            val viewList = mutableListOf<View>()
            for (i in 0 until pageCount) {
                val pointView = View(context).apply {
                    val dimension = context.getDimension(R.dimen.dp_8).toInt()
                    layoutParams = LinearLayout.LayoutParams(dimension, dimension).apply {
                        if (i == 0) {
                            isSelected = true
                        } else {
                            leftMargin = dimension
                            isSelected = false
                        }
                    }

                    background = DrawableCreator.Builder()
                            .setCornersRadius(dimension.toFloat())
                            .setSelectedSolidColor(Color.RED, Color.GRAY)
                            .build()
                }
                ll_container.addView(pointView)

                val emojiView = View.inflate(context, R.layout.layout_emoji, null)
                viewList.add(emojiView)
            }
            adapter = MyViewPagerAdapter(viewList, emojiList)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    ll_container.getChildAt(mLastPosition).isSelected = false
                    ll_container.getChildAt(position).isSelected = true
                    mLastPosition = position
                }
            })
        }
        view_empty.setOnClickListener {
            mIsClickSend = false
            KeyboardUtils.hideKeyboard(et_content)
            dismiss()
        }
        et_content.run {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()

            if (mComment.isNullOrEmpty()) {
                hint = if (mName.isNullOrEmpty()) {
                    "有爱评论，说点好听点的~"
                } else {
                    "回复 @ $mName: "
                }
            } else {
                FaceManager.handlerEmojiText(et_content, mComment, false)
                et_content.setSelection(mComment!!.length)
            }

            addTextChangedListener(object : EditWatcher() {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    mComment = s?.toString()!!.trim { it <= ' ' }
                }

                override fun afterTextChanged(editable: Editable?) {
                    /*if (!TextUtils.equals(mComment, et_content.text.toString())) {
                        FaceManager.handlerEmojiText(et_content, et_content.text.toString(), false)
                    }*/
                    mComment = editable?.toString()!!.trim { it <= ' ' }
                }
            })
        }
        iv_emoji.setOnClickListener {
            mIsClickSend = false
            if (fl_container.visibility == View.GONE) {
                mIsShowKeyBoard = false
                val params = view_empty.layoutParams as LinearLayout.LayoutParams
                params.height = view_empty.height
                params.weight = 0f
                view_empty.layoutParams = params

                KeyboardUtils.hideKeyboard(et_content)

            } else {
                mIsShowKeyBoard = true
                fl_container.visibility = View.GONE
                KeyboardUtils.showKeyboard(et_content)
            }
        }
        iv_send.setOnClickListener {
            if (mComment.isNullOrEmpty()) {
                return@setOnClickListener
            }
            mIsClickSend = true

            if (mReplyId == 0) {
                Logger.d("reply_id == ${mReplyId}, name == ${mComment!!}, 我是一级的")
                mOnDialogCallback?.onDialog(mComment!!, 1)
            } else {
                Logger.d("reply_id == ${mReplyId}, name == ${mComment!!}")
                val arrayMap = ArrayMap<String, Any>().apply {
                    put("reply_id", mReplyId)
                    put("name", mComment!!)
                }
                mOnDialogCallback?.onDialog(arrayMap, 1)
            }

            if (mIsShowKeyBoard) {
                KeyboardUtils.hideKeyboard(et_content)
            } else {
                dismiss()
            }
        }
        dialog?.setOnShowListener {
            mIsShowKeyBoard = true
            KeyboardUtils.toggleSoftInput(et_content)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setDimAmount(0.2f)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        SoftKeyBoardListener.removeListener()
        if (!mIsClickSend) {
            mOnDialogCallback?.onDialog("", 2)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (null != mOnDialogCallback) {
            mOnDialogCallback = null
        }
    }

    override fun setWidth() = MATCH_PARENT

    override fun setHeight() = MATCH_PARENT

    internal inner class MyViewPagerAdapter(private val emojiViewList: List<View>, private val emojiList: MutableList<Emoji>) : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any) = view == obj

        override fun getCount() = emojiViewList.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val emojiView = emojiViewList[position].apply {
                recycler_view.run {
                    val list = emojiList.filterIndexed { index, _ ->
                        index >= position * 20 && index < (position + 1) * 20
                    }.toMutableList()
                    val emoji = Emoji().apply {
                        icon = BitmapFactory.decodeResource(resources, R.drawable.face_delete)
                        filter = "[delete]"
                    }
                    list.add(emoji)
                    val emojiAdapter = EmojiAdapter(list)
                    adapter = emojiAdapter
                    layoutManager = GridLayoutManager(context, 7)
                    emojiAdapter.setOnItemClickListener { _, _, position ->
                        val data = list[position]
                        if (data.filter == "[delete]") {
                            val editable = et_content.text
                            if (editable.toString().isEmpty()) {
                                return@setOnItemClickListener
                            }
                            et_content.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        } else if (data.filter != "null") {
                            val index = et_content.selectionStart
                            val editable = et_content.text
                            editable?.insert(index, data.filter)
                            FaceManager.handlerEmojiText(et_content, editable.toString(), true)
                        }
                    }
                }
            }
            container.addView(emojiView)
            return emojiView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(emojiViewList[position])
        }
    }

}
