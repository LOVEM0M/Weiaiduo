package com.miyin.zhenbaoqi.widget.flow_layout

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.miyin.zhenbaoqi.R
import com.miyin.zhenbaoqi.utils.DensityUtils.dp2px
import java.util.*

class TagFlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FlowLayout(context, attrs, defStyle), TagAdapter.OnDataChangedListener {

    private var mTagAdapter: TagAdapter? = null
    private var mSelectedMax: Int = -1
    private val mSelectedView = mutableSetOf<Int>()
    private var mOnSelectListener: OnSelectListener? = null
    private var mOnTagClickListener: OnTagClickListener? = null

    interface OnSelectListener {
        fun onSelected(selectPosSet: Set<Int>?)
    }

    interface OnTagClickListener {
        fun onTagClick(view: View, position: Int, parent: FlowLayout)
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout).apply {
            mSelectedMax = getInt(R.styleable.TagFlowLayout_max_select, -1)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val cCount = childCount
        (0 until cCount)
                .map { getChildAt(it) as TagView }
                .filter { it.visibility != View.GONE && it.tagView.visibility == View.GONE }
                .forEach { it.visibility = View.GONE }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener?) {
        mOnSelectListener = onSelectListener
    }

    fun setOnTagClickListener(onTagClickListener: OnTagClickListener?) {
        mOnTagClickListener = onTagClickListener
    }

    fun setAdapter(adapter: TagAdapter) {
        mTagAdapter = adapter
        mTagAdapter!!.setOnDataChangedListener(this)
        mSelectedView.clear()
        changeAdapter()
    }

    private fun changeAdapter() {
        removeAllViews()
        val adapter = mTagAdapter
        var tagViewContainer: TagView
        for (i in 0 until adapter!!.count) {
            val tagView = adapter.getView(this, i, adapter.getItem(i))
            tagViewContainer = TagView(context)
            tagView.isDuplicateParentStateEnabled = true
            if (tagView.layoutParams != null) {
                tagViewContainer.layoutParams = tagView.layoutParams
            } else {
                val lp = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                lp.setMargins(dp2px(5f), dp2px(5f), dp2px(5f), dp2px(5f))
                tagViewContainer.layoutParams = lp
            }
            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            tagView.layoutParams = lp
            tagViewContainer.addView(tagView)
            addView(tagViewContainer)
            if (mTagAdapter!!.setSelected(i, adapter.getItem(i))) {
                setChildChecked(i, tagViewContainer)
            }
            tagView.isClickable = false
            val finalTagViewContainer = tagViewContainer
            tagViewContainer.setOnClickListener {
                doSelect(finalTagViewContainer, i)
                mOnTagClickListener?.onTagClick(finalTagViewContainer, i, this@TagFlowLayout)
            }
        }
    }

    fun setMaxSelectCount(count: Int) {
        if (mSelectedView.size > count) {
            mSelectedView.clear()
        }
        mSelectedMax = count
    }

    val selectedList: Set<Int>
        get() = HashSet(mSelectedView)

    private fun setChildChecked(position: Int, view: TagView) {
        view.isChecked = true
        mTagAdapter!!.onSelected(position, view.tagView)
    }

    private fun setChildUnChecked(position: Int, view: TagView) {
        view.isChecked = false
        mTagAdapter!!.unSelected(position, view.tagView)
    }

    private fun doSelect(child: TagView, position: Int) {
        if (!child.isChecked) { //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size == 1) {
                val iterator: Iterator<Int> = mSelectedView.iterator()
                val preIndex = iterator.next()
                val pre = getChildAt(preIndex) as TagView
                setChildUnChecked(preIndex, pre)
                setChildChecked(position, child)
                mSelectedView.remove(preIndex)
                mSelectedView.add(position)
            } else {
                if (mSelectedMax > 0 && mSelectedView.size >= mSelectedMax) {
                    return
                }
                setChildChecked(position, child)
                mSelectedView.add(position)
            }
        } else {
            setChildUnChecked(position, child)
            mSelectedView.remove(position)
        }
        if (mOnSelectListener != null) {
            mOnSelectListener!!.onSelected(HashSet(mSelectedView))
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState())
        var selectPos = StringBuilder()
        if (mSelectedView.size > 0) {
            for (key in mSelectedView) {
                selectPos.append(key).append("|")
            }
            selectPos = StringBuilder(selectPos.substring(0, selectPos.length - 1))
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos.toString())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val mSelectPos = state.getString(KEY_CHOOSE_POS)
            if (!TextUtils.isEmpty(mSelectPos)) {
                val split = mSelectPos!!.split("\\|").toTypedArray()
                for (pos in split) {
                    val index = pos.toInt()
                    mSelectedView.add(index)
                    val tagView = getChildAt(index) as TagView?
                    if (null != tagView) {
                        setChildChecked(index, tagView)
                    }
                }
            }
            super.onRestoreInstanceState(state.getParcelable(KEY_DEFAULT))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun onChanged() {
        mSelectedView.clear()
        changeAdapter()
    }

    companion object {
        private const val KEY_CHOOSE_POS = "key_choose_pos"
        private const val KEY_DEFAULT = "key_default"
    }

}
